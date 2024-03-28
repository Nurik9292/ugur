package tm.ugur.services.parser;


import java.util.List;


public class PlaceUgur {
    private String name_tm;
    private String name_ru;
    private String image;
    private String address_tm;
    private String address_ru;
    private double latitude;
    private double longitude;
    private List<Category> categories;

    public PlaceUgur(String name_tm,
                     String name_ru,
                     String image,
                     String address_tm,
                     String address_ru,
                     double latitude,
                     double longitude,
                     List<Category> categories) {
        this.name_tm = name_tm;
        this.name_ru = name_ru;
        this.image = image;
        this.address_tm = address_tm;
        this.address_ru = address_ru;
        this.latitude = latitude;
        this.longitude = longitude;
        this.categories = categories;
    }

    public String getName_tm() {
        return name_tm;
    }

    public void setName_tm(String name_tm) {
        this.name_tm = name_tm;
    }

    public String getName_ru() {
        return name_ru;
    }

    public void setName_ru(String name_ru) {
        this.name_ru = name_ru;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress_tm() {
        return address_tm;
    }

    public void setAddress_tm(String address_tm) {
        this.address_tm = address_tm;
    }

    public String getAddress_ru() {
        return address_ru;
    }

    public void setAddress_ru(String address_ru) {
        this.address_ru = address_ru;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Place{" +
                ", name_tm='" + name_tm + '\'' +
                ", name_ru='" + name_ru + '\'' +
                ", image='" + image + '\'' +
                ", address_tm='" + address_tm + '\'' +
                ", address_ru='" + address_ru + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", categories=" + categories +
                '}';
    }
}
