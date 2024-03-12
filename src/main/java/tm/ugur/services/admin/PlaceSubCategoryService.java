package tm.ugur.services.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.PlaceSubCategory;
import tm.ugur.repo.PlaceSubCategoryRepository;
import tm.ugur.util.pagination.PaginationService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PlaceSubCategoryService {

    private final PlaceSubCategoryRepository placeSubCategoryRepository;
    private final PaginationService paginationService;

    public PlaceSubCategoryService(PlaceSubCategoryRepository placeSubCategoryRepository,
                                   PaginationService paginationService) {
        this.placeSubCategoryRepository = placeSubCategoryRepository;
        this.paginationService = paginationService;
    }

    public List<PlaceSubCategory> findAll(){
        return placeSubCategoryRepository.findAll();
    }

    public Page<PlaceSubCategory> findAll(int pageNumber, int itemsPerPage, String sortBy)
    {
        return this.paginationService.createPage(placeSubCategoryRepository.findAll(Sort.by(sortBy)), pageNumber, itemsPerPage);
    }

    public Page<PlaceSubCategory> findAll(int pageNumber, int itemsPerPage)
    {
        return paginationService.createPage(placeSubCategoryRepository.findAll(), pageNumber, itemsPerPage);
    }


    public Page<PlaceSubCategory> getPlaceSubCategoryPages(String page, String items, String sortBy){
        int pageNumber = page == null ? 1 : Integer.parseInt(page);
        int itemsPerPage = items == null ? 10 : Integer.parseInt(items);


        List<PlaceSubCategory> placeCategories = !sortBy.isBlank()
                ? placeSubCategoryRepository.findAll(Sort.by(sortBy)) : placeSubCategoryRepository.findAll();;

        return this.paginationService.createPage(placeCategories, pageNumber, itemsPerPage);
    }

    @Transactional
    public void store(PlaceSubCategory placeCategory){
        placeCategory.setUpdatedAt(new Date());
        placeCategory.setCreatedAt(new Date());
        placeSubCategoryRepository.save(placeCategory);
    }

    public Optional<PlaceSubCategory> findOne(long id){
        return placeSubCategoryRepository.findById(id);
    }


    @Transactional
    public void update(long id, PlaceSubCategory placeCategory){
        placeCategory.setId(id);
        placeCategory.setUpdatedAt(new Date());
        placeSubCategoryRepository.save(placeCategory);
    }

    @Transactional
    public void delete(Long id){
        placeSubCategoryRepository.deleteById(id);
    }
}
