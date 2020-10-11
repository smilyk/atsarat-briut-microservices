package com.users.service.validatorService;

import com.users.entity.ResponsePerson;
import com.users.entity.Users;
import com.users.enums.ErrorMessages;
import com.users.exception.UserServiceException;
import com.users.repository.RespPersonRepo;
import com.users.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidatorServiceImpl implements ValidatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorServiceImpl.class);
    @Autowired
    UserRepo userRepo;
    @Autowired
    RespPersonRepo respPersonRepo;


    @Override
    public Boolean checkUniqueEmail(String eMail) {
        Optional<Users> userEntity = userRepo.findByMainEmail(eMail);
        if (userEntity.isPresent()) {
            LOGGER.error(ErrorMessages.USER_WITH_EMAIL_EXISTS + " " + eMail);
            throw new UserServiceException(ErrorMessages.USER_WITH_EMAIL_EXISTS.getErrorMessage() + eMail);
        }
        return true;
    }

    @Override
    public void checkRespPersonUniqueEmail(String emailRespPerson) {
        Optional<ResponsePerson> optionalResponsePerson = respPersonRepo.findByEmailRespPerson(emailRespPerson);
        if (optionalResponsePerson.isPresent()) {
            LOGGER.error(ErrorMessages.RESP_PERSON_WITH_EMAIL_EXISTS + " " + emailRespPerson);
            throw new UserServiceException(ErrorMessages.USER_WITH_EMAIL_EXISTS.getErrorMessage() + emailRespPerson);
        }
    }
}
