package tm.ugur.util.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.StopDTO;
import tm.ugur.models.Stop;

@Component
public class StopMapper extends AbstractMapper<Stop, StopDTO>{
    public StopMapper() {
        super(Stop.class, StopDTO.class);
    }


}
