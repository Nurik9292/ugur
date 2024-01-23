package tm.ugur.dto;

import java.io.Serializable;

public abstract class AbstractDTO implements Serializable {

    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
