package com.henrytran1803.BEBakeManage.category.entity;

import jakarta.persistence.*;

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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
