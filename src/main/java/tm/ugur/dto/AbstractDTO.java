package tm.ugur.dto;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import tm.ugur.dto.views.Views;

import java.io.Serializable;

public abstract class AbstractDTO implements Serializable {

    @JsonView(Views.Summary.class)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
