package com.children.services.validator;

import com.children.entity.ChildrenEntity;
import com.children.enums.ErrorMessages;
import com.children.enums.LoggerMessages;
import com.children.exception.ChildrenServiceException;
import com.children.repo.ChildRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidatorServiceImpl implements ValidatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorServiceImpl.class);

    @Autowired
    ChildRepo childRepo;

    @Override
    public Boolean checkUniqueTZ(String tz) {
        Optional<ChildrenEntity> optionalChildrenEntity = childRepo.findByTzAndDeleted(
                tz, false);
        if (optionalChildrenEntity.isPresent()) {
           LOGGER.error(LoggerMessages.CHILD + LoggerMessages.PROVIDED_TZ + LoggerMessages.EXISTS);
            throw new ChildrenServiceException(ErrorMessages.CHILD_WITH_TZ_EXISTS.getErrorMessage());
        }
        return true;
    }
}

