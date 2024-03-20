package tm.ugur.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tm.ugur.storage.FileSystemStorageService;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final FileSystemStorageService storageService;

    @Autowired
    public ImageController(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable("imageName") String imageName) {
        System.out.println(imageName);
        System.out.println("test");

        Resource image = storageService.loadAsResource(imageName);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }
}
