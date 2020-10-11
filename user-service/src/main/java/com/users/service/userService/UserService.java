package com.users.service.userService;

import com.users.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    Response addUser(AddUserDto user);
    UserResponseDto getUserByUserId(String uuidUser);
    UpdateUserDto updateUser(String uuidUser, UpdateUserDto user);
    Boolean deleteUser(String uuidUser);
    UserProfileDto checkUsersProfile(String uuidUser);
    List<UserResponseDto> getUsers(int page, int limit);

//    confirm-email
    Boolean verifyEmailToken(String token);



}
