package com.cbk.extract.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ReceivingAmount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double amount;

    @Column
    private String currency;

}
