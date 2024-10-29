package com.henrytran1803.BEBakeManage.user.service;

import com.henrytran1803.BEBakeManage.common.util.JwtUtils;
import com.henrytran1803.BEBakeManage.user.model.User;
import com.henrytran1803.BEBakeManage.user.model.Role;
import com.henrytran1803.BEBakeManage.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder; // Sử dụng PasswordEncoder thay vì BCryptPasswordEncoder

    public String login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.joining(","));

        // Tạo JWT
        return jwtUtils.generateJwtToken(user.getId(), roles);
    }
    public void register(String firstName, String lastName, String email, String dateOfBirth, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setDateOfBirth(java.sql.Date.valueOf(dateOfBirth)); // Chuyển đổi kiểu dữ liệu nếu cần
        user.setPassword(passwordEncoder.encode(password)); // Mã hóa mật khẩu
        user.setActive(true); // Hoặc giá trị mặc định khác

        // Lưu người dùng vào cơ sở dữ liệu
        userRepository.save(user);
    }

}
