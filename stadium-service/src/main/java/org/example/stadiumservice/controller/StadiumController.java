package org.example.stadiumservice.controller;

import org.example.stadiumservice.model.Stadium;
import org.example.stadiumservice.service.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stadiums")
public class StadiumController {
    private final StadiumService stadiumService;

    @Autowired
    public StadiumController(StadiumService stadiumService) {
        this.stadiumService = stadiumService;
    }

    @GetMapping
    public List<Stadium> getAllStadiums() {
        return stadiumService.getAllStadiums();
    }

    @PostMapping
    public Stadium createStadium(@RequestBody Stadium stadium) {
        return stadiumService.saveStadium(stadium);
    }

    @PutMapping("/{id}")
    public Stadium updateStadium(@PathVariable Long id, @RequestBody Stadium stadium) {
        stadium.setId(id);
        return stadiumService.updateStadium(stadium);
    }

    @DeleteMapping("/{id}")
    public void deleteStadium(@PathVariable Long id) {
        stadiumService.deleteStadium(id);
    }
}
