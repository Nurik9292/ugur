package tm.ugur.dto;

import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.PlaceCategory;


import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlaceDTO extends AbstractDTO{

    private Map<String, String> titles;

    private Map<String, String> address;

    private List<PlaceImageDTO> images;

    private PlaceThumbDTO thumb;
    private String website;

    private String email;

    private PointDTO location;

    private List<PlacePhoneDTO> phones;

    private List<SocialNetworkDTO> socialNetworks;

    private PlaceCategoryDTO placeCategory;

    private  PlaceSubCategoryDTO placeSubCategory;

    private Boolean isFavorite;

    public PlaceDTO() {
    }


    public PlaceDTO(String website, String email) {
        this.website = website;
        this.email = email;
    }

    public PlaceDTO(String website,
                    String email,
                    PointDTO location,
                    List<PlacePhoneDTO> phones,
                    List<SocialNetworkDTO> socialNetworks,
                    PlaceCategoryDTO placeCategory) {
        this.website = website;
        this.email = email;
        this.location = location;
        this.phones = phones;
        this.socialNetworks = socialNetworks;
        this.placeCategory = placeCategory;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PointDTO getLocation() {
        return location;
    }

    public void setLocation(PointDTO location) {
        this.location = location;
    }

    public List<PlacePhoneDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PlacePhoneDTO> phones) {
        this.phones = phones;
    }

    public List<SocialNetworkDTO> getSocialNetworks() {
        return socialNetworks;
    }

    public void setSocialNetworks(List<SocialNetworkDTO> socialNetworks) {
        this.socialNetworks = socialNetworks;
    }

    public PlaceCategoryDTO getPlaceCategory() {
        return placeCategory;
    }

    public void setPlaceCategory(PlaceCategoryDTO placeCategory) {
        this.placeCategory = placeCategory;
    }

    public PlaceSubCategoryDTO getPlaceSubCategory() {
        return placeSubCategory;
    }

    public void setPlaceSubCategory(PlaceSubCategoryDTO placeSubCategory) {
        this.placeSubCategory = placeSubCategory;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public Map<String, String> getTitles() {
        return titles;
    }

    public void setTitles(Map<String, String> titles) {
        this.titles = titles;
    }

    public Map<String, String> getAddress() {
        return address;
    }

    public void setAddress(Map<String, String> address) {
        this.address = address;
    }

    public List<PlaceImageDTO> getImages() {
        return images;
    }

    public void setImages(List<PlaceImageDTO> images) {
        this.images = images;
    }

    public PlaceThumbDTO getThumb() {
        return thumb;
    }

    public void setThumb(PlaceThumbDTO thumb) {
        this.thumb = thumb;
    }
}
