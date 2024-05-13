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
import tm.ugur.dto.PlacePhoneDTO;
import tm.ugur.dto.TranslationDTO;
import tm.ugur.models.*;
import tm.ugur.repo.PlacePhoneRepository;
import tm.ugur.repo.PlaceRepository;
import tm.ugur.repo.SocialNetworkRepository;
import tm.ugur.services.redis.RedisPlacePhoneService;
import tm.ugur.services.redis.RedisPlaceTranslationService;
import tm.ugur.storage.FileSystemStorageService;
import tm.ugur.util.ImageDownload;
import tm.ugur.util.errors.places.PlaceNotFoundException;
import tm.ugur.util.mappers.PlacePhoneMapper;
import tm.ugur.util.mappers.TranslationPlaceMapper;
import tm.ugur.util.pagination.PaginationService;

import java.util.*;
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
    private final PlaceImageService placeImageService;
    private final PlaceTranslationService translationService;
    private final PlaceThumbService thumbService;
    private final ImageDownload imageDownload;
    private final RedisPlaceTranslationService redisTranslationService;
    private final TranslationPlaceMapper translationPlaceMapper;
    private final RedisPlacePhoneService placePhoneService;
    private final PlacePhoneMapper placePhoneMapper;

    @Autowired
    public PlaceService(PlaceRepository placeRepository,
                        PlacePhoneRepository placePhoneRepository,
                        SocialNetworkRepository socialNetworkRepository,
                        PaginationService paginationService,
                        GeometryFactory geometryFactory,
                        FileSystemStorageService storageService,
                        PlaceImageService placeImageService,
                        PlaceTranslationService translationService,
                        PlaceThumbService thumbService,
                        ImageDownload imageDownload,
                        RedisPlaceTranslationService redisTranslationService,
                        TranslationPlaceMapper translationPlaceMapper,
                        RedisPlacePhoneService placePhoneService,
                        PlacePhoneMapper placePhoneMapper) {
        this.placeRepository = placeRepository;
        this.placePhoneRepository = placePhoneRepository;
        this.socialNetworkRepository = socialNetworkRepository;
        this.paginationService = paginationService;
        this.geometryFactory = geometryFactory;
        this.storageService = storageService;
        this.placeImageService = placeImageService;
        this.translationService = translationService;
        this.thumbService = thumbService;
        this.imageDownload = imageDownload;
        this.redisTranslationService = redisTranslationService;
        this.translationPlaceMapper = translationPlaceMapper;
        this.placePhoneService = placePhoneService;
        this.placePhoneMapper = placePhoneMapper;
    }

    public List<Place> findAll(){
        return placeRepository.findAll();
    }

    public Page<Place> findAll(int pageNumber, int itemsPerPage)
    {
        return paginationService.createPage(placeRepository.findAll(), pageNumber, itemsPerPage);
    }

    public Place findOne(long id){
        return placeRepository.findById(id).map(place -> {
            setLatLng(place);
            return place;
        }).orElseThrow(PlaceNotFoundException::new);
    }


    public Page<Place> getPlacePages(String page, String items, String sortBy){
        int pageNumber = page == null ? 1 : Integer.parseInt(page);
        int itemsPerPage = items == null ? 10 : Integer.parseInt(items);

        List<Place> places = sortBy.isBlank() ? placeRepository.findAll() :
                sortBy.equals("title") ? placeSortedTitle() : placeSortedAddress();

        return this.paginationService.createPage(places, pageNumber, itemsPerPage);
    }

    private List<Place> placeSortedTitle(){
        return placeRepository.findAll().stream()
                .sorted(Comparator.comparing(place -> {
                    PlaceTranslation russianTranslation = place.getTranslations().stream()
                            .filter(translation -> translation.getLocale().equals("ru"))
                            .findFirst().orElse(null);

                    return russianTranslation != null ? russianTranslation.getTitle() : "";
                })).toList();
    }

    private List<Place> placeSortedAddress(){
        return placeRepository.findAll().stream()
                .sorted(Comparator.comparing(place -> {
                    PlaceTranslation russianTranslation = place.getTranslations().stream()
                            .filter(translation -> translation.getLocale().equals("ru"))
                            .findFirst().orElse(null);

                    return russianTranslation != null ? russianTranslation.getAddress() : "";
                })).toList();
    }

    @Transactional
    public void store(Place place,
                      String instagram,
                      String tiktok,
                      MultipartFile[] images,
                      MultipartFile prev) {
        ;

        List<PlaceImage> savedImages = Objects.nonNull(images) ? storeImages(images) : Collections.emptyList();;
        place.setImages(savedImages);

        PlaceThumb savedThumb =  storeThumb(prev);
        place.setThumbs(savedThumb);

        place.setLocation(geometryFactory.createPoint(new Coordinate(place.getLat(), place.getLng())));

//        Set<SocialNetwork> savedNetworks = saveSocialNetworks(instagram, tiktok);
//        place.addSocialNetworks(savedNetworks);
//
        Set<PlacePhone> savedPhones = getPlacePhones();
        place.addPhones(savedPhones);
        System.out.println(savedPhones);

        Set<PlaceTranslation> savedTranslations = getPlaceTranslations();
        place.setTranslations(savedTranslations);

        place.setCreatedAt(new Date());
        place.setUpdatedAt(new Date());

        Place finalPlace = placeRepository.save(place);
//        savedNetworks.forEach(network -> network.setPlace(finalPlace));
        savedPhones.forEach(phone -> phone.setPlace(finalPlace));
        if(!savedImages.isEmpty())
            savedImages.forEach(image -> image.setPlace(finalPlace));
        savedTranslations.forEach(translation -> translation.setPlace(finalPlace));
        if (Objects.nonNull(savedThumb))
            savedThumb.setPlace(finalPlace);
    }

    @Transactional
    public void store(PlaceCategory placeCategory,
                       PlaceSubCategory placeSubCategory,
                       String title_tm,
                       String title_ru,
                       String address_tm,
                       String address_ru,
                       String image,
                       double latitude,
                       double longitude){

        Place place = new Place();
        place.setPlaceSubCategory(placeSubCategory);
        place.setPlaceCategory(placeCategory);
        place.setLocation(geometryFactory.createPoint(new Coordinate(latitude, longitude)));

        Set<PlaceTranslation>  translations = Set.of(
                translationService.store(new PlaceTranslation("tm", title_tm, address_tm)),
                translationService.store(new PlaceTranslation("ru", title_ru, address_ru)));
        place.setTranslations(translations);

        byte[] byteImages = imageDownload.getImageByte(image);

        PlaceImage placeImage = placeImageService.store(new PlaceImage(storageService.store(byteImages,
                        "place", 360, 620)));
        place.setImages(new ArrayList<>(List.of(placeImage)));
        PlaceThumb placeThumb = thumbService.store(new PlaceThumb(storageService.store(byteImages,
                        "place/thumb", 64, 64)));
        place.setThumbs(placeThumb);


        place.setCreatedAt(new Date());
        place.setUpdatedAt(new Date());

        Place newPlace = placeRepository.save(place);
        translations.forEach(translation -> translation.setPlace(newPlace));
        placeImage.setPlace(newPlace);
        placeThumb.setPlace(newPlace);
    }

    @Transactional
    public void update(Long id, Place place,
                       String instagram,
                       String tiktok,
                       List<String> phones,
                       String cityPhone,
                       MultipartFile[] images,
                       MultipartFile prev,
                       Map<String, String> titles,
                       Map<String, String> address,
                       Long[] removeImageIds){


        Place existingPlace = findOne(id);

        if(Objects.nonNull(removeImageIds) && removeImageIds.length > 0)
            deleteImageIds(existingPlace, removeImageIds);

        List<PlaceImage> savedImages = Objects.nonNull(images) ? storeImages(images) : Collections.emptyList();
        place.setImages(savedImages);


        PlaceThumb savedThumb = Objects.nonNull(prev) ? storeThumb(prev, existingPlace) : null;
        place.setThumbs(savedThumb);

        place.setLocation(geometryFactory.createPoint(new Coordinate(place.getLat(), place.getLng())));

        socialNetworkRepository.deleteAll(existingPlace.getSocialNetworks());
        Set<SocialNetwork> savedNetworks = saveSocialNetworks(instagram, tiktok);
        place.addSocialNetworks(savedNetworks);

        placePhoneRepository.deleteAll(existingPlace.getPhones());

        Set<PlacePhone> savedPhones = getPlacePhones();
        place.addPhones(savedPhones);

        Set<PlaceTranslation> savedTranslations = updatePlaceTranslations(existingPlace.getTranslations(), titles, address);
        place.setTranslations(savedTranslations);


        place.setId(id);
        place.setCreatedAt(existingPlace.getCreatedAt());
        place.setUpdatedAt(new Date());
        Place finalPlace = placeRepository.save(place);
        savedNetworks.forEach(network -> network.setPlace(finalPlace));
        savedPhones.forEach(phone -> phone.setPlace(finalPlace));
        if(!savedImages.isEmpty())
            savedImages.forEach(image -> image.setPlace(finalPlace));
        savedTranslations.forEach(translation -> translation.setPlace(finalPlace));
        if (Objects.nonNull(savedThumb))
            savedThumb.setPlace(finalPlace);
    }

    @Transactional
    public void delete(Long id){
        Optional<Place> placeOptional = placeRepository.findById(id);

        placeOptional.ifPresent(place -> {
          place.getImages().forEach(placeImageService::delete);
          thumbService.delete(place.getThumbs());
        });

        this.placeRepository.deleteById(id);
    }

    private List<PlaceImage> storeImages(MultipartFile[] images) {
        List<PlaceImage> placeImages = new ArrayList<>();
        for (MultipartFile image : images) {
            String pathImage = storageService.store(image, "place", 360, 620);
            if (!pathImage.isBlank()) {
                placeImages.add(placeImageService.store(new PlaceImage(pathImage)));
            }
        }
        return placeImages;
    }

    private PlaceThumb storeThumb(MultipartFile image){

        String thumbPath = storageService.store(image,  "place/thumb",64, 64);
        return thumbPath.isBlank() ? null
                : thumbService.store(new PlaceThumb(thumbPath));
    }

    private PlaceThumb storeThumb(MultipartFile image, Place existingPlace){
        if(Objects.nonNull(existingPlace.getThumbs()))
            thumbService.delete(existingPlace.getThumbs());

        String thumbPath = storageService.store(image,  "place/thumb",64, 64);
        return thumbPath.isBlank() ? null
                : thumbService.store(new PlaceThumb(thumbPath));
    }

    private Set<SocialNetwork> saveSocialNetworks(String instagram, String tiktok) {
        Set<SocialNetwork> savedNetworks = new HashSet<>();
        if(!instagram.isBlank())
            savedNetworks.add(socialNetworkRepository.save(new SocialNetwork(instagram, "instagram")));
        if(!tiktok.isBlank())
            savedNetworks.add(socialNetworkRepository.save(new SocialNetwork(tiktok, "tiktok")));
        return savedNetworks;
    }

    private Set<PlacePhone> getPlacePhones(){
        List<PlacePhoneDTO> phones = placePhoneService.getAll();

        Set<PlacePhone> savedPhones = Objects.nonNull(phones) ? phones.stream()
                .map(phone -> placePhoneRepository.save(convertDtoToEntity(phone)))
                .collect(Collectors.toSet()) : new HashSet<>();

        return savedPhones;
    }

    private Set<PlaceTranslation> getPlaceTranslations(){
        Set<PlaceTranslation> translations = new HashSet<>();
        System.out.println(redisTranslationService.getTm());
        translations.add(translationService.store(convertDtoToEntity(redisTranslationService.getTm())));
        translations.add(translationService.store(convertDtoToEntity(redisTranslationService.getRu())));
        translations.add(translationService.store(convertDtoToEntity(redisTranslationService.getEn())));
        return translations;
    }

    private Set<PlaceTranslation> updatePlaceTranslations(Set<PlaceTranslation> existTranslation,
                                                          Map<String, String> titles, Map<String, String> address){
        Map<String, PlaceTranslation> existingTranslations = existTranslation.stream()
                .collect(Collectors.toMap(PlaceTranslation::getLocale, translation -> translation));


        return updateTranslations(existingTranslations, titles, address);
    }

    private Set<PlaceTranslation> updateTranslations(Map<String, PlaceTranslation> existingTranslations,
                                         Map<String, String> titles, Map<String, String> address) {
        Set<PlaceTranslation> translations = new HashSet<>();
        for (String locale : Arrays.asList("tm", "ru", "en")) {
            String title = titles.get(locale);
            String addressText = address.get(locale);
            PlaceTranslation translation = existingTranslations.get(locale);
            if (title != null) {
                if (translation != null) {
                    translation.setTitle(title);
                    translation.setAddress(addressText);
                    translations.add(translationService.update(translation));
                } else {
                    translations.add(translationService.store(new PlaceTranslation(locale, title, addressText)));
                }
            }
        }

        return translations;
    }

    private void deleteImageIds(Place existingPlace, Long[] removeImageIds){
        List<PlaceImage> placeImages = existingPlace.getImages();

        for(int i = 0; i < placeImages.size(); i++){
            for(int j = 0; j < removeImageIds.length; j++){
                if(placeImages.get(i).getId() == removeImageIds[j]){
                    placeImageService.delete(placeImages.get(i));
                    placeImages.remove(i);
                }
            }
        }
        existingPlace.setImages(placeImages);
    }

    private void setLatLng(Place place){
        Point point = place.getLocation();
        place.setLat(point.getX());
        place.setLng(point.getY());
    }

    public String pruningPath(String path){
        return path.substring(path.lastIndexOf("/") + 1);
    }

    private PlaceTranslation convertDtoToEntity(TranslationDTO translation) {
        return translationPlaceMapper.toEntity(translation);
    }

    private PlacePhone convertDtoToEntity(PlacePhoneDTO placePhone) {
        return placePhoneMapper.toEntity(placePhone);
    }
}
