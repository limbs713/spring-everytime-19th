package com.ceos19.springeverytime.user.service;

import com.ceos19.springeverytime.user.domain.User;
import com.ceos19.springeverytime.user.dto.request.UserLoginDto;
import com.ceos19.springeverytime.user.dto.request.UserRequestDto;
import com.ceos19.springeverytime.user.dto.response.ResponseUserDto;
import com.ceos19.springeverytime.user.exception.UserErrorCode;
import com.ceos19.springeverytime.user.exception.UserException;
import com.ceos19.springeverytime.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void createUser(UserRequestDto userRequestDto) {
        if (userRepository.existsUserByLoginId(userRequestDto.getId())) {
            throw new UserException(UserErrorCode.USER_ALREADY_EXIST);
        }

        User user = userRequestDto.toEntity();
        userRepository.save(user);
    }

    public ResponseUserDto readUser(Long userId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        return ResponseUserDto.of(user);
    }

    public List<User> readAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(UserRequestDto userRequestDto, Long userId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        user.update(userRequestDto.getPassword());
    }

    public void loginUser(UserLoginDto userLoginDto) {
        User user = userLoginDto.toEntity();

        User loginUser = userRepository.findUserByLoginId(user.getLoginId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (!loginUser.getPassword().equals(user.getPassword())) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }
    }
}
