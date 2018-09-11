package com.example.sportsnetserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureJsonTesters
public class RestControllerTests {

    @MockBean
    TeamRepository teamRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        Mockito.when(teamRepository.findFavoriteTeam())
                .thenReturn(new Team(1L, "RANGERS"));

        Mockito.when(teamRepository.findAll())
                .thenReturn(Arrays.asList(
                        new Team(1L, "RANGERS"),
                        new Team(2L, "ASTROS")
                ));

        Mockito.when(teamRepository.save(Mockito.any(Team.class)))
                .thenReturn(new Team(1L, "RANGERS"));
    }

    @Test
    public void testShouldGETFavorite() throws Exception {
        mockMvc.perform(get("/team/favorite"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("@.name").value("RANGERS"));
    }

    @Test
    public void testShouldGetAll() throws Exception {
        mockMvc.perform(get("/team/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("@").isNotEmpty());
    }
}
