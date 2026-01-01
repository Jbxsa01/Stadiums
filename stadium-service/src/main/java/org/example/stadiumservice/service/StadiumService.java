
package org.example.stadiumservice.service;


import org.example.stadiumservice.model.Stadium;
import org.example.stadiumservice.repository.StadiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;

@Service
public class StadiumService {
    private final StadiumRepository stadiumRepository;

    @Autowired
    public StadiumService(StadiumRepository stadiumRepository) {
        this.stadiumRepository = stadiumRepository;
    }

    public List<Stadium> getAllStadiums() {
        return stadiumRepository.findAll();
    }

    public Stadium saveStadium(Stadium stadium) {
        return stadiumRepository.save(stadium);
    }
    public Stadium updateStadium(Stadium stadium) {
        if (!stadiumRepository.existsById(stadium.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stadium not found");
        }
        return stadiumRepository.save(stadium);
    }

    public void deleteStadium(Long id) {
        if (!stadiumRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stadium not found");
        }
        stadiumRepository.deleteById(id);
    }

    public Stadium getStadiumById(Long id) {
        return stadiumRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stadium not found"));
    }
}
