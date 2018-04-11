package com.example.sportsnetserver;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SportsnetEntityTests {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    TeamRepository teamRepository;
    @Test
    public void testShouldConstructTeam() {
        Team team = new Team(null, "RANGERS");

        Assertions.assertThat(team).isNotNull();
        Assertions.assertThat(team.getId()).isNull();
        Assertions.assertThat(team.getName()).isEqualTo("RANGERS");
    }

    @Test
    public void testShouldPersistFlushFind() {
        Team team = testEntityManager.persistFlushFind(
                new Team(null, "RANGERS")
        );

        Assertions.assertThat(team).isNotNull();
        Assertions.assertThat(team.getId()).isNotNull();
        Assertions.assertThat(team.getName()).isEqualTo("RANGERS");
    }

    @Test
    public void testShouldFindByQuery() {
        testEntityManager.persistFlushFind(
                new Team(null, "RANGERS")
        );

        Team team = teamRepository.findRangers();

        Assertions.assertThat(team).isNotNull();
        Assertions.assertThat(team.getId()).isNotNull();
        Assertions.assertThat(team.getName()).isEqualTo("RANGERS");
    }
}
