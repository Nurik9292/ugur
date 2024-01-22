package tm.ugur.services.api;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.BusDTO;
import tm.ugur.models.Bus;
import tm.ugur.repo.BusRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BusApiService {

    private final BusRepository busRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public BusApiService(BusRepository busRepository, ModelMapper modelMapper) {
        this.busRepository = busRepository;
        this.modelMapper = modelMapper;
    }

    public List<BusDTO> getBuses(){
        return this.busRepository.findAll().stream().map(this::convertToBusDTO).toList();
    }

    public Bus getBus(long id){
        return this.busRepository.findById(id).orElse(null);
    }

    @Transactional
    public void store(Bus bus){
        this.busRepository.save(bus);
    }

    @Transactional
    public void update(long id, Bus bus){
        bus.setId(id);
        this.busRepository.save(bus);
    }


    public Bus converToBus(BusDTO busDTO){
        return this.modelMapper.map(busDTO, Bus.class);
    }

    public BusDTO convertToBusDTO(Bus bus){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(bus, BusDTO.class);
    }
}
