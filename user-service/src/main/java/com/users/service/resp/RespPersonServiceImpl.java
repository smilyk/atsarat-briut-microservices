package com.users.service.resp;

import com.users.dto.AddRespPersonDto;
import com.users.dto.RepsoncePersonDto;
import com.users.dto.RespPersonResponseDto;
import com.users.dto.Response;
import com.users.entity.ResponsePerson;
import com.users.enums.ErrorMessages;
import com.users.enums.LoggerMessages;
import com.users.repository.RespPersonRepo;
import com.users.service.userService.UserServiceImpl;
import com.users.utils.UserUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RespPersonServiceImpl implements RespPersonService {
    private String currentDate = LocalDateTime.now().toLocalDate().toString();
    ModelMapper modelMapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    RespPersonRepo respPersonRepo;
    @Autowired
    UserUtils utils;
    @Override
    public Response addResponsePerson(AddRespPersonDto respPersonDetails) {
        ResponsePerson responsePerson = modelMapper.map(respPersonDetails, ResponsePerson.class);
        responsePerson.setUuidRespPerson(utils.generateUserId().toString());
        RespPersonResponseDto respPersonResponseDto= modelMapper.map(responsePerson, RespPersonResponseDto.class);
        respPersonRepo.save(responsePerson);
        LOGGER.info(LoggerMessages.ADD_RESP_PERSON + ' ' + responsePerson.getEmailRespPerson());
        return new Response(respPersonResponseDto, HttpServletResponse.SC_CREATED, currentDate);
    }

    @Override
    public RepsoncePersonDto updateResponsePerson(RepsoncePersonDto respPersonDetails) {
        Optional<ResponsePerson> optionalResponsePerson = respPersonRepo
                .findByUuidRespPersonAndDeleted(respPersonDetails.getUuidRespPerson(), false);
        if (!optionalResponsePerson.isPresent()) {
            LOGGER.error(LoggerMessages.RESP_PERS_WITH_UUID + respPersonDetails.getUuidRespPerson() + LoggerMessages.NOT_FOUND);
            throw new UsernameNotFoundException(
                    ErrorMessages.RESP_PERS_WITH_UUID + respPersonDetails.getUuidRespPerson() + ErrorMessages.NOT_FOUND);
        }
        ResponsePerson responsePerson = optionalResponsePerson.get();
        if(respPersonDetails.getEmailRespPerson() != null){
            responsePerson.setEmailRespPerson(respPersonDetails.getEmailRespPerson());
            LOGGER.info(LoggerMessages.EMAIL_FOR + LoggerMessages.RESP_PERS_WITH_UUID + LoggerMessages.WAS_UPDATE+
                    LoggerMessages.TO + respPersonDetails.getEmailRespPerson() );
        }
        if(respPersonDetails.getFirstName() != null){
            responsePerson.setFirstName(respPersonDetails.getFirstName());
            LOGGER.info(LoggerMessages.FIRST_NAME + LoggerMessages.RESP_PERS_WITH_UUID + LoggerMessages.WAS_UPDATE+
                    LoggerMessages.TO + respPersonDetails.getFirstName() );
        }
        if(respPersonDetails.getSecondName() != null){
            responsePerson.setSecondName(respPersonDetails.getSecondName());
            LOGGER.info(LoggerMessages.SECOND_NAME + LoggerMessages.RESP_PERS_WITH_UUID + LoggerMessages.WAS_UPDATE+
                    LoggerMessages.TO + respPersonDetails.getSecondName());
        }
        if(respPersonDetails.getTzRespPers() != null){
            responsePerson.setTz(respPersonDetails.getTzRespPers());
            LOGGER.info(LoggerMessages.TZ + LoggerMessages.RESP_PERS_WITH_UUID + LoggerMessages.WAS_UPDATE+
                    LoggerMessages.TO + respPersonDetails.getTzRespPers());
        }
        respPersonRepo.save(responsePerson);
        LOGGER.info(LoggerMessages.RESP_PERS_WITH_UUID + responsePerson.getUuidRespPerson() +
                LoggerMessages.WAS_UPDATE + LoggerMessages.TO + responsePerson);
        return modelMapper.map(responsePerson, RepsoncePersonDto.class);
    }

    @Override
    public RepsoncePersonDto getRespPersonByUuid(String uuidRespPerson) {
        Optional<ResponsePerson> optionalResponsePerson = respPersonRepo
                .findByUuidRespPersonAndDeleted(uuidRespPerson, false);
        if (!optionalResponsePerson.isPresent()) {
            LOGGER.error(LoggerMessages.RESP_PERS_WITH_UUID + uuidRespPerson + LoggerMessages.NOT_FOUND);
            throw new UsernameNotFoundException(
                    ErrorMessages.RESP_PERS_WITH_UUID + uuidRespPerson + ErrorMessages.NOT_FOUND);
        }
        LOGGER.info(LoggerMessages.RESP_PERS_WITH_UUID + uuidRespPerson + LoggerMessages.WAS_RETURND);
        return modelMapper.map(optionalResponsePerson.get(), RepsoncePersonDto.class);
    }

    @Override
    public Boolean deleteResponsePerson(String uuidRespPerson) {
        Optional<ResponsePerson> optionalResponsePerson = respPersonRepo
                .findByUuidRespPersonAndDeleted(uuidRespPerson, false);
        if (!optionalResponsePerson.isPresent()) {
            LOGGER.error(LoggerMessages.RESP_PERS_WITH_UUID + uuidRespPerson + LoggerMessages.NOT_FOUND);
            throw new UsernameNotFoundException(
                    ErrorMessages.RESP_PERS_WITH_UUID + uuidRespPerson + ErrorMessages.NOT_FOUND);
        }
        ResponsePerson responsePerson = optionalResponsePerson.get();
        responsePerson.setDeleted(true);
        respPersonRepo.save(responsePerson);
        LOGGER.info(LoggerMessages.RESP_PERS_WITH_UUID + uuidRespPerson + LoggerMessages.WAS_DELETED);
        return true;
    }

    @Override
    public List<RespPersonResponseDto> getAllResponsePerson(int page, int limit) {
        if (page > 0) page = page - 1;
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<ResponsePerson> responsePersonPage = respPersonRepo.findAll(pageableRequest);
        List<ResponsePerson> responsePersons = responsePersonPage.getContent();
        List<RespPersonResponseDto> returnValue = new ArrayList<>();
        responsePersons.stream().filter(user -> !user.getDeleted()).map(this::toDto)
                .forEachOrdered(returnValue::add);
        LOGGER.info(LoggerMessages.LIST_OF_RESPONSE_PERSON + LoggerMessages.RETURNED + responsePersons.toString());
        return returnValue;
    }


    private RespPersonResponseDto toDto(ResponsePerson responsePerson) {
        return modelMapper.map(responsePerson, RespPersonResponseDto.class);
    }

}
