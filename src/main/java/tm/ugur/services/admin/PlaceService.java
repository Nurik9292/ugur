package tm.ugur.services.admin;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tm.ugur.models.*;
import tm.ugur.models.place.*;
import tm.ugur.models.place.category.PlaceCategory;
import tm.ugur.models.place.subCategory.PlaceSubCategory;
import tm.ugur.repo.PlaceRepository;
import tm.ugur.request.PlaceRequest;
import tm.ugur.storage.FileSystemStorageService;
import tm.ugur.util.ImageDownload;
import tm.ugur.util.errors.places.PlaceNotFoundException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlacePhoneService placePhoneService;
    private final PlaceSocialNetworkService socialNetworkService;
    private final PlaceCategoryService placeCategoryService;
    private final PlaceSubCategoryService placeSubCategoryService;
    private final GeometryFactory geometryFactory;
    private final FileSystemStorageService storageService;
    private final PlaceImageService placeImageService;
    private final PlaceTranslationService translationService;
    private final PlaceThumbService thumbService;
    private final ImageDownload imageDownload;


    @Autowired
    public PlaceService(PlaceRepository placeRepository,
                        PlacePhoneService placePhoneService,
                        PlaceSocialNetworkService socialNetworkService,
                        PlaceCategoryService placeCategoryService,
                        PlaceSubCategoryService placeSubCategoryService,
                        GeometryFactory geometryFactory,
                        FileSystemStorageService storageService,
                        PlaceImageService placeImageService,
                        PlaceTranslationService translationService,
                        PlaceThumbService thumbService,
                        ImageDownload imageDownload) {
        this.placeRepository = placeRepository;
        this.placePhoneService = placePhoneService;
        this.socialNetworkService = socialNetworkService;
        this.placeCategoryService = placeCategoryService;
        this.placeSubCategoryService = placeSubCategoryService;
        this.geometryFactory = geometryFactory;
        this.storageService = storageService;
        this.placeImageService = placeImageService;
        this.translationService = translationService;
        this.thumbService = thumbService;
        this.imageDownload = imageDownload;
    }

    public List<Place> search(String search, String locale) {
        List<Place> places = placeRepository.findAll();

        if (Objects.nonNull(search) && Objects.nonNull(locale)) {
            places = places.stream()
                    .filter(place -> place.getTranslations().stream()
                            .anyMatch(translation -> translation.getLocale().equals(locale) &&
                                    translation.getTitle().toLowerCase().contains(search.toLowerCase())))
                    .collect(Collectors.toList());
        }

        return places;
    }

    public List<Place> findAll(long categoryId){
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        List<Place> places = placeRepository.findAll(sort);

        return  categoryId == 0 ? places : places.stream()
                .filter(place -> place.getPlaceCategory().getId() == categoryId)
                .collect(Collectors.toList());
    }


    public Place findOne(long id){
        return placeRepository.findById(id).map(place -> {
            setLatLng(place);
            return place;
        }).orElseThrow(PlaceNotFoundException::new);
    }

    @Transactional
    public void store(PlaceRequest request) {
        Place place = new Place();
        PlaceCategory category = placeCategoryService.findOne(request.getCategoryId());
        PlaceSubCategory subCategory = placeSubCategoryService.findOne(request.getSubCategoryId());

        System.out.println(request.getFiles());
        List<PlaceImage> savedImages = Objects.nonNull(request.getFiles()) ? storeImages(request.getFiles()) : Collections.emptyList();;
        PlaceThumb savedThumb =  storeThumb(request.getThumb());
        System.out.println(savedThumb);

        Set<PlaceTranslation> translations = new HashSet<>();
        translations.add(translationService.store(new PlaceTranslation(request.getTitleTm(), request.getAddressTm(), "tm")));
        translations.add(translationService.store(new PlaceTranslation(request.getTitleRu(), request.getAddressRu(), "ru")));
        translations.add(translationService.store(new PlaceTranslation(request.getTitleEn(), request.getAddressEn(), "en")));

        Set<SocialNetwork> socialNetworks = getSocialNetworks(request.getInstagram(), request.getTiktok());
        Set<PlacePhone> phones = getPlacePhones(request.getCityNumber(), request.getMobNumbers());

        Point location = geometryFactory.createPoint(new Coordinate(request.getLat(), request.getLng()));

        place.setLocation(location);
        place.setEmail(request.getEmail());
        place.setWebsite(request.getWebsite());
        place.setPlaceCategory(category);
        place.setPlaceSubCategory(subCategory);
        place.setTranslations(translations);
        place.setSocialNetworks(socialNetworks);
        place.setPhones(phones);
        place.setImages(savedImages);
        place.setThumbs(savedThumb);
        place.setCreatedAt(new Date());
        place.setUpdatedAt(new Date());

        Place resultPlace = placeRepository.save(place);
        socialNetworks.forEach(network -> network.setPlace(resultPlace));
        phones.forEach(phone -> phone.setPlace(resultPlace));
        if(!savedImages.isEmpty())
            savedImages.forEach(image -> image.setPlace(resultPlace));
        translations.forEach(translation -> translation.setPlace(resultPlace));
        if (Objects.nonNull(savedThumb))
            savedThumb.setPlace(resultPlace);
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
    public void update(long id, PlaceRequest request){

        Place existingPlace = findOne(id);

        if(Objects.nonNull(request.getRemoveImageIds()) && !request.getRemoveImageIds().isEmpty())
            deleteImageIds(existingPlace, request.getRemoveImageIds());

        List<PlaceImage> savedImages = Objects.nonNull(request.getFiles()) ? storeImages(request.getFiles()) : Collections.emptyList();
        existingPlace.addImages(savedImages);

        PlaceThumb savedThumb = null;
        if(Objects.nonNull(request.getThumb())) {
            savedThumb = storeThumb(request.getThumb(), existingPlace);
            existingPlace.setThumbs(savedThumb);
        }

        existingPlace.setLocation(geometryFactory.createPoint(new Coordinate(request.getLat(), request.getLng())));

        socialNetworkService.deleteAll(existingPlace.getSocialNetworks());
        Set<SocialNetwork> savedNetworks = getSocialNetworks(request.getInstagram(), request.getTiktok());
        existingPlace.addSocialNetworks(savedNetworks);

        placePhoneService.deleteAll(existingPlace.getPhones());
        Set<PlacePhone> savedPhones = getPlacePhones(request.getCityNumber(), request.getMobNumbers());
        existingPlace.addPhones(savedPhones);

        Set<PlaceTranslation> savedTranslations = getUpdateTranslation(request, existingPlace.getTranslations());
        if(!savedTranslations.isEmpty())
            existingPlace.addTranslations(savedTranslations);

        existingPlace.setUpdatedAt(new Date());
        Place finalPlace = placeRepository.save(existingPlace);
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
          if(Objects.nonNull(place.getThumbs()))
            thumbService.delete(place.getThumbs());
        });

        this.placeRepository.deleteById(id);
    }

    private List<PlaceImage> storeImages(List<MultipartFile> images) {
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

    private Set<SocialNetwork> getSocialNetworks(String instagram, String tiktok) {
        Set<SocialNetwork> savedNetworks = new HashSet<>();
        if(Objects.nonNull(instagram) && !instagram.isBlank())
            savedNetworks.add(socialNetworkService.store(new SocialNetwork(instagram, "instagram")));
        if(Objects.nonNull(tiktok) && !tiktok.isBlank())
            savedNetworks.add(socialNetworkService.store(new SocialNetwork(tiktok, "tiktok")));
        return savedNetworks;
    }

    private Set<PlacePhone> getPlacePhones(String cityNumber, List<String> mobNumbers){
        Set<PlacePhone> phones = new HashSet<>();

        if(Objects.nonNull(mobNumbers) && !mobNumbers.isEmpty()) {
            mobNumbers.forEach(mobNumber -> {
                phones.add(placePhoneService.store(new PlacePhone(mobNumber, "mob")));
            });
        }

        if(Objects.nonNull(cityNumber) && !cityNumber.isBlank())
            phones.add(placePhoneService.store(new PlacePhone(cityNumber, "city")));

        return phones;
    }

    private Set<PlaceTranslation> getUpdateTranslation(PlaceRequest request, Set<PlaceTranslation> translations) {
//        translations.forEach(translation -> {
//            if(translation.getLocale().equals("tm")) {
//                translation.setTitle(request.getTitleTm());
//                translation.setAddress(request.getAddressTm());
//            }
//            if(translation.getLocale().equals("ru")) {
//                translation.setTitle(request.getTitleRu());
//                translation.setAddress(request.getAddressRu());
//            }
//            if(translation.getLocale().equals("en")) {
//                translation.setTitle(request.getTitleEn());
//                translation.setAddress(request.getAddressEn());
//            }
//        });
//
//
//        Set<String> translationLocales = translations.stream().map(PlaceTranslation::getLocale).collect(Collectors.toSet());
//        if(!translationLocales.contains("en"))
//            translations.add(translationService.store(new PlaceTranslation(request.getTitleEn(), request.getAddressEn(), "en")));
//        if(!translationLocales.contains("ru"))
//            translations.add(translationService.store(new PlaceTranslation(request.getTitleRu(), request.getAddressRu(), "ru")));
//        if(!translationLocales.contains("tm"))
//            translations.add(translationService.store(new PlaceTranslation(request.getTitleTm(), request.getAddressTm(), "tm")));
//
//        return translations;

        Map<String, PlaceTranslation> translationMap = translations.stream()
                .collect(Collectors.toMap(PlaceTranslation::getLocale, Function.identity()));

        translationMap.computeIfPresent("tm", (locale, translation) -> {
            translation.setTitle(request.getTitleTm());
            translation.setAddress(request.getAddressTm());
            return translation;
        });

        translationMap.computeIfPresent("ru", (locale, translation) -> {
            translation.setTitle(request.getTitleRu());
            translation.setAddress(request.getAddressRu());
            return translation;
        });

        translationMap.computeIfPresent("en", (locale, translation) -> {
            translation.setTitle(request.getTitleEn());
            translation.setAddress(request.getAddressEn());
            return translation;
        });

        Set<PlaceTranslation> newTranslations = new HashSet<>();
        if (!translationMap.containsKey("tm"))
            newTranslations.add(translationService.store(new PlaceTranslation(request.getTitleTm(), request.getAddressTm(), "tm")));

        if (!translationMap.containsKey("ru"))
            newTranslations.add(translationService.store(new PlaceTranslation(request.getTitleRu(), request.getAddressRu(), "ru")));

        if (!translationMap.containsKey("en"))
            newTranslations.add(translationService.store(new PlaceTranslation(request.getTitleEn(), request.getAddressEn(), "en")));

        return newTranslations;
    }


    private void deleteImageIds(Place existingPlace, List<Long> removeImageIds){
        List<PlaceImage> placeImages = existingPlace.getImages();

        for(int i = 0; i < placeImages.size(); i++){
            if(removeImageIds.contains(placeImages.get(i).getId()))
                placeImageService.delete(placeImages.remove(i));
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

}
