package tm.ugur.services.admin;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.Place;
import tm.ugur.models.PlacePhone;
import tm.ugur.models.Route;
import tm.ugur.models.SocialNetwork;
import tm.ugur.repo.PlacePhoneRepository;
import tm.ugur.repo.PlaceRepository;
import tm.ugur.repo.SocialNetworkRepository;
import tm.ugur.util.pagination.PaginationService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlacePhoneRepository placePhoneRepository;
    private final SocialNetworkRepository socialNetworkRepository;
    private final PaginationService paginationService;
    private final GeometryFactory geometryFactory;

    @Autowired
    public PlaceService(PlaceRepository placeRepository,
                        PlacePhoneRepository placePhoneRepository,
                        SocialNetworkRepository socialNetworkRepository,
                        PaginationService paginationService,
                        GeometryFactory geometryFactory) {
        this.placeRepository = placeRepository;
        this.placePhoneRepository = placePhoneRepository;
        this.socialNetworkRepository = socialNetworkRepository;
        this.paginationService = paginationService;
        this.geometryFactory = geometryFactory;
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

    @Transactional
    public void store(Place place,
                      List<String> socialNetworks,
                      List<String> phones,
                      String pathImage,
                      double lat, double lng) {

        place.setImage(pathImage);
        place.setLocation(geometryFactory.createPoint(new Coordinate(lat, lng)));

        List<SocialNetwork> savedNetworks = socialNetworks.stream()
                .filter(this::isNotBlank)
                .map(network -> socialNetworkRepository.save(new SocialNetwork(network, getSocialNetworkType(network))))
                .collect(Collectors.toList());

        place.addSocialNetworks(savedNetworks);


        List<PlacePhone> savedPhones = phones.stream()
                .map(placePhone -> placePhoneRepository.save(new PlacePhone(placePhone)))
                .collect(Collectors.toList());

        place.addPhones(savedPhones);

        Place finalPlace = placeRepository.save(place);
        savedNetworks.forEach(network -> network.setPlace(finalPlace));
        savedPhones.forEach(phone -> phone.setPlace(finalPlace));
    }

    private boolean isNotBlank(String network){
        return !network.isBlank();
    }

    private String getSocialNetworkType(String network) {
        return network.toLowerCase();
    }

}
