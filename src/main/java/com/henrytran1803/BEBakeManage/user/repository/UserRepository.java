package com.henrytran1803.BEBakeManage.user.repository;

import com.henrytran1803.BEBakeManage.user.dto.UserBasicDTO;
import com.henrytran1803.BEBakeManage.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
  /*  // Tìm tất cả user theo trạng thái active
    List<User> findByIsActive(boolean isActive);*/
  @Query("SELECT new com.henrytran1803.BEBakeManage.user.dto.UserBasicDTO(u.id, u.firstName, u.lastName, u.email) FROM User u")
  List<UserBasicDTO> findAllBasicInfo();
        Page<User> findByIsActive(boolean isActive, Pageable pageable);
}