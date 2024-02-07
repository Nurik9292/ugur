package tm.ugur.dto;

import jakarta.validation.constraints.NotEmpty;

public class ClientDTO {

    @NotEmpty(message = "Заполните поле.")
    private String phone;



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
