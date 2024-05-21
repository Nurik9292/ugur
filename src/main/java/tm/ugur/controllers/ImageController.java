package tm.ugur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import tm.ugur.storage.FileSystemStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/images")
public class ImageController {

    @Value("${upload.image}")
    String uploadPth;

    private final FileSystemStorageService storageService;

    @Autowired
    public ImageController(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/place/thumb/{filename}")
    public ResponseEntity<byte[]> getThumb(@PathVariable String filename){
        Path path = Paths.get(uploadPth + "/place/thumb/", filename);
        try {

            byte[] imageBytes = Files.readAllBytes(path);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/places/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename){
        Resource image = storageService.loadAsResource("place/" + filename);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);

    }

    @GetMapping("/place/category/{filename}")
    public ResponseEntity<Resource> getCategoryThumb(@PathVariable String filename) {

        Resource thumb = storageService.loadAsResource("place/category/" +filename);

        return ResponseEntity.ok()
                .contentType(new MediaType("image", "svg+xml"))
                .body(thumb);
    }
}
