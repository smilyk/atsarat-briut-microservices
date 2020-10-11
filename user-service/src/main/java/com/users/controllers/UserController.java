package com.users.controllers;

import com.users.dto.*;
import com.users.enums.RequestOperationName;
import com.users.enums.RequestOperationStatus;
import com.users.service.userService.UserServiceImpl;
import com.users.service.validatorService.ValidatorServiceImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users/v1")
public class UserController {
    private String currentDate = LocalDateTime.now().toLocalDate().toString();

    @Autowired
    ValidatorServiceImpl validatorService;
    @Autowired
    UserServiceImpl userService;

    @PostMapping()
    public Response createUser(@Valid @RequestBody AddUserDto userDetails) {
        validatorService.checkUniqueEmail(userDetails.getMainEmail());
        return userService.addUser(userDetails);
    }


    @GetMapping(path = "/{uuidUser}")
    public Response getUser(@PathVariable String uuidUser) {
        UserResponseDto userDto = userService.getUserByUserId(uuidUser);
        return new Response(userDto, HttpServletResponse.SC_FOUND, currentDate);
    }

    @GetMapping(path = "/check/{uuidUser}")
    public Response checkUserProfile(@PathVariable String uuidUser){
        UserProfileDto userProfileDto = userService.checkUsersProfile(uuidUser);
        return new Response(userProfileDto,HttpServletResponse.SC_OK, currentDate);
    }

    @PutMapping(path = "/{id}")
    public Response updateUser(@PathVariable String id,  @Valid @RequestBody UpdateUserDto userDetails) {
        UpdateUserDto updateUser = userService.updateUser(id, userDetails);
        return new Response(updateUser,HttpServletResponse.SC_OK, currentDate);
    }

    @DeleteMapping(path = "/{id}")
    public Response deleteUser(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        Boolean deleted = userService.deleteUser(id);
        if(deleted){
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }else{
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }
        return new Response(returnValue, HttpServletResponse.SC_OK, currentDate);
    }
//

    @GetMapping()
    public Response getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "2") int limit) {
        List<UserResponseDto> users = userService.getUsers(page, limit);
        Type listType = new TypeToken<List<UserResponseDto>>() {
        }.getType();
        List<UserResponseDto> returnValue = new ModelMapper().map(users, listType);
        return new Response(returnValue, HttpServletResponse.SC_FOUND, currentDate);
    }
    /**
     * email-verification - confirm-email
     * http://localhost:8080/users/email-verification?token=sdfsdf
     * */
    @GetMapping(path = "/email-verification")
    public Response verifyEmailToken(@RequestParam(value = "token") String token) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
        boolean isVerified = userService.verifyEmailToken(token);
        if (isVerified) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return new Response(returnValue, HttpServletResponse.SC_OK, currentDate);
    }


}
