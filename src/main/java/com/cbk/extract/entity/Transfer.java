package com.cbk.extract.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String date;

    @Column
    private String platformReferenceNumber;

    @OneToOne
    private ReceivingAmount receivingAmount;
}
