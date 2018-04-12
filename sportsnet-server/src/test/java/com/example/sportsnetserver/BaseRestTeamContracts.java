package com.example.sportsnetserver;


import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;


//@RunWith(MockitoJUnitRunner.class)
@RunWith(SpringRunner.class)
public class BaseRestTeamContracts {
    @Mock
    private TeamRepository teamRepo;

    @Before
    public void setUp() {
        TeamRestController teamRestController = new TeamRestController(teamRepo);
        Team rangers = new Team(1L, "RANGERS");

        given(teamRepo.save(argThat(team -> (team != null) && (team.getName() != null))))
                .willReturn(rangers);
        given(teamRepo.findAll())
                .willReturn(Arrays.asList(
                        new Team(1L, "RANGERS"),
                        new Team(2L, "ASTROS")
                ));
        given(teamRepo.findRangers())
                .willReturn(rangers);
        given(teamRepo.findByName(argThat(str -> str != null)))
                .willReturn(Collections.singletonList(new Team(1L, "GIANTS")));

        RestAssuredMockMvc.standaloneSetup(teamRestController);
    }

}
