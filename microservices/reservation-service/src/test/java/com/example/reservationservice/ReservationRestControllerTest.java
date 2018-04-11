package com.example.reservationservice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;

@WebMvcTest
@RunWith(SpringRunner.class)
public class ReservationRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ReservationRepository reservationRepository;

	@Before
	public void setUp() {
		Mockito.when(reservationRepository.findAll())
				.thenReturn(
						Arrays.asList(
								new Reservation(1L, "CAFEBABE")
						)
				);
	}

	@Test
	public void getAllReservations() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/reservations"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("@.[0].reservationName").value("CAFEBABE"));

	}
}
