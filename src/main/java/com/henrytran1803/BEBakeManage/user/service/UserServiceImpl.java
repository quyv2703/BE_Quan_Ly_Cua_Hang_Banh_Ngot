package com.henrytran1803.BEBakeManage.user.service;

import com.henrytran1803.BEBakeManage.common.exception.error.QuyExeption;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.user.dto.UserRequest;
import com.henrytran1803.BEBakeManage.user.dto.UserResponseRegisterDTO;
import com.henrytran1803.BEBakeManage.user.entity.Role;
import com.henrytran1803.BEBakeManage.user.entity.User;
import com.henrytran1803.BEBakeManage.user.repository.RoleRepository;
import com.henrytran1803.BEBakeManage.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByEmail(userName);
    }
    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

 /*   @Override
    public ApiResponse<List<UserResponseRegisterDTO>> getActiveUsers(boolean isActive) {
        return null;
    }*/


    @Override
    public ApiResponse<Page<UserResponseRegisterDTO>> getActiveUsers(boolean isActive, Pageable pageable) {
        // Lấy danh sách user dựa trên trạng thái và phân trang
        Page<User> users = userRepository.findByIsActive(isActive, pageable);

        // Map danh sách user sang danh sách DTO
        Page<UserResponseRegisterDTO> userDTOs = users.map(user -> new UserResponseRegisterDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getDateOfBirth().toString(),
                user.getIsActive(),
                // Chuyển đổi Set<Role> thành Set<Long> (id của roles)
                user.getRoles().stream()
                        .map(Role::getId) // Giả sử Role có phương thức getId() để lấy ID
                        .collect(Collectors.toSet())
        ));

        // Trả về ApiResponse với phân trang
        return ApiResponse.Q_success(userDTOs, QuyExeption.SUCCESS);
    }


    @Override
    @Transactional
    public ApiResponse<UserResponseRegisterDTO> updateUser(int id, UserRequest userRequest) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.USER_NOT_FOUND);
        }

        User user = optionalUser.get();

        // Cập nhật thông tin cơ bản
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());

        // Kiểm tra và mã hóa mật khẩu nếu có thay đổi
        if (!userRequest.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        user.setDateOfBirth(userRequest.getDateOfBirth());
        user.setIsActive(userRequest.getIsActive());

        // Cập nhật roles
        Set<Role> roles = new HashSet<>();
        for (Long roleId : userRequest.getRoleIds()) {
            Optional<Role> roleOptional = roleRepository.findById(Math.toIntExact(roleId));
            if (roleOptional.isEmpty()) {
                return ApiResponse.Q_failure(null, QuyExeption.ROLE_NOT_FOUND);
            }
            roles.add(roleOptional.get());
        }
        user.setRoles(roles);

        // Lưu thông tin vào cơ sở dữ liệu
        userRepository.save(user);
        // Map sang DTO
        UserResponseRegisterDTO userDTO = new UserResponseRegisterDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getDateOfBirth().toString(),
                user.getIsActive(),
                // Chuyển đổi Set<Role> thành Set<Long> (id của roles)
                user.getRoles().stream()
                        .map(Role::getId) // Giả sử Role có phương thức getId() để lấy ID
                        .collect(Collectors.toSet())
        );
        return ApiResponse.Q_success(userDTO, QuyExeption.SUCCESS);
    }

    @Override
    @Transactional
    public ApiResponse<Void> deactivateUser(int id) {
        // Tìm User theo ID
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.USER_NOT_FOUND); // Lỗi nếu không tìm thấy
        }

        // Cập nhật trạng thái active của user
        User user = optionalUser.get();
        if (!user.getIsActive()) {
            return ApiResponse.Q_failure(null, QuyExeption.USER_ALREADY_INACTIVE); // Nếu đã inactive thì trả lỗi
        }

        user.setIsActive(false); // Khóa tài khoản
        userRepository.save(user);

        return ApiResponse.Q_success(null, QuyExeption.SUCCESS); // Thành công
    }

    @Override
    @Transactional
    public ApiResponse<Void> activateUser(int id) {
        // Tìm User theo ID
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.USER_NOT_FOUND); // Lỗi nếu không tìm thấy
        }

        // Cập nhật trạng thái active của user
        User user = optionalUser.get();
        if (!user.getIsActive()) {
            return ApiResponse.Q_failure(null, QuyExeption.USER_ALREADY_INACTIVE); // Nếu đã inactive thì trả lỗi
        }

        user.setIsActive(true); // Khóa tài khoản
        userRepository.save(user);

        return ApiResponse.Q_success(null, QuyExeption.SUCCESS); // Thành công
    }

    @Override
    public ApiResponse<UserResponseRegisterDTO> findUserById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.USER_NOT_FOUND);
        }

        User user = optionalUser.get();
        UserResponseRegisterDTO userDTO = new UserResponseRegisterDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getDateOfBirth().toString(),
                user.getIsActive(),
                // Chuyển đổi Set<Role> thành Set<Long> (id của roles)
                user.getRoles().stream()
                        .map(Role::getId) // Giả sử Role có phương thức getId() để lấy ID
                        .collect(Collectors.toSet())
        );

        return ApiResponse.Q_success(userDTO, QuyExeption.SUCCESS);
    }

}
