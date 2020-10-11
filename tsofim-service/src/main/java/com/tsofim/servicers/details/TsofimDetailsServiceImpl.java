package com.tsofim.servicers.details;

import com.tsofim.dto.AddTsofimDetailsDto;
import com.tsofim.dto.Response;
import com.tsofim.dto.ResponseTsofimDetails;
import com.tsofim.dto.UpdateTsofimDetailDto;
import com.tsofim.entity.TsofimDetails;
import com.tsofim.enums.LoggerMessages;
import com.tsofim.repository.TsofimDetailsRepo;
import com.tsofim.utils.UserUtils;
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
public class TsofimDetailsServiceImpl implements TsofimDetailsService {
    ModelMapper modelMapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(TsofimDetailsServiceImpl.class);
    private String currentDate = LocalDateTime.now().toLocalDate().toString();

    @Autowired
    TsofimDetailsRepo tsofimDetailsRepo;
    @Autowired
    UserUtils utils;
    @Value("secretPassword")
    String secretWord;
    @Override
    public Response addTsofimDetails(AddTsofimDetailsDto tsofimDetails) {
        TsofimDetails tsofimDetailsEntity = modelMapper.map(tsofimDetails, TsofimDetails.class);
        tsofimDetailsEntity.setUuidTsofimDetails(utils.generateUuid().toString());
        tsofimDetailsRepo.save(tsofimDetailsEntity);
        LOGGER.info(LoggerMessages.CHILD + LoggerMessages.WITH_UUID + LoggerMessages.WAS_SAVE);
        ResponseTsofimDetails responseTsofimDetails = modelMapper.map(tsofimDetailsEntity, ResponseTsofimDetails.class);
        return new Response(responseTsofimDetails, HttpServletResponse.SC_CREATED, currentDate);
    }

    @Override
    public ResponseTsofimDetails updateTsofimDetails(UpdateTsofimDetailDto tsofimDetails) {
        Optional<TsofimDetails> optionalTsofimDetails = tsofimDetailsRepo.findByUuidChildAndDeleted(tsofimDetails.getUuidChild(),
                false);
        if(!optionalTsofimDetails.isPresent()){
            LOGGER.info(LoggerMessages.CHILD + LoggerMessages.WITH_UUID + tsofimDetails.getUuidChild() +
                    LoggerMessages.NOT_FOUND);
            return null;
        }
        TsofimDetails tsofimDetailsEntity = optionalTsofimDetails.get();
        if(!tsofimDetails.getGroupTs().isEmpty()){
            tsofimDetailsEntity.setGroupTs(tsofimDetails.getGroupTs());
            LOGGER.info(LoggerMessages.TSOFIM_DETAILS + LoggerMessages.WITH_UUID + tsofimDetails.getUuidChild() +
                    LoggerMessages.WAS_UPDATE +
                    ": new Group " + tsofimDetails.getGroupTs());
        }
        if(!tsofimDetails.getPlace().isEmpty()){
            tsofimDetailsEntity.setPlace(tsofimDetails.getPlace());
            LOGGER.info(LoggerMessages.TSOFIM_DETAILS + LoggerMessages.WITH_UUID + tsofimDetails.getUuidChild() +
                    LoggerMessages.WAS_UPDATE +
                    ": new Place " + tsofimDetails.getPlace());
        }
        if(!tsofimDetails.getSchool().isEmpty()){
            tsofimDetailsEntity.setSchool(tsofimDetails.getSchool());
            LOGGER.info(LoggerMessages.TSOFIM_DETAILS + LoggerMessages.WITH_UUID + tsofimDetails.getUuidChild() +
                    LoggerMessages.WAS_UPDATE +
                    ": new School " + tsofimDetails.getSchool());
        }
        if(!tsofimDetails.getChildClass().isEmpty()){
            tsofimDetailsEntity.setChildClass(tsofimDetails.getChildClass());
            LOGGER.info(LoggerMessages.TSOFIM_DETAILS + LoggerMessages.WITH_UUID + tsofimDetails.getUuidChild() +
                    LoggerMessages.WAS_UPDATE +
                    ": new School " + tsofimDetails.getChildClass());
        }
        tsofimDetailsRepo.save(tsofimDetailsEntity);
        LOGGER.info(LoggerMessages.TSOFIM_DETAILS + LoggerMessages.WITH_UUID + tsofimDetails.getUuidChild() +
                LoggerMessages.WAS_UPDATE );
        return modelMapper.map(tsofimDetailsEntity, ResponseTsofimDetails.class);
    }

    @Override
    public ResponseTsofimDetails getTsofimDetaildByUuid(String uuidTsofimDetails) {
        Optional<TsofimDetails> optionalTsofimDetails = tsofimDetailsRepo.findByUuidTsofimDetailsAndDeleted(uuidTsofimDetails,
                false);
        if(!optionalTsofimDetails.isPresent()){
            LOGGER.info(LoggerMessages.TSOFIM_DETAILS + LoggerMessages.WITH_UUID + uuidTsofimDetails +
                    LoggerMessages.NOT_FOUND);
            return null;
        }
        return modelMapper.map(optionalTsofimDetails.get(), ResponseTsofimDetails.class);
    }

    @Override
    public ResponseTsofimDetails getChildDetailsByChildUuid(String uuidChild) {
        Optional<TsofimDetails> optionalTsofimDetails = tsofimDetailsRepo.findByUuidChildAndDeleted(uuidChild,
                false);
        if(!optionalTsofimDetails.isPresent()){
            LOGGER.info(LoggerMessages.TSOFIM_DETAILS + LoggerMessages.WITH_UUID + uuidChild +
                    LoggerMessages.NOT_FOUND);
            return null;
        }
        return modelMapper.map(optionalTsofimDetails.get(), ResponseTsofimDetails.class);
    }

    @Override
    public Boolean deleteChildDetails(String uuidTsofimDetails) {
        Optional<TsofimDetails> optionalTsofimDetails = tsofimDetailsRepo.findByUuidTsofimDetailsAndDeleted(uuidTsofimDetails,
                false);
        if (!optionalTsofimDetails.isPresent()) {
            LOGGER.info(LoggerMessages.TSOFIM_DETAILS + LoggerMessages.WITH_UUID + uuidTsofimDetails +
                    LoggerMessages.NOT_FOUND);
            return false;
        }
        TsofimDetails tsofimDetails = optionalTsofimDetails.get();
        tsofimDetails.setDeleted(true);
        tsofimDetailsRepo.save(tsofimDetails);
        LOGGER.info(LoggerMessages.TSOFIM_DETAILS + LoggerMessages.WITH_UUID + uuidTsofimDetails +
                LoggerMessages.WAS_DELETED);
        return true;
    }

    @Override
    public List<ResponseTsofimDetails> getAllTsofimDetails(int page, int limit) {
        if (page > 0) page = page - 1;
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<TsofimDetails> usersPage = tsofimDetailsRepo.findAll(pageableRequest);
        List<TsofimDetails> tsofimDetails = usersPage.getContent();
        List<ResponseTsofimDetails> returnValue = new ArrayList<>();
        tsofimDetails.stream().filter(tsofim -> !tsofim.getDeleted()).map(this::toDto)
                .forEachOrdered(returnValue::add);
        LOGGER.info(LoggerMessages.LIST_OF_TSOFIM_DETAILS + LoggerMessages.RETURNED);
        return returnValue;
    }

    private ResponseTsofimDetails toDto(TsofimDetails tsofimDetails) {
        return modelMapper.map(tsofimDetails,ResponseTsofimDetails.class);
    }
}
