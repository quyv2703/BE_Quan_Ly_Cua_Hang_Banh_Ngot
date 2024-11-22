package com.henrytran1803.BEBakeManage.category.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity // Đảm bảo annotation này có mặt
@Table(name = "categories") // Tên bảng trong cơ sở dữ liệu
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

}
