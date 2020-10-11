package com.tsofim.servicers.validators;

import com.tsofim.entity.TsofimDetails;
import com.tsofim.enums.ErrorMessages;
import com.tsofim.enums.LoggerMessages;
import com.tsofim.exception.TsofimServiceException;
import com.tsofim.repository.TsofimDetailsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidatorServiceImpl implements ValidatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorServiceImpl.class);

    @Autowired
    TsofimDetailsRepo repo;
    @Override
    public void checkChildUuidUnique(String uuidChild) {
        Optional<TsofimDetails> tsofimDetails = repo.findByUuidChildAndDeleted(uuidChild, false);
        if (tsofimDetails.isPresent()) {
            LOGGER.error(ErrorMessages.CHILD_WITH_UUID_EXISTS + " " + uuidChild);
            throw new TsofimServiceException(ErrorMessages.CHILD_WITH_UUID_EXISTS.getErrorMessage() + uuidChild);
        }
        LOGGER.info(LoggerMessages.CHECK_UNIQ_CHILD_WITH_UUID + uuidChild);
    }
}
