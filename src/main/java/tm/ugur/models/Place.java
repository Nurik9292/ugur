package tm.ugur.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.locationtech.jts.geom.Point;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "places")
public class Place extends AbstractEntity{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Заполните поле.")
    @Column(name = "title")
    private String title;



    @Column(name = "image")
    private String image;

    @NotEmpty(message = "Заполните поле.")
    @Column(name = "address")
    private String address;

    @Column(name = "website")
    private String website;

    @Email(message = "Пожалуйста, введите корректный адрес электронной почты.")
    @Column(name = "email")
    private String email;

    @Column(name = "location")
    private Point location;

    @OneToMany(mappedBy = "place")
    private Set<PlacePhone> phones;

    @OneToMany(mappedBy = "place")
    private Set<SocialNetwork> socialNetworks;

    @ManyToOne
    @JoinColumn(name = "place_category_id", referencedColumnName = "id")
    private PlaceCategory placeCategory;

    @ManyToOne
    @JoinColumn(name = "place_sub_category_id",referencedColumnName = "id")
    private PlaceSubCategory placeSubCategory;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


    @Transient
    private Double lat;


    @Transient
    private Double lng;

    public Place(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PlaceCategory getPlaceCategory() {
        return placeCategory;
    }

    public void setPlaceCategory(PlaceCategory placeCategory) {
        this.placeCategory = placeCategory;
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

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Set<PlacePhone> getPhones() {
        return phones;
    }

    public void setPhones(Set<PlacePhone> phones) {
        this.phones = phones;
    }

    public Set<SocialNetwork> getSocialNetworks() {
        return socialNetworks;
    }

    public void setSocialNetworks(Set<SocialNetwork> socialNetworks) {
        this.socialNetworks = socialNetworks;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }


    public PlaceSubCategory getPlaceSubCategory() {
        return placeSubCategory;
    }

    public void setPlaceSubCategory(PlaceSubCategory placeSubCategory) {
        this.placeSubCategory = placeSubCategory;
    }

    public void addSocialNetworks(Set<SocialNetwork> networks){
        if(Objects.isNull(socialNetworks))
            setSocialNetworks(networks);
        socialNetworks.addAll(networks);
    }

    public void addPhones(Set<PlacePhone> placePhones){
        if(Objects.isNull(phones))
            setPhones(placePhones);
        phones.addAll(placePhones);
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Place place = (Place) object;
        return id == place.id
                && Objects.equals(title, place.title)
                && Objects.equals(image, place.image)
                && Objects.equals(address, place.address)
                && Objects.equals(website, place.website)
                && Objects.equals(email, place.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, image, address, website, email);
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", address='" + address + '\'' +
                ", website='" + website + '\'' +
                ", email='" + email + '\'' +
                ", location=" + location +
                ", placeCategory=" + placeCategory +
                '}';
    }



}
