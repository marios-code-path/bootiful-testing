package com.example.sportsnetserver;


import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;


@RunWith(MockitoJUnitRunner.class)
public class BaseRestTeamContracts {
    @Mock
    TeamRepository teamRepo;
    @InjectMocks
    TeamRestController teamRestController;

    @Test
    public void testShouldReceiveTeams() {
        Team rangers = new Team(1L, "RANGERS");

        given(teamRepo.save(argThat(team -> team!=null && team.getName() != null)))
                .willReturn(rangers);
        given(teamRepo.findAll())
                .willReturn(Arrays.asList(
                        new Team(1L, "RANGERS"),
                        new Team(2L, "ASTROS")
                ));
        given(teamRepo.findRangers())
                .willReturn(rangers);

        RestAssuredMockMvc.standaloneSetup(teamRestController);
    }

    private TypeSafeMatcher<Team> teamWithName() {
        return new TypeSafeMatcher<Team>() {
            @Override
            protected boolean matchesSafely(Team teamInput) {
                return teamInput.getName() != null;
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

}
