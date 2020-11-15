package com.users.service.userService;

import com.users.dto.*;
import com.users.entity.Users;
import com.users.entity.resetPassword.PasswordResetTokenEntity;
import com.users.enums.ErrorMessages;
import com.users.enums.LoggerMessages;
import com.users.repository.PasswordResetTokenRepository;
import com.users.repository.UserRepo;
import com.users.utils.UserUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RefreshScope
public class UserServiceImpl implements UserService {
    private String currentDate = LocalDateTime.now().toLocalDate().toString();
    ModelMapper modelMapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value(("${email.exchange}"))
    String emailExchange;
    @Value(("${email.key}"))
    String emailRoutingkey;

    @Value(("${email.password.exchange}"))
    String emailPasswordExchange;
    @Value(("${email.password.key}"))
    String emailPasswordRoutingkey;


    @Autowired
    UserRepo userRepo;
    @Autowired
    UserUtils utils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public Response addUser(AddUserDto user) {
        Users userEntity = modelMapper.map(user, Users.class);
        userEntity.setUuidUser(utils.generateUserId().toString());
        userEntity.setConfirmEmailToken(null);
        userEntity.setPassword(bCryptPasswordEncoder.encode(user.getNotDecryptedPassword()));
        userEntity.setConfirmEmailToken(utils.generateEmailVerificationToken(utils.generateUserId().toString()));
        UserResponseDto userResponseDto = modelMapper.map(userEntity, UserResponseDto.class);
        EmailVerificationDto emailDto = EmailVerificationDto.builder()
                .email(userEntity.getMainEmail())
                .tokenValue(userEntity.getConfirmEmailToken())
                .userLastName(userEntity.getSecondName())
                .userName(userEntity.getFirstName())
                .build();
//        method - send email
        rabbitTemplate.convertAndSend(emailExchange, emailRoutingkey, emailDto);
        LOGGER.info("verificatiWebSecurity on email was send to email " + userResponseDto.getMainEmail());
        userRepo.save(userEntity);
        LOGGER.info(LoggerMessages.ADD_USER + ' ' + userEntity.getMainEmail());
        return new Response(userResponseDto, HttpServletResponse.SC_CREATED, currentDate);
    }

    @Override
    public UserResponseDto getUserByUserId(String uuidUser) {
        Optional<Users> optionalUserEntity = userRepo.findByUuidUserAndDeleted(uuidUser, false);
        if (!optionalUserEntity.isPresent()) {
            LOGGER.error(LoggerMessages.USER_WITH_UUID + uuidUser + LoggerMessages.NOT_FOUND);
            throw new UsernameNotFoundException(
                    ErrorMessages.USER_WITH_UUID + uuidUser + ErrorMessages.NOT_FOUND);
        }
        LOGGER.info(LoggerMessages.USER_WITH_UUID + uuidUser + LoggerMessages.WAS_RETURND);
        return modelMapper.map(optionalUserEntity.get(), UserResponseDto.class);
    }


    @Override
    public UserProfileDto checkUsersProfile(String uuidUser) {
        Optional<Users> optionalUserEntity = userRepo.findByUuidUserAndDeleted(uuidUser, false);
        if (!optionalUserEntity.isPresent()) {
            LOGGER.error(LoggerMessages.USER_WITH_UUID + uuidUser + LoggerMessages.NOT_FOUND);
            throw new UsernameNotFoundException(
                    ErrorMessages.USER_WITH_UUID + uuidUser + ErrorMessages.NOT_FOUND);
        }
        LOGGER.info(LoggerMessages.USER_WITH_UUID + uuidUser + LoggerMessages.WAS_RETURND);
        return modelMapper.map(optionalUserEntity.get(), UserProfileDto.class);
    }

    @Override
    public List<UserResponseDto> getUsers(int page, int limit) {
        if (page > 0) page = page - 1;
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<Users> usersPage = userRepo.findAll(pageableRequest);
        List<Users> users = usersPage.getContent();
        List<UserResponseDto> returnValue = new ArrayList<>();
        users.stream().filter(user -> !user.getDeleted()).map(this::toDto)
                .forEachOrdered(returnValue::add);
        return returnValue;
    }

    private UserResponseDto toDto(Users user) {
        return modelMapper.map(user, UserResponseDto.class);
    }


    @Override
    public UpdateUserDto updateUser(String uuidUser, UpdateUserDto user) {
        Optional<Users> optionalUserEntity = userRepo.findByUuidUserAndDeleted(uuidUser, false);
        if (!optionalUserEntity.isPresent()) {
            LOGGER.error(LoggerMessages.USER_WITH_UUID + uuidUser + LoggerMessages.NOT_FOUND);
            throw new UsernameNotFoundException(
                    ErrorMessages.USER_WITH_UUID + uuidUser + ErrorMessages.NOT_FOUND);
        }
        LOGGER.info(LoggerMessages.USER_WITH_UUID + uuidUser + LoggerMessages.WAS_RETURND);
        Users userEntity = optionalUserEntity.get();
        if (user.getAltEmail() != null) {
            userEntity.setAltEmail(user.getAltEmail());
            LOGGER.info(LoggerMessages.USER_WITH_UUID + uuidUser + LoggerMessages.CHANGE +
                    LoggerMessages.ALT_EMAIL_TO + user.getAltEmail());
        }
        if (user.getFirstName() != null) {
            userEntity.setFirstName(user.getFirstName());
            LOGGER.info(LoggerMessages.USER_WITH_UUID + uuidUser + LoggerMessages.CHANGE +
                    LoggerMessages.FIRST_NAME + user.getFirstName());
        }
        if (user.getSecondName() != null) {
            userEntity.setSecondName(user.getSecondName());
            LOGGER.info(LoggerMessages.USER_WITH_UUID + uuidUser + LoggerMessages.CHANGE +
                    LoggerMessages.SECOND_NAME + user.getSecondName());
        }
        if (user.getTz() != null) {
            userEntity.setTz(user.getTz());
//            TODO зашифровать ТЗ
            LOGGER.info(LoggerMessages.USER_WITH_UUID + uuidUser + LoggerMessages.CHANGE +
                    LoggerMessages.TZ + user.getTz());
        }
        userRepo.save(userEntity);
        LOGGER.info((LoggerMessages.USER_WITH_UUID + uuidUser + LoggerMessages.WAS_UPDATE));
        return modelMapper.map(userEntity, UpdateUserDto.class);
    }

    @Override
    public Boolean deleteUser(String uuidUser) {
        Optional<Users> optionalUserEntity = userRepo.findByUuidUserAndDeleted(uuidUser, false);
        if (!optionalUserEntity.isPresent()) {
            LOGGER.error(LoggerMessages.USER_WITH_UUID + uuidUser + LoggerMessages.NOT_FOUND);
            throw new UsernameNotFoundException(
                    ErrorMessages.USER_WITH_UUID + uuidUser + ErrorMessages.NOT_FOUND);
        }
        Users userEntity = optionalUserEntity.get();
        userEntity.setDeleted(true);
//        if user deleted - dont wont to have his teudat zeut
        userEntity.setTz("000000000");
        userRepo.save(userEntity);
        LOGGER.info(LoggerMessages.USER_WITH_UUID + uuidUser + LoggerMessages.DELETED + currentDate);
        return true;
    }

    @Override
    public Boolean verifyEmailToken(String token) {
        boolean rez = false;
        // Find user by token
        Optional<Users> optionalUsersEntity = userRepo.findUserByConfirmEmailToken(token);
//            if there is token - that means - not confirmed
        if (optionalUsersEntity.isPresent()) {
//                check time of token
            boolean hastokenExpired = utils.hasTokenExpired(token);
            Users userEntity = optionalUsersEntity.get();
            if (!hastokenExpired) {
                userEntity.setConfirmEmailToken(null);
                userEntity.setConfirmEmail(Boolean.TRUE);
                userRepo.save(userEntity);
                LOGGER.info(LoggerMessages.USER_CONFIRM_EMAIL + userEntity.getMainEmail());
                rez = true;
            }
        }
        return rez;
    }

    @Override
    public UserDto getUser(String email) {
        Optional<Users> optionalUser = userRepo.findByMainEmail(email);
        if (!optionalUser.isPresent())
            throw new UsernameNotFoundException(email);
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(optionalUser.get(), returnValue);
        return returnValue;
    }

    /**
     * запрос на обноелние пароля шаг1
     */
    @Override
    public boolean requestPasswordReset(String email) {

        Optional<Users> optionalUser = userRepo.findByMainEmail(email);

        if (!optionalUser.isPresent()) {
            return false;
        }
        Users userEntity = optionalUser.get();
        String token = new UserUtils().generatePasswordResetToken(userEntity.getUuidUser());

        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserDetails(userEntity);
        passwordResetTokenRepository.save(passwordResetTokenEntity);
//        отправляем письмо. Если письмоотправлено - вернется True
        EmailVerificationDto emailDto = EmailVerificationDto.builder()
                .email(userEntity.getMainEmail())
                .tokenValue(token)
                .userLastName(userEntity.getSecondName())
                .userName(userEntity.getFirstName())
                .build();
//        method - send email
        rabbitTemplate.convertAndSend(emailPasswordExchange, emailPasswordRoutingkey, emailDto);
        LOGGER.info("new token for changing password was send to email " + emailDto.getEmail());
        return true;
    }

    /** запрос на обноелние пароля шаг2**/
    @Override
    public boolean resetPassword(String token, String password) {
        boolean returnValue = false;

        if( UserUtils.hasTokenExpired(token) )
        {
            return returnValue;
        }
        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);
        if (passwordResetTokenEntity == null) {
            return returnValue;
        }
        // Prepare new password
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        // Update User password in database
        Users userEntity = passwordResetTokenEntity.getUserDetails();
        userEntity.setPassword(encodedPassword);
        Users savedUserEntity = userRepo.save(userEntity);
        // Verify if password was saved successfully
        if (savedUserEntity != null && savedUserEntity.getPassword().equalsIgnoreCase(encodedPassword)) {
            returnValue = true;
        }
        // Remove Password Reset token from database
        passwordResetTokenRepository.delete(passwordResetTokenEntity);
        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Users> optionalUser = userRepo.findByMainEmail(email);
        if (!optionalUser.isPresent())
            throw new UsernameNotFoundException(email);
        /**
         * если optionalUser.get().getConfirmEmail(), == false -> login вернет error
         */
        return new User(optionalUser.get().getMainEmail(),
                optionalUser.get().getPassword(),
                optionalUser.get().getConfirmEmail(),
                true, true, true, new ArrayList<>());
    }

//        return new User(optionalUser.get().getMainEmail(), optionalUser.get().getPassword(), new ArrayList<>());
//    }
}
