package tm.ugur.request;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;
import tm.ugur.util.validator.annotation.NonZero;
import tm.ugur.util.validator.annotation.ValidPhoneNumber;
import tm.ugur.util.validator.annotation.ValidPhoneSize;

import java.util.List;

public class PlaceRequest {

    @NotBlank(message = "Заголовк Tm не должен быть пустым.")
    @Size(min = 3, max = 100, message = "Заголовок Tm не должен быть меньше 3 символов и не больше 100 сиволов")
    private String titleTm;
    @NotBlank(message = "Заголовк Ru не должен быть пустым.")
    @Size(min = 3, max = 100, message = "Заголовок Ru не должен быть меньше 3 символов и не больше 100 сиволов")
    private String titleRu;
    @NotBlank(message = "Заголовк En не должен быть пустым.")
    @Size(min = 3, max = 100, message = "Заголовок En не должен быть меньше 3 символов и не больше 100 сиволов")
    private String titleEn;
    @NotBlank(message = "Адрес Tm не должен быть пустым.")
    @Size(min = 3, max = 100, message = "Адрес Tm не должен быть меньше 3 символов и не больше 100 сиволов")
    private String addressTm;
    @Size(min = 3, max = 100, message = "Адрес Ru не должен быть меньше 3 символов и не больше 100 сиволов")
    @NotBlank(message = "Адрес Ru не должен быть пустым.")
    private String addressRu;
    @Size(min = 3, max = 100, message = "Адрес En не должен быть меньше 3 символов и не больше 100 сиволов")
    @NotBlank(message = "Адрес En не должен быть пустым.")
    private String addressEn;
    private String instagram;
    private String tiktok;
    @Size(min = 12, max = 12, message = "Номер должен быть ровно в 12 символов")
    @Pattern(regexp = "^\\+993.*$", message = "Номер должен начинаться +993")
    private String cityNumber;
    @ValidPhoneSize(message = "Номер должен быть ровно в 12 символов")
    @ValidPhoneNumber(message = "Номер должен начинаться +993")
    private List<String> mobNumbers;
    private long categoryId;
    private long subCategoryId;
    private MultipartFile thumb;
    private List<MultipartFile> files;
    @NotNull(message = "Заполните широту")
    @NonZero(message = "Широта не должна быть пустым")
    private double lat;
    @NotNull(message = "Заполните долготу")
    @NonZero(message = "Долгота не должна быть пустым")
    private double lng;
    @Email(message = "Пожалуйста, введите корректный адрес электронной почты.")
    private String email;
    private String website;
    private List<Long> removeImageIds;

    public String getTitleTm() {
        return titleTm;
    }

    public void setTitleTm(String titleTm) {
        this.titleTm = titleTm;
    }

    public String getTitleRu() {
        return titleRu;
    }

    public void setTitleRu(String titleRu) {
        this.titleRu = titleRu;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getAddressTm() {
        return addressTm;
    }

    public void setAddressTm(String addressTm) {
        this.addressTm = addressTm;
    }

    public String getAddressRu() {
        return addressRu;
    }

    public void setAddressRu(String addressRu) {
        this.addressRu = addressRu;
    }

    public String getAddressEn() {
        return addressEn;
    }

    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTiktok() {
        return tiktok;
    }

    public void setTiktok(String tiktok) {
        this.tiktok = tiktok;
    }

    public String getCityNumber() {
        return cityNumber;
    }

    public void setCityNumber(String cityNumber) {
        this.cityNumber = cityNumber;
    }

    public List<String> getMobNumbers() {
        return mobNumbers;
    }

    public void setMobNumbers(List<String> mobNumbers) {
        this.mobNumbers = mobNumbers;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public MultipartFile getThumb() {
        return thumb;
    }

    public void setThumb(MultipartFile thumb) {
        this.thumb = thumb;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<Long> getRemoveImageIds() {
        return removeImageIds;
    }

    public void setRemoveImageIds(List<Long> removeImageIds) {
        this.removeImageIds = removeImageIds;
    }

    @Override
    public String toString() {
        return "PlaceRequest{" +
                "titleTm='" + titleTm + '\'' +
                ", titleRu='" + titleRu + '\'' +
                ", titleEn='" + titleEn + '\'' +
                ", addressTm='" + addressTm + '\'' +
                ", addressRu='" + addressRu + '\'' +
                ", addressEn='" + addressEn + '\'' +
                ", instagram='" + instagram + '\'' +
                ", tiktok='" + tiktok + '\'' +
                ", cityNumber='" + cityNumber + '\'' +
                ", mobNumbers=" + mobNumbers +
                ", categoryId='" + categoryId + '\'' +
                ", subCategoryId='" + subCategoryId + '\'' +
                ", thumb=" + thumb +
                ", files=" + files +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                '}';
    }

}
