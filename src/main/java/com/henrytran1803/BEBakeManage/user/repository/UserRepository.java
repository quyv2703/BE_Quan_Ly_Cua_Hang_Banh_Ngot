package com.henrytran1803.BEBakeManage.user.repository;

import com.henrytran1803.BEBakeManage.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    // Tìm tất cả user theo trạng thái active
    List<User> findByIsActive(boolean isActive);
}