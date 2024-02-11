package tm.ugur.services.api;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.StopDTO;
import tm.ugur.models.Client;
import tm.ugur.models.Stop;
import tm.ugur.repo.StopRepository;
import tm.ugur.security.ClientDetails;
import tm.ugur.util.errors.stop.StopNotFoundException;
import tm.ugur.util.mappers.StopMapper;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StopApiService {

    private final StopRepository stopRepository;

    private final StopMapper stopMapper;

    @Autowired
    public StopApiService(StopRepository stopRepository, ModelMapper modelMapper, StopMapper stopMapper) {
        this.stopRepository = stopRepository;
        this.stopMapper = stopMapper;
    }

    public List<StopDTO> getStops(){
        List<StopDTO> stopDTOS = this.stopRepository.findAllWithIdNameLocationCity().stream().map(this::convertToStopDTO).toList();
        stopDTOS.forEach(stopDTO -> stopDTO.setFavorite(this.isFavorite(stopDTO)));
        return stopDTOS;
    }

    public Optional<Stop> fetchStop(Long id){
        return this.stopRepository.findById(id);
    }

    public StopDTO getStop(Long id){
        StopDTO stopDTO =  this.convertToStopDTO(this.stopRepository.findById(id).orElseThrow(StopNotFoundException::new));
        stopDTO.setFavorite(this.isFavorite(stopDTO));
        return stopDTO;
    }

    public Optional<Stop> findByClientsAndId(Client client, Long id){
        return this.stopRepository.findAllByClientsAndId(client, id);
    }


    @Transactional
    public void store(Stop stop){
        this.stopRepository.save(stop);
    }

    public StopDTO convertToStopDTO(Stop stop){
        return this.stopMapper.toDto(stop);
    }

    private boolean isFavorite(StopDTO stopDto){
        Client client = getAuthClient();
        return client.getStops().stream()
                .anyMatch(stop -> stop.getId() == stopDto.getId());
    }


    private Client getAuthClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getClient();
    }
}
