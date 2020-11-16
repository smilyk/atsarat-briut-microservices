package com.users.service.admin;

import com.users.constants.SecurityConstants;
import com.users.dto.EmailVerificationDto;
import com.users.dto.Response;
import com.users.dto.UserResponseDto;
import com.users.entity.Users;
import com.users.enums.LoggerMessages;
import com.users.repository.UserRepo;
import com.users.service.userService.UserServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class AdminService {
    private String currentDate = LocalDateTime.now().toLocalDate().toString();
    ModelMapper modelMapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    UserRepo userRepo;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    public Users createSuperAdmin() {
        String adminEmail = "admin@admin.com";
        String adminPassword = "adminPassword";
        String adminUuid="1111";
        String adminFirstName = "administrator";
        Optional<Users> userFromDB = userRepo.findByMainEmail(adminEmail);
        if(userFromDB.isPresent()){
            return userFromDB.get();
        }
        Users superAdmin = new Users();
        superAdmin.setPassword(bCryptPasswordEncoder.encode(adminPassword));
        superAdmin.setConfirmEmail(true);
        superAdmin.setDeleted(false);
        superAdmin.setUuidUser("1111");
        superAdmin.setFirstName(adminFirstName);
        superAdmin.setMainEmail(adminEmail);

        userRepo.save(superAdmin);
        LOGGER.info("super admin created");
        return superAdmin;
    }
}
