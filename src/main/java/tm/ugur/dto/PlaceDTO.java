package tm.ugur.dto;

import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.PlaceCategory;


import java.util.List;
import java.util.Objects;

public class PlaceDTO extends AbstractDTO{

    private String title;

    private String image;

    private String address;

    private String website;

    private String email;

    private PointDTO location;

    private List<PlacePhoneDTO> phones;

    private List<SocialNetworkDTO> socialNetworks;

    private PlaceCategoryDTO placeCategory;

    private  PlaceSubCategoryDTO placeSubCategory;


    public PlaceDTO() {
    }


    public PlaceDTO(String title, String image, String address, String website, String email) {
        this.title = title;
        this.image = image;
        this.address = address;
        this.website = website;
        this.email = email;
    }

    public PlaceDTO(String title, String image, String address, String website, String email, PointDTO location, List<PlacePhoneDTO> phones, List<SocialNetworkDTO> socialNetworks, PlaceCategoryDTO placeCategory) {
        this.title = title;
        this.image = image;
        this.address = address;
        this.website = website;
        this.email = email;
        this.location = location;
        this.phones = phones;
        this.socialNetworks = socialNetworks;
        this.placeCategory = placeCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlaceDTO placeDTO = (PlaceDTO) object;
        return Objects.equals(title, placeDTO.title) && Objects.equals(image, placeDTO.image) && Objects.equals(address, placeDTO.address) && Objects.equals(website, placeDTO.website) && Objects.equals(email, placeDTO.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, image, address, website, email);
    }

    @Override
    public String toString() {
        return "PlaceDTO{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", address='" + address + '\'' +
                ", website='" + website + '\'' +
                ", email='" + email + '\'' +
                ", location=" + location +
                ", phones=" + phones +
                ", socialNetworks=" + socialNetworks +
                ", placeCategory=" + placeCategory +
                ", id=" + id +
                '}';
    }


}
