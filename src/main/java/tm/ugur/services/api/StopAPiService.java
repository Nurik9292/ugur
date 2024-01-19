package tm.ugur.services.api;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.StopDTO;
import tm.ugur.models.Stop;
import tm.ugur.repo.StopRepository;
import tm.ugur.util.errors.stop.StopNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StopAPiService {

    private final StopRepository stopRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public StopAPiService(StopRepository stopRepository, ModelMapper modelMapper) {
        this.stopRepository = stopRepository;
        this.modelMapper = modelMapper;
    }

    public List<StopDTO> getStops(){
        return this.stopRepository.findAll().stream().map(this::convertToStopDTO).toList();
    }


    public StopDTO getStop(int id){
        return this.convertToStopDTO(
                this.stopRepository.findById(id).orElseThrow(StopNotFoundException::new));
    }



    public Stop converToStop(StopDTO stopDTO){
        return this.modelMapper.map(stopDTO, Stop.class);
    }

    public StopDTO convertToStopDTO(Stop stop){
        return this.modelMapper.map(stop, StopDTO.class);
    }

}
