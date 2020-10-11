package com.school.services.usersDetails;

import com.school.dto.AddChildDetailsDto;
import com.school.dto.Response;
import com.school.dto.ResponseChildDetails;
import com.school.entity.SchoolDetails;
import com.school.enums.LoggerMessages;
import com.school.repository.SchoolDetailsRepo;
import com.school.security.AES;
import com.school.utils.UserUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    ModelMapper modelMapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private String currentDate = LocalDateTime.now().toLocalDate().toString();

    @Autowired
    SchoolDetailsRepo schoolDetailsRepo;
    @Autowired
    UserUtils utils;
    @Value("secretPassword")
    String secretWord;


    @Override
    public Response addChildDetails(AddChildDetailsDto childDetails) {
        SchoolDetails schoolDetails = modelMapper.map(childDetails, SchoolDetails.class);
        schoolDetails.setUuidChildDetails(utils.generateUserId().toString());
        String encodeSchoolPassword = encryptSchoolPassword(childDetails.getSchoolPassword());
        schoolDetails.setSchoolPassword(encodeSchoolPassword);
        LOGGER.info(LoggerMessages.PASSWORD_ENCODE + LoggerMessages.FOR_CHILD + LoggerMessages.WITH_UUID
                + schoolDetails.getUuidChild());
        schoolDetailsRepo.save(schoolDetails);
        LOGGER.info(LoggerMessages.CHILD + LoggerMessages.WITH_UUID + LoggerMessages.WAS_SAVE);
        ResponseChildDetails responseChildDetails = modelMapper.map(schoolDetails, ResponseChildDetails.class);
        return new Response(responseChildDetails, HttpServletResponse.SC_CREATED, currentDate);
    }

    private String encryptSchoolPassword(String schoolPassword) {
        return AES.encrypt(schoolPassword, secretWord);
    }

    @Override
    public ResponseChildDetails updateChildDetails(AddChildDetailsDto childDetails) {
        Optional<SchoolDetails> optionalSchoolDetails = schoolDetailsRepo.findByUuidChildAndDeleted(childDetails.getUuidChild(),
                false);
        if(!optionalSchoolDetails.isPresent()){
            LOGGER.info(LoggerMessages.CHILD + LoggerMessages.WITH_UUID + childDetails.getUuidChild() +
                    LoggerMessages.NOT_FOUND);
            return null;
        }
        SchoolDetails schoolDetails = optionalSchoolDetails.get();
        String encodeSchoolPassword = encryptSchoolPassword(childDetails.getSchoolPassword());
        if (!childDetails.getSchoolPassword().isEmpty()) {
            schoolDetails.setSchoolPassword(encodeSchoolPassword);
        }
        schoolDetails.setSchoolUserName(childDetails.getSchoolUserName());
        schoolDetailsRepo.save(schoolDetails);
        LOGGER.info(LoggerMessages.CHILD + LoggerMessages.WITH_UUID + childDetails.getUuidChild()
        + LoggerMessages.WAS_UPDATE);
        return modelMapper.map(schoolDetails, ResponseChildDetails.class);
    }

    @Override
    public ResponseChildDetails getChildDetailsByUuid(String uuidChildDetails) {
        Optional<SchoolDetails> optionalSchoolDetails = schoolDetailsRepo.findByUuidChildDetailsAndDeleted(
                uuidChildDetails,false);
        if(!optionalSchoolDetails.isPresent()){
            LOGGER.info(LoggerMessages.CHILD + LoggerMessages.WITH_UUID + uuidChildDetails +
                    LoggerMessages.NOT_FOUND);
            return null;
        }
        return modelMapper.map(optionalSchoolDetails.get(), ResponseChildDetails.class);
    }

    @Override
    public ResponseChildDetails getChildDetailsByChildUuid(String uuidChild) {
        Optional<SchoolDetails> optionalSchoolDetails = schoolDetailsRepo.findByUuidChildAndDeleted(uuidChild,
                false);
        if(!optionalSchoolDetails.isPresent()){
            LOGGER.info(LoggerMessages.CHILD + LoggerMessages.WITH_UUID + uuidChild+
                    LoggerMessages.NOT_FOUND);
            return null;
        }
        return modelMapper.map(optionalSchoolDetails.get(), ResponseChildDetails.class);
    }

    @Override
    public Boolean deleteChildDetails(String uuidChildDetails) {
        Optional<SchoolDetails> optionalSchoolDetails = schoolDetailsRepo.findByUuidChildDetailsAndDeleted(
                uuidChildDetails,false);
        if(!optionalSchoolDetails.isPresent()){
            LOGGER.info(LoggerMessages.CHILD + LoggerMessages.WITH_UUID + uuidChildDetails +
                    LoggerMessages.NOT_FOUND);
            return false;
        }
        SchoolDetails schoolDetails = optionalSchoolDetails.get();
        schoolDetails.setDeleted(true);
        schoolDetailsRepo.save(schoolDetails);
        LOGGER.info(LoggerMessages.CHILD + LoggerMessages.WITH_UUID + schoolDetails.getUuidChild() +
                LoggerMessages.DELETED);
        return true;
    }

    @Override
    public List<ResponseChildDetails> getAllchildDetails(int page, int limit) {
        if (page > 0) page = page - 1;
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<SchoolDetails> usersPage = schoolDetailsRepo.findAll(pageableRequest);
        List<SchoolDetails> users = usersPage.getContent();
        List<ResponseChildDetails> returnValue = new ArrayList<>();
        users.stream().filter(user -> !user.getDeleted()).map(this::toDto)
                .forEachOrdered(returnValue::add);
        LOGGER.info(LoggerMessages.LIST_OF_CHILD_DETAILS + LoggerMessages.RETURNED);
        return returnValue;
    }

    private ResponseChildDetails toDto(SchoolDetails user) {
        return modelMapper.map(user, ResponseChildDetails.class);
    }

}
