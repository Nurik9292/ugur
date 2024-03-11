package tm.ugur.services.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tm.ugur.models.Place;
import tm.ugur.models.Route;
import tm.ugur.repo.PlaceRepository;
import tm.ugur.util.pagination.PaginationService;

import java.util.List;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PaginationService paginationService;


    @Autowired
    public PlaceService(PlaceRepository placeRepository, PaginationService paginationService) {
        this.placeRepository = placeRepository;
        this.paginationService = paginationService;
    }

    public List<Place> findAll(){
        return placeRepository.findAll();
    }

    public Page<Place> findAll(int pageNumber, int itemsPerPage, String sortBy)
    {
        return this.paginationService.createPage(placeRepository.findAll(Sort.by(sortBy)), pageNumber, itemsPerPage);
    }

    public Page<Place> findAll(int pageNumber, int itemsPerPage)
    {
        return paginationService.createPage(placeRepository.findAll(), pageNumber, itemsPerPage);
    }


    public Page<Place> getPlacePages(String page, String items, String sortBy){
        int pageNumber = page == null ? 1 : Integer.parseInt(page);
        int itemsPerPage = items == null ? 10 : Integer.parseInt(items);


        List<Place> places = !sortBy.isBlank()
                ? placeRepository.findAll(Sort.by(sortBy)) : placeRepository.findAll();;

        return this.paginationService.createPage(places, pageNumber, itemsPerPage);
    }
}
