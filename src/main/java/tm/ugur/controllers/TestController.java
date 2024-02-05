package tm.ugur.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.*;

@Controller
public class TestController {

    @GetMapping("/test")
    public String test(){
        File inputFile = new File("routesIn.json"); // Путь к входному файлу
        File outputFile = new File("routes.json"); // Путь к выходному файлу

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("lats", "lng");
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Замена выполнена успешно.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
