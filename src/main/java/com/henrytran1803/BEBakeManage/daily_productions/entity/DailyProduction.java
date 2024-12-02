package com.henrytran1803.BEBakeManage.daily_productions.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "daily_productions")
public class DailyProduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="production_date")
    private LocalDateTime productionDate;
}

