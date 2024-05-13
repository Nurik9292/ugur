package tm.ugur.dto;


import java.util.Objects;

public class SocialNetworkDTO extends AbstractDTO{

    private String link;

    private String name;

    public SocialNetworkDTO() {
    }

    public SocialNetworkDTO(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SocialNetworkDTO that = (SocialNetworkDTO) object;
        return Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link);
    }

    @Override
    public String toString() {
        return "SocialNetworkDTO{" +
                "link='" + link + '\'' +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
