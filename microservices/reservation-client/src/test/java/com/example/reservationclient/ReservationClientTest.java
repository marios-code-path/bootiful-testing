package com.example.reservationclient;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureStubRunner(ids = "com.example:reservation-service:+:8080", workOffline = true)
public class ReservationClientTest {

    @Autowired
    private ReservationClient client;

    @Test
    public void testShouldConstruct() {
        Reservation res = new Reservation(1L, "MARIO");

        Assertions.assertThat(res)
                .as("should have constructed")
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    public void getReservations() throws Exception {

        Collection<Reservation> res = this.client.getReservations();
        Assertions.assertThat(res.size()).isEqualTo(1);

        Assertions.assertThat(
                res.stream()
                        .filter(r -> r.getReservationName().equalsIgnoreCase("CAFE"))
                        .count()
        ).isEqualTo(1);
    }
}