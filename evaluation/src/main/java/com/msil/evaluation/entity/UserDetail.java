package com.msil.evaluation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.msil.evaluation.constants.ErrorConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = ErrorConstants.Username_Blank)
    private String userName;

    @NotBlank(message = ErrorConstants.Password_Blank)
    @Pattern(regexp = ErrorConstants.Password_Pattern, message = ErrorConstants.Password_Constraint)
    private String password;

    @Column(unique = true)
    @Email(message = ErrorConstants.Invalid_Email)
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<WatchList> watchlists = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userOrdersId", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userTradeId", cascade = CascadeType.ALL)
    private List<Trade> trade = new ArrayList<>();

    @JsonIgnore
    private LocalDateTime created_Time;

}