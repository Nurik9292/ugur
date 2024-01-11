package tm.ugur.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tm.ugur.models.Person;
import tm.ugur.services.PersonService;

import java.util.Objects;

@Controller
@RequestMapping("/users")
public class UserController {

    private final PersonService personService;

    @Autowired
    public UserController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("title", " Пользователи");
        model.addAttribute("page", "user-index");
        model.addAttribute("users", this.personService.index());
        return "layouts/users/index";
    }


    @GetMapping("/create")
    public String create(@ModelAttribute("person") Person person, Model model){
        model.addAttribute("page", "user-create");
        model.addAttribute("title", "Создать пользователя");
        return "layouts/users/create";
    }

    @PostMapping("/store")
    public String store(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()) {
            if(Objects.requireNonNull(bindingResult.getFieldError()).getField().equals("userName"))
                model.addAttribute("nameError", true);
            if(Objects.requireNonNull(bindingResult.getFieldError()).getField().equals("password"))
                model.addAttribute("passwordError", true);
            model.addAttribute("page", "user-create");
            model.addAttribute("title", "Создать пользователя");
            return "layouts/users/create";
        }

        this.personService.store(person);

        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){


        model.addAttribute("title", "Изменить пользователя");
        model.addAttribute("page", "user-edit");
        model.addAttribute("person", this.personService.findOne(id));

        return "layouts/users/edit";
    }

    @PatchMapping("/update/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id, Model model){;
        if(bindingResult.hasErrors() && !person.getPassword().isEmpty()){
            if(Objects.requireNonNull(bindingResult.getFieldError()).getField().equals("userName"))
                model.addAttribute("nameError", true);
            if(Objects.requireNonNull(bindingResult.getFieldError()).getField().equals("password"))
                model.addAttribute("passwordError", true);
            model.addAttribute("page", "user-edit");
            model.addAttribute("title", "Изменить пользователя");
            return "layouts/users/edit";
        }

        this.personService.upodate(id, person);

        return "redirect:/layouts/users/edit";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        this.personService.delete(id);

        return "redirect:/users";
    }
}
