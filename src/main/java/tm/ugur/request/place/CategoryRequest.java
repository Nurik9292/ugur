package tm.ugur.request.place;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class CategoryRequest {

    @NotBlank(message = "Заголовок на Tm не должен быть пустым")
    @Size(min = 3, max = 100, message = "Заголовок на Tm не должен быть меньше 3 и больше 100 символов")
    private String titleTm;
    @NotBlank(message = "Заголовок на Ru не должен быть пустым")
    @Size(min = 3, max = 100, message = "Заголовок на Ru не должен быть меньше 3 и больше 100 символов")
    private String titleRu;
    @NotBlank(message = "Заголовок на En не должен быть пустым")
    @Size(min = 3, max = 100, message = "Заголовок на En не должен быть меньше 3 и больше 100 символов")
    private String titleEn;
    private MultipartFile thumb;

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

    public MultipartFile getThumb() {
        return thumb;
    }

    public void setThumb(MultipartFile thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "CategoryRequest{" +
                "titleTm='" + titleTm + '\'' +
                ", titleRu='" + titleRu + '\'' +
                ", titleEn='" + titleEn + '\'' +
                ", thumb=" + thumb +
                '}';
    }
}
