package com.henrytran1803.BEBakeManage.user.service;

import com.henrytran1803.BEBakeManage.common.util.JwtUtils;
import com.henrytran1803.BEBakeManage.user.entity.User;
import com.henrytran1803.BEBakeManage.user.entity.Role;
import com.henrytran1803.BEBakeManage.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;
@Autowired
private  UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(String email, String password) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            System.out.println(user);
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);

            if (user.isPresent()) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password));

                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                String roles = String.join(",", user.get().getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList()));
                return jwtUtils.generateJwtToken(user.get().getId(), roles);
            } else {
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid login credentials", e);
        }
    }

    public void register(String firstName, String lastName, String email, String dateOfBirth, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setDateOfBirth(java.sql.Date.valueOf(dateOfBirth));
        user.setPassword(passwordEncoder.encode(password));
        user.setActive(true);

        userRepository.save(user);
    }

}
