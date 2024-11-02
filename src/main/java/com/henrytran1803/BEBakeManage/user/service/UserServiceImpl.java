package com.henrytran1803.BEBakeManage.user.service;

import com.henrytran1803.BEBakeManage.user.entity.User;
import com.henrytran1803.BEBakeManage.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByEmail(userName);
    }
    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }
}
