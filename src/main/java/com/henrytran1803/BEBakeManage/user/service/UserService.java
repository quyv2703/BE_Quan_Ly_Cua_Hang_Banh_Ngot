package com.henrytran1803.BEBakeManage.user.service;
import com.henrytran1803.BEBakeManage.user.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserByUserName(String userName);
    Optional<User> getUserById(int id);
}
