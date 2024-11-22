package com.henrytran1803.BEBakeManage.user.repository;

import com.henrytran1803.BEBakeManage.user.entity.Role;
import com.henrytran1803.BEBakeManage.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
