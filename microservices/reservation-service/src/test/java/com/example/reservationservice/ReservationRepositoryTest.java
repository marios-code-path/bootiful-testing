package com.example.reservationservice;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository repo;

    @Autowired
    private TestEntityManager testEntityManager;

    @Before
    public void setUp() {
        testEntityManager.persist(new Reservation(null, "CAFE"));
    }

    @Test
    public void testShouldFindAllCafeBabes() throws Exception {

        Collection<Reservation> rs = this.repo.findAllCafeBabe();
        Assertions.assertThat(rs.size()).isEqualTo(1);
        Assertions.assertThat(rs.iterator().next().getId()).isNotNull();
        Assertions.assertThat(rs.iterator().next().getReservationName()).isEqualTo("CAFE");
    }
}

