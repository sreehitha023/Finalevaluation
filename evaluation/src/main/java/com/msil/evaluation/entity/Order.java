package com.msil.evaluation.entity;

import com.msil.evaluation.constants.ErrorConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDetail userOrdersId;

    @NotBlank(message = ErrorConstants.Stock_Symbol_Not_Blank)
    @Size(min = 10, max = 25, message = ErrorConstants.Stock_Symbol_Length)
    private String stockSymbol;
    private String assertType = "SHARES";

    @Pattern(regexp = "(?i)buy|sell",message = ErrorConstants.Order_Type)
    private String orderType;

    @Positive(message =ErrorConstants.Price_Positive)
    @DecimalMin(value = "0.0", message = ErrorConstants.Price_Constraint)
    private double price;

    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = ErrorConstants.Quantity_Constraint)
    private int quantity;

    private String status;

    private LocalDateTime timestamp;
}