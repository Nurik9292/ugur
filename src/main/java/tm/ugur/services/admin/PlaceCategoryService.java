package tm.ugur.services.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.PlaceCategory;
import tm.ugur.repo.PlaceCategoryRepository;
import tm.ugur.util.pagination.PaginationService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PlaceCategoryService {

    private final PlaceCategoryRepository placeCategoryRepository;
    private final PaginationService paginationService;

    @Autowired
    public PlaceCategoryService(PlaceCategoryRepository placeCategoryRepository,
                                PaginationService paginationService) {
        this.placeCategoryRepository = placeCategoryRepository;
        this.paginationService = paginationService;
    }


    public List<PlaceCategory> findAll(){
        return placeCategoryRepository.findAll();
    }

    public Page<PlaceCategory> findAll(int pageNumber, int itemsPerPage, String sortBy)
    {
        return this.paginationService.createPage(placeCategoryRepository.findAll(Sort.by(sortBy)), pageNumber, itemsPerPage);
    }

    public Page<PlaceCategory> findAll(int pageNumber, int itemsPerPage)
    {
        return paginationService.createPage(placeCategoryRepository.findAll(), pageNumber, itemsPerPage);
    }


    public Page<PlaceCategory> getPlaceCategoryPages(String page, String items, String sortBy){
        int pageNumber = page == null ? 1 : Integer.parseInt(page);
        int itemsPerPage = items == null ? 10 : Integer.parseInt(items);


        List<PlaceCategory> placeCategories = !sortBy.isBlank()
                ? placeCategoryRepository.findAll(Sort.by(sortBy)) : placeCategoryRepository.findAll();;

        return this.paginationService.createPage(placeCategories, pageNumber, itemsPerPage);
    }

    @Transactional
    public void store(PlaceCategory placeCategory){
        placeCategory.setUpdatedAt(new Date());
        placeCategory.setCreatedAt(new Date());
        placeCategoryRepository.save(placeCategory);
    }

    public Optional<PlaceCategory> findOne(long id){
        return placeCategoryRepository.findById(id);
    }


    @Transactional
    public void update(long id, PlaceCategory placeCategory){
        placeCategory.setId(id);
        placeCategory.setUpdatedAt(new Date());
        this.placeCategoryRepository.save(placeCategory);
    }

    @Transactional
    public void delete(Long id){
        this.placeCategoryRepository.deleteById(id);
    }
}
