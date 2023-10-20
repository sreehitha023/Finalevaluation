package com.msil.evaluation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "SYMBOL")
public class Symbols {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SYMBOL_ID")
    private Long id;
    @Column(name = "SYMBOL")
    private String symbol;

    @Column(name = "SYMBOL_NAME")
    private String symbolName;

    @Column(name = "INDEX_NAME")
    private String indexName;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "INDUSTRY")
    private String industry;

    @Column(name = "SERIES")
    private String series;

    @Column(name = "ISIN_CODE")
    private String isinCode;

    @Column(name = "EXCHANGE")
    private String exchange;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "SCRIP_CODE")
    private String scripCode;
}