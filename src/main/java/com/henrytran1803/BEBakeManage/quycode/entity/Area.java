package com.henrytran1803.BEBakeManage.quycode.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="areas")
@Data
public class Area  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng giá trị ID
    private Long id;

    @Column(name = "name", nullable = false, length = 100) // Cột "name" không được để trống
    private String name;

    @Column(name = "description", length = 255) // Cột "description" có độ dài tối đa 255 ký tự
    private String description;
}