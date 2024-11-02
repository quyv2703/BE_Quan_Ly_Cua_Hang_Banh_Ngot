package com.henrytran1803.BEBakeManage.user.repository;

import com.henrytran1803.BEBakeManage.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
