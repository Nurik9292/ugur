package tm.ugur.services.api;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.StopDTO;
import tm.ugur.models.Stop;
import tm.ugur.repo.StopRepository;
import tm.ugur.util.errors.stop.StopNotFoundException;
import tm.ugur.util.mappers.StopMapper;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StopAPiService {

    private final StopRepository stopRepository;

    private final StopMapper stopMapper;

    @Autowired
    public StopAPiService(StopRepository stopRepository, ModelMapper modelMapper, StopMapper stopMapper) {
        this.stopRepository = stopRepository;
        this.stopMapper = stopMapper;
    }

    public List<StopDTO> getStops(){
        return this.stopRepository.findAll().stream().map(this::convertToStopDTO).toList();
    }


    public StopDTO getStop(Long id){
        return this.convertToStopDTO(this.stopRepository.findById(id).orElseThrow(StopNotFoundException::new));
    }

    public StopDTO convertToStopDTO(Stop stop){
        return this.stopMapper.toDto(stop);
    }

}
