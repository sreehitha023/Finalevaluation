package com.msil.evaluation.entity;

import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long sessionId;
    String token;
    @Column(unique = true)
    Long userId;

}
