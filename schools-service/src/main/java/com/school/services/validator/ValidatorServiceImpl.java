package com.school.services.validator;

import com.school.entity.SchoolDetails;
import com.school.enums.ErrorMessages;
import com.school.enums.LoggerMessages;
import com.school.exception.SchoolServiceException;
import com.school.repository.SchoolDetailsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidatorServiceImpl implements ValidatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorServiceImpl.class);

    @Autowired
    SchoolDetailsRepo repo;
    @Override
    public void checkChildUuidUnique(String uuidChild) {
        Optional<SchoolDetails> schoolDetails = repo.findByUuidChildAndDeleted(uuidChild, false);
        if (schoolDetails.isPresent()) {
            LOGGER.error(ErrorMessages.CHILD_WITH_UUID_EXISTS + " " + uuidChild);
            throw new SchoolServiceException(ErrorMessages.CHILD_WITH_UUID_EXISTS.getErrorMessage() + uuidChild);
        }
        LOGGER.info(LoggerMessages.CHECK_UNIQ_CHILD_WITH_UUID + uuidChild);
    }
}
