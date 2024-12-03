package com.henrytran1803.BEBakeManage.quycode.entity;



import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@jakarta.persistence.Table(name="tables")
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "qr_code_url", nullable = false)
    private String qrCodeUrl; // Đường dẫn lưu QR Code

    @Column(name = "active", nullable = false)
    private boolean active; // Trạng thái bàn (true/false)

    @ManyToOne
    @JoinColumn(name = "area_id", nullable = false)
    private Area area; // Liên kết với khu vực
}
