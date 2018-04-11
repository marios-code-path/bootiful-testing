package com.example.sportsnetserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class SportsnetTeamRestController {

    @Autowired
    TeamRepository teamRepository;

    @GetMapping("/rangers")
    public Team getRangers() {
        return teamRepository.findRangers();
    }

    @GetMapping("/all")
    public Collection<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @PutMapping("/new")
    public Team addTeam(@RequestParam("name")String name) {
        return teamRepository.save(new Team(null, name));
    }

}
