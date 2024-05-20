package tm.ugur.models.place;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.locationtech.jts.geom.Point;
import tm.ugur.models.AbstractEntity;
import tm.ugur.models.Client;
import tm.ugur.models.SocialNetwork;
import tm.ugur.models.place.category.PlaceCategory;
import tm.ugur.models.place.subCategory.PlaceSubCategory;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "places")
public class Place extends AbstractEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PlaceTranslation> translations;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<PlaceImage> images;

    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL)
    private PlaceThumb thumbs;

    @ManyToOne
    @JoinColumn(name = "place_category_id", referencedColumnName = "id")
    private PlaceCategory placeCategory;

    @ManyToOne
    @JoinColumn(name = "place_sub_category_id",referencedColumnName = "id")
    private PlaceSubCategory placeSubCategory;

    @ManyToMany
    @JoinTable(name = "place_favorites",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private List<Client> clients;

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

    public PlaceCategory getPlaceCategory() {
        return placeCategory;
    }

    public void setPlaceCategory(PlaceCategory placeCategory) {
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

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public Set<PlaceTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(Set<PlaceTranslation> translations) {
        this.translations = translations;
    }

    public List<PlaceImage> getImages() {
        return images;
    }

    public void setImages(List<PlaceImage> images) {
        this.images = images;
    }

    public void addImages(List<PlaceImage> images) {
        this.images.addAll(images);
    }

    public PlaceThumb getThumbs() {
        return thumbs;
    }

    public void setThumbs(PlaceThumb thumbs) {
        this.thumbs = thumbs;
    }

    public void addTranslations(Set<PlaceTranslation> translations) {
        if(Objects.nonNull(translations) && !translations.isEmpty())
            this.translations.addAll(translations);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Place place = (Place) object;
        return id == place.id
                && Objects.equals(website, place.website)
                && Objects.equals(email, place.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, website, email);
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", website='" + website + '\'' +
                ", email='" + email + '\'' +
                ", location=" + location +
                ", placeCategory=" + placeCategory +
                '}';
    }

}
