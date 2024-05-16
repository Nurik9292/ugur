package tm.ugur.services.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.PlaceThumb;
import tm.ugur.repo.PlaceThumbRepository;
import tm.ugur.storage.FileSystemStorageService;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class PlaceThumbService {

    private final PlaceThumbRepository thumbRepository;
    private final FileSystemStorageService storageService;

    @Autowired
    public PlaceThumbService(PlaceThumbRepository thumbRepository, FileSystemStorageService storageService) {
        this.thumbRepository = thumbRepository;
        this.storageService = storageService;
    }

    @Transactional
    public PlaceThumb store(PlaceThumb placeThumb){
        placeThumb.setCreatedAt(new Date());
        placeThumb.setUpdatedAt(new Date());
        return thumbRepository.save(placeThumb);
    }

    @Transactional
    public void delete(PlaceThumb placeThumb){
        storageService.delete(placeThumb.getPath());
        thumbRepository.delete(placeThumb);
    }
}
