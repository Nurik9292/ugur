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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
                      List<String> socialNetworks,
                      List<String> phones,
                      MultipartFile image) {

        String pathImage = storageService.store(image);

        if(!pathImage.isBlank())
            place.setImage(pathImage);

        place.setLocation(geometryFactory.createPoint(new Coordinate(place.getLat(), place.getLng())));

        AtomicInteger countNetwork = new AtomicInteger(0);
        List<SocialNetwork> savedNetworks = socialNetworks.stream()
                .filter(this::isNotBlank)
                .map(network -> socialNetworkRepository.save(
                        new SocialNetwork(network, getSocialNetworkType(countNetwork.getAndIncrement()))))
                .collect(Collectors.toList());

        place.addSocialNetworks(savedNetworks);


        AtomicInteger countPhone = new AtomicInteger(0);
        List<PlacePhone> savedPhones = phones.stream()
                .map(phone -> placePhoneRepository.save(new PlacePhone(phone, getPhoneType(countPhone.getAndIncrement()))))
                .collect(Collectors.toList());

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
                       List<String> socialNetworks,
                       List<String> phones,
                       MultipartFile image){

        String pathImage = storageService.store(image);

        if(!pathImage.isBlank()){
            storageService.delete(place.getImage());
            place.setImage(pathImage);
        }

        place.setLocation(geometryFactory.createPoint(new Coordinate(place.getLat(), place.getLng())));

        AtomicInteger countNetwork = new AtomicInteger(0);
        List<SocialNetwork> savedNetworks = socialNetworks.stream()
                .filter(this::isNotBlank)
                .map(network -> socialNetworkRepository.save(
                        new SocialNetwork(network, getSocialNetworkType(countNetwork.getAndIncrement()))))
                .collect(Collectors.toList());

        place.addSocialNetworks(savedNetworks);


        AtomicInteger countPhone = new AtomicInteger(0);
        List<PlacePhone> savedPhones = phones.stream()
                .map(phone -> placePhoneRepository.save(new PlacePhone(phone, getPhoneType(countPhone.getAndIncrement()))))
                .collect(Collectors.toList());

        place.addPhones(savedPhones);

        place.setId(id);
        place.setUpdatedAt(new Date());

        Place finalPlace = placeRepository.save(place);
        savedNetworks.forEach(network -> network.setPlace(finalPlace));
        savedPhones.forEach(phone -> phone.setPlace(finalPlace));
    }

    @Transactional
    public void delete(Long id){
         Optional<Place> place = findOne(id);

         String imagePath = place.get().getImage();

         if(!imagePath.isBlank())
             storageService.delete(imagePath);

        this.placeRepository.deleteById(id);
    }

    private boolean isNotBlank(String network){
        return !network.isBlank();
    }

    private String getSocialNetworkType(int count) {
        return count == 0 ? "instagram" : "tiktok";
    }

    private String getPhoneType(int count) {
        return count == 0 ? "city" : "mob";
    }

    private void setLatLng(Place place){
        Point point = place.getLocation();
        place.setLat(point.getX());
        place.setLng(point.getY());
    }
}
