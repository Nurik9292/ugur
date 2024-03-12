package tm.ugur.models;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "places")
public class Place {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "image")
    private String image;

    @Column(name = "address")
    private String address;

    @Column(name = "website")
    private String website;

    @Column(name = "email")
    private String email;

    @Column(name = "location")
    private Point location;

    @OneToMany(mappedBy = "place")
    private List<PlacePhone> phones;

    @OneToMany(mappedBy = "place")
    private List<SocialNetwork> socialNetworks;

    @ManyToOne
    @JoinColumn(name = "place_category_id", referencedColumnName = "id")
    private PlaceCategory category;


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

    public List<PlacePhone> getPhones() {
        return phones;
    }

    public void setPhones(List<PlacePhone> phones) {
        this.phones = phones;
    }

    public PlaceCategory getCategory() {
        return category;
    }

    public void setCategory(PlaceCategory category) {
        this.category = category;
    }

    public List<SocialNetwork> getSocialNetworks() {
        return socialNetworks;
    }

    public void setSocialNetworks(List<SocialNetwork> socialNetworks) {
        this.socialNetworks = socialNetworks;
    }

    public void addSocialNetworks(List<SocialNetwork> networks){
        if(Objects.isNull(socialNetworks))
            setSocialNetworks(networks);
        socialNetworks.addAll(networks);
    }

    public void addPhones(List<PlacePhone> placePhones){
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
                '}';
    }


}
