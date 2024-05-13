package tm.ugur.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class TranslationDTO extends AbstractDTO{

    private String locale;

    @NotBlank(message = "Заголовк не должен быть пустым.")
    private String title;

    @NotBlank(message = "Адрес не должен быть пустым.")
    private String address;

    public TranslationDTO(){}

    public TranslationDTO(String locale, String title){
        this.locale = locale;
        this.title = title;
    }

    public String getLocale() {

        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TranslationDTO that = (TranslationDTO) object;
        return Objects.equals(locale, that.locale) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locale, title);
    }

    @Override
    public String toString() {
        return "TranslationDTO{" +
                "locale='" + locale + '\'' +
                ", title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", id=" + id +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
