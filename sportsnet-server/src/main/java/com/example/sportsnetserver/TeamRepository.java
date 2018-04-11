package com.example.sportsnetserver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, Long>{
    @Query("select t from Team t where t.name='RANGERS'")
    public Team findRangers();
}
