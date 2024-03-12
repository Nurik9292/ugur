package tm.ugur.dto;

import java.util.Objects;

public class PlacePhoneDTO extends AbstractDTO{

    private String number;

    private String type;

    public PlacePhoneDTO() {
    }

    public PlacePhoneDTO(String number, String type) {
        this.number = number;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlacePhoneDTO that = (PlacePhoneDTO) object;
        return Objects.equals(number, that.number) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, type);
    }

    @Override
    public String toString() {
        return "PlacePhoneDTO{" +
                "number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", id=" + id +
                '}';
    }
}
