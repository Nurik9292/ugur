package tm.ugur.dto;

import java.util.Objects;

public class PlaceImageDTO{

    private String path;

    public PlaceImageDTO() {
    }

    public PlaceImageDTO(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlaceImageDTO that = (PlaceImageDTO) object;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "PlaceImageDTO{" +
                "path='" + path + '\'' +
                '}';
    }
}
