package com.cbk.extract.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "operation")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String date;

    @Column
    private String recipient;

    @Column
    private Double sum;

    @Column
    private String operation;
}
