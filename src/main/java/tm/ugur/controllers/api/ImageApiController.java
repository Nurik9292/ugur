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
public class ImageApiController {

    private final FileSystemStorageService storageService;

    @Autowired
    public ImageApiController(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/place/{imageName}")
    public ResponseEntity<Resource> getPlaceImage(@PathVariable("imageName") String imageName) {

        Resource image = storageService.loadAsResource("place/" + imageName);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    @GetMapping("/place/thumb/{imageName}")
    public ResponseEntity<Resource> getPlaceThumb(@PathVariable("imageName") String imageName) {

        Resource image = storageService.loadAsResource("place/thumb/" + imageName);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    @GetMapping("place/category/{imageName}")
    public ResponseEntity<Resource> getPlaceCategoryImage(@PathVariable("imageName") String imageName){
        Resource image = storageService.loadAsResource("place/category/" + imageName);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }
}
