package tm.ugur.dto;

import jakarta.persistence.Column;

import java.io.Serializable;

public abstract class AbstractDTO implements Serializable {

    @Column(name = "id")
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
