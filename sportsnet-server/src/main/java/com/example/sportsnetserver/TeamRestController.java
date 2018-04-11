package com.example.sportsnetserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Collection;

@RestController
@RequestMapping(path = "/team")
public class TeamRestController {

    @Autowired
    TeamRepository teamRepository;

    @GetMapping("/byName/{name}")
    public Collection<Team> getByTeamName(@PathParam("name") String name) {
        return teamRepository.findTeamsByName(name);
    };

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
