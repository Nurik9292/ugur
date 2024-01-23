package tm.ugur.util.mappers;

import tm.ugur.dto.AbstractDTO;
import tm.ugur.models.AbstractEntity;

public interface Mapper<E extends AbstractEntity, D extends AbstractDTO> {

    E toEntity(D dto);

    D toDto(E entity);
}
