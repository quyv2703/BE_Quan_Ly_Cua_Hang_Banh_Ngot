package com.henrytran1803.BEBakeManage.Image.repository;
import com.henrytran1803.BEBakeManage.Image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByProductId(Integer productId); // Phương thức tìm kiếm theo productId

}