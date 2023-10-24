package com.msil.evaluation.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AuthRequest {
   private String userName;
   private String password;
}
