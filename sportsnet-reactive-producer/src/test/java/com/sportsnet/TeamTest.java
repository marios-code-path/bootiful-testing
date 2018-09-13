package com.sportsnet;

import org.assertj.core.api.Assertions;
import org.junit.Test;


public class TeamTest {

    @Test
    public void create() throws Exception {
        Team team = new Team("1903", "Beşiktaş");
        Assertions.assertThat(team.getId()).isEqualToIgnoringWhitespace("1903");
        Assertions.assertThat(team.getName()).isEqualToIgnoringWhitespace("Beşiktaş");
    }
}
