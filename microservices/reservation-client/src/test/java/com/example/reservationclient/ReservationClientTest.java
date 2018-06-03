package com.example.reservationclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(ids = "com.example:reservation-service:+:8080", workOffline = true)
public class ReservationClientTest {

    @Autowired
    private ReservationClient client;

    @Test
    public void getReservations() throws Exception {

        Collection<Reservation> res = this.client.getReservations();
        Assertions.assertThat(res.size()).isEqualTo(2);

        Assertions.assertThat(res.stream().filter(r -> r.getReservationName().equalsIgnoreCase("CAFEBABE")).count()).isEqualTo(1);
    }
}
