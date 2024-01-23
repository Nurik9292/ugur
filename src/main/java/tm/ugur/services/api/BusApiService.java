package tm.ugur.services.api;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.BusDTO;
import tm.ugur.models.Bus;
import tm.ugur.repo.BusRepository;
import tm.ugur.util.errors.buses.BusNotFoundException;
import tm.ugur.util.mappers.BusMapper;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BusApiService {

    private final BusRepository busRepository;

    private final BusMapper busMapper;

    @Autowired
    public BusApiService(BusRepository busRepository, BusMapper busMapper) {
        this.busRepository = busRepository;
        this.busMapper = busMapper;
    }

    public List<BusDTO> getBuses(){
        return this.busRepository.findAll().stream().map(this::convertToBusDTO).toList();
    }

    public BusDTO getBus(long id){
        Bus bus = this.busRepository.findById(id).orElseThrow(BusNotFoundException::new);
        return this.convertToBusDTO(bus);
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
        return this.busMapper.toEntity(busDTO);
    }

    public BusDTO convertToBusDTO(Bus bus){
        return this.busMapper.toDto(bus);
    }
}
