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

    @GetMapping("/all")
    public Collection<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @GetMapping("/favorite")
    public Team getChampion() {
        return teamRepository.findFavoriteTeam();
    }

}
