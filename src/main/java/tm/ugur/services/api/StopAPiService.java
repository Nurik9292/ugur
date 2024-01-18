package tm.ugur.services.api;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.StopDTO;
import tm.ugur.models.Stop;
import tm.ugur.repo.StopRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StopAPiService {

    private final StopRepository stopRepository;

    @Autowired
    public StopAPiService(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    public List<StopDTO> findAll(){
        return this.stopRepository.findAll().stream().map(this::convertToStopDTO).toList();
    }



    public StopDTO findOne(int id){
        return this.convertToStopDTO(this.stopRepository.findById(id).orElse(null));
    }



    public Stop converToStop(StopDTO stopDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(stopDTO, Stop.class);
    }

    public StopDTO convertToStopDTO(Stop stop){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(stop, StopDTO.class);
    }

}
