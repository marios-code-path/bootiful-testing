package com.example.sportsnetserver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Data @Entity
public class Team {
    @Id
    @GeneratedValue
    Long id;
    String name;
}
