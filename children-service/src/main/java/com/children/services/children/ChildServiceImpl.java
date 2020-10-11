package com.children.services.children;

import com.children.dto.AddChildDto;
import com.children.dto.Response;
import com.children.dto.ResponseChildDto;
import com.children.dto.UpdateChildDto;
import com.children.entity.ChildrenEntity;
import com.children.enums.ErrorMessages;
import com.children.enums.LoggerMessages;
import com.children.exception.ChildrenServiceException;
import com.children.repo.ChildRepo;
import com.children.utils.ChildUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ChildServiceImpl implements ChildService {
    private String currentDate = LocalDateTime.now().toLocalDate().toString();
    ModelMapper modelMapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(ChildServiceImpl.class);
    @Autowired
    ChildRepo childRepo;
    @Autowired
    ChildUtils utils;

    @Override
    public Response addChild(AddChildDto childDetails) {

        ChildrenEntity childrenEntity = modelMapper.map(childDetails, ChildrenEntity.class);
        childrenEntity.setUuidChild(utils.generateUserId().toString());
        ResponseChildDto responseChildDto = modelMapper.map(childrenEntity, ResponseChildDto.class);
        childRepo.save(childrenEntity);
        LOGGER.info(LoggerMessages.CHILD + LoggerMessages.FIRST_NAME + childrenEntity.getFirstName() +
         LoggerMessages.SECOND_NAME + childrenEntity.getSecondName() + LoggerMessages.SAVED);
        return  new Response(responseChildDto, HttpServletResponse.SC_CREATED, currentDate);
    }

    @Override
    public ResponseChildDto getChildByUuid(String uuidChild) {
        Optional<ChildrenEntity> optionalChildrenEntity = childRepo.findByUuidChildAndDeleted(uuidChild, false);
        if (!optionalChildrenEntity.isPresent()) {
            LOGGER.error(LoggerMessages.CHILD_WITH_UUID  + uuidChild + LoggerMessages.NOT_FOUND);
            throw new ChildrenServiceException(
                    ErrorMessages.USER_WITH_UUID + uuidChild + ErrorMessages.NOT_FOUND);
        }
        LOGGER.info(LoggerMessages.CHILD_WITH_UUID + uuidChild + LoggerMessages.WAS_RETURND);
        return modelMapper.map(optionalChildrenEntity.get(), ResponseChildDto.class);
    }

//    @Override
//    public ResponseChildDto checkChildProfile(String uuidChild) {
//        Optional<ChildrenEntity> optionalChildrenEntity = childRepo.findByUuidChildAndDeleted(uuidChild, false);
//        if (!optionalChildrenEntity.isPresent()) {
//            LOGGER.error(LoggerMessages.CHILD_WITH_UUID  + uuidChild + LoggerMessages.NOT_FOUND);
//            throw new ChildrenServiceException(
//                    ErrorMessages.USER_WITH_UUID + uuidChild + ErrorMessages.NOT_FOUND);
//        }
//        ChildrenEntity childrenEntity = optionalChildrenEntity.get();
//        if(childrenEntity.getUuidParent() == null || childrenEntity.getUuidRespPers() == null){
//
//        }
//        return null;
//    }

    @Override
    public UpdateChildDto updateChild(String uuidChild, UpdateChildDto childDetails) {
        Optional<ChildrenEntity> optionalChildrenEntity = childRepo.findByUuidChildAndDeleted(uuidChild, false);
        if (!optionalChildrenEntity.isPresent()) {
            LOGGER.error(LoggerMessages.CHILD_WITH_UUID  + uuidChild + LoggerMessages.NOT_FOUND);
            throw new ChildrenServiceException(
                    ErrorMessages.USER_WITH_UUID + uuidChild + ErrorMessages.NOT_FOUND);
        }
        ChildrenEntity childrenEntity = optionalChildrenEntity.get();
        if(childDetails.getFirstName() != null){
            childrenEntity.setFirstName(childDetails.getFirstName());
            LOGGER.info(LoggerMessages.CHILD_WITH_UUID + uuidChild + LoggerMessages.CHANGE + LoggerMessages.FIRST_NAME
             + LoggerMessages.TO + childDetails.getFirstName());
        }
        if(childDetails.getSecondName() != null){
            childrenEntity.setSecondName(childDetails.getSecondName());
            LOGGER.info(LoggerMessages.CHILD_WITH_UUID + uuidChild + LoggerMessages.CHANGE + LoggerMessages.SECOND_NAME
                    + LoggerMessages.TO + childDetails.getSecondName());
        }
        if(childDetails.getTz() != null){
            childrenEntity.setTz(childDetails.getTz());
            LOGGER.info(LoggerMessages.CHILD_WITH_UUID + uuidChild + LoggerMessages.CHANGE + LoggerMessages.PROVIDED_TZ);
        }
        if(childDetails.getUuidParent() != null){
            childrenEntity.setUuidParent(childDetails.getUuidParent());
            LOGGER.info(LoggerMessages.CHILD_WITH_UUID + uuidChild + LoggerMessages.CHANGE + LoggerMessages.PARENT
                    + LoggerMessages.TO + LoggerMessages.PARENT + LoggerMessages.WITH_UUID +  childDetails.getUuidParent());
        }
        if(childDetails.getUuidRespPers() != null){
            childrenEntity.setUuidRespPers(childDetails.getUuidRespPers());
            LOGGER.info(LoggerMessages.CHILD_WITH_UUID + uuidChild + LoggerMessages.CHANGE + LoggerMessages.RESP_PERS
                    + LoggerMessages.TO + LoggerMessages.RESP_PERS + LoggerMessages.WITH_UUID +  childDetails.getUuidParent());
        }
        childRepo.save(childrenEntity);
        LOGGER.info( LoggerMessages.CHILD_WITH_UUID + uuidChild + LoggerMessages.WAS_UPDATE);
        return modelMapper.map(childrenEntity, UpdateChildDto.class);
    }

    @Override
    public Boolean deleteChild(String uuidChild) {
        Optional<ChildrenEntity> optionalChildrenEntity = childRepo.findByUuidChildAndDeleted(uuidChild, false);
        if (!optionalChildrenEntity.isPresent()) {
            LOGGER.error(LoggerMessages.CHILD_WITH_UUID  + uuidChild + LoggerMessages.NOT_FOUND);
            throw new ChildrenServiceException(
                    ErrorMessages.USER_WITH_UUID + uuidChild + ErrorMessages.NOT_FOUND);
        }
        ChildrenEntity childrenEntity = optionalChildrenEntity.get();
        childrenEntity.setDeleted(true);
        childRepo.save(childrenEntity);
        LOGGER.info(LoggerMessages.USER_WITH_UUID + uuidChild + LoggerMessages.DELETED);
        return true;
    }

    @Override
    public List<ResponseChildDto> getAllChilds(int page, int limit) {
        if (page > 0) page = page - 1;
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<ChildrenEntity> childesPage = childRepo.findAll(pageableRequest);
        List<ChildrenEntity> childes = childesPage.getContent();
        List<ResponseChildDto> returnValue = new ArrayList<>();
        childes.stream().filter(child -> !child.getDeleted()).map(this::toDto)
                .forEachOrdered(returnValue::add);
        return returnValue;
    }

    private ResponseChildDto toDto(ChildrenEntity child) {
        return modelMapper.map(child, ResponseChildDto.class);
    }

}
