package tm.ugur.services.admin;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tm.ugur.models.*;
import tm.ugur.repo.PlacePhoneRepository;
import tm.ugur.repo.PlaceRepository;
import tm.ugur.repo.SocialNetworkRepository;
import tm.ugur.storage.FileSystemStorageService;
import tm.ugur.util.pagination.PaginationService;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlacePhoneRepository placePhoneRepository;
    private final SocialNetworkRepository socialNetworkRepository;
    private final PaginationService paginationService;
    private final GeometryFactory geometryFactory;
    private final FileSystemStorageService storageService;

    @Autowired
    public PlaceService(PlaceRepository placeRepository,
                        PlacePhoneRepository placePhoneRepository,
                        SocialNetworkRepository socialNetworkRepository,
                        PaginationService paginationService,
                        GeometryFactory geometryFactory,
                        FileSystemStorageService storageService) {
        this.placeRepository = placeRepository;
        this.placePhoneRepository = placePhoneRepository;
        this.socialNetworkRepository = socialNetworkRepository;
        this.paginationService = paginationService;
        this.geometryFactory = geometryFactory;
        this.storageService = storageService;
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
                      String instagram,
                      String tiktok,
                      List<String> phones,
                      String cityPhone,
                      MultipartFile image) {

        String pathImage = storageService.store(image);

        if(!pathImage.isBlank())
            place.setImage(pathImage);

        place.setLocation(geometryFactory.createPoint(new Coordinate(place.getLat(), place.getLng())));


        Set<SocialNetwork> savedNetworks = new HashSet<>();
        if(!instagram.isBlank())
            savedNetworks.add(new SocialNetwork(instagram, "instagram"));
        if(!tiktok.isBlank())
            savedNetworks.add(new SocialNetwork(instagram, "tiktok"));
        place.addSocialNetworks(savedNetworks);

        Set<PlacePhone> savedPhones = phones.stream()
                .map(phone -> placePhoneRepository.save(new PlacePhone(phone, "mob")))
                .collect(Collectors.toSet());
        savedPhones.add(new PlacePhone(cityPhone, "city"));
        place.addPhones(savedPhones);

        place.setCreatedAt(new Date());
        place.setUpdatedAt(new Date());

        Place finalPlace = placeRepository.save(place);
        savedNetworks.forEach(network -> network.setPlace(finalPlace));
        savedPhones.forEach(phone -> phone.setPlace(finalPlace));
    }

    public Optional<Place> findOne(long id){
        Optional<Place> place = placeRepository.findById(id);
        setLatLng(place.orElse(new Place()));
        return place;
    }

    @Transactional
    public void update(Long id, Place place,
                       String instagram,
                       String tiktok,
                       List<String> phones,
                       String cityPhone,
                       MultipartFile image){

        String pathImage = storageService.store(image);

        Place existingPlace = findOne(id).orElseThrow();


        if(!pathImage.isBlank()){
            storageService.delete(pruningPath(existingPlace.getImage()));
            place.setImage(pathImage);
        }

        place.setLocation(geometryFactory.createPoint(new Coordinate(place.getLat(), place.getLng())));

        place.setUpdatedAt(new Date());

        socialNetworkRepository.deleteAll(existingPlace.getSocialNetworks());
        Set<SocialNetwork> savedNetworks = new HashSet<>();
        if(!instagram.isBlank())
            savedNetworks.add(new SocialNetwork(instagram, "instagram"));
        if(!tiktok.isBlank())
            savedNetworks.add(new SocialNetwork(instagram, "tiktok"));
        place.addSocialNetworks(savedNetworks);

        placePhoneRepository.deleteAll(existingPlace.getPhones());
        Set<PlacePhone> savedPhones = phones.stream()
                .map(phone -> placePhoneRepository.save(new PlacePhone(phone, "mob")))
                .collect(Collectors.toSet());
        savedPhones.add(new PlacePhone(cityPhone, "city"));

        place.addPhones(savedPhones);
        place.setId(id);
        Place finalPlace = placeRepository.save(place);
        savedNetworks.forEach(network -> network.setPlace(finalPlace));
        savedPhones.forEach(phone -> phone.setPlace(finalPlace));

    }

    @Transactional
    public void delete(Long id){
        Optional<Place> placeOptional = placeRepository.findById(id);

        placeOptional.ifPresent(place -> {
            String imagePath = place.getImage();
            if (imagePath != null && !imagePath.isBlank()) {
                storageService.delete(pruningPath(imagePath));
            }
        });

        this.placeRepository.deleteById(id);
    }


    private void setLatLng(Place place){
        Point point = place.getLocation();
        place.setLat(point.getX());
        place.setLng(point.getY());
    }

    private String pruningPath(String path){
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
