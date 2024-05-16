package tm.ugur.services.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.PlaceImage;
import tm.ugur.repo.PlaceImageRepository;
import tm.ugur.storage.FileSystemStorageService;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class PlaceImageService {

    private final PlaceImageRepository imageRepository;
    private final FileSystemStorageService storageService;
    @Autowired
    public PlaceImageService(PlaceImageRepository imageRepository, FileSystemStorageService storageService) {
        this.imageRepository = imageRepository;
        this.storageService = storageService;
    }

    @Transactional
    public PlaceImage store(PlaceImage placeImage){
        placeImage.setCreatedAt(new Date());
        placeImage.setUpdatedAt(new Date());
        return imageRepository.save(placeImage);
    }

    @Transactional
    public void delete(PlaceImage placeImage){
        storageService.delete(placeImage.getPath());
        imageRepository.delete(placeImage);
    }
}
