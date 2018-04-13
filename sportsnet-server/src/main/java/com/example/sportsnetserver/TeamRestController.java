package com.example.sportsnetserver;

import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Collection;

@RestController
@RequestMapping(path = "/team")
public class TeamRestController {


    private final TeamRepository teamRepository;

    public TeamRestController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping("/{name}")
    public Collection<Team> getByTeamName(@PathParam("name") String name) {
        return teamRepository.findByName(name);
    }

    @GetMapping("/champion")
    public Team getChampion() {
        return teamRepository.findRangers();
    }

    @GetMapping("/all")
    public Collection<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @PostMapping("/new")
    public Team addTeam(@RequestBody Team team) {
        team.setId(null);
        return teamRepository.save(team);
    }

}
