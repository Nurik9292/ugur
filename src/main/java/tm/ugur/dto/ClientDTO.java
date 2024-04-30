package tm.ugur.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

public class ClientDTO {

    @NotEmpty(message = "Заполните поле.")
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ClientDTO clientDTO = (ClientDTO) object;
        return Objects.equals(phone, clientDTO.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone);
    }
}
