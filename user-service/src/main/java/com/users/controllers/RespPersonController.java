package com.users.controllers;

import com.users.dto.*;
import com.users.enums.RequestOperationName;
import com.users.enums.RequestOperationStatus;
import com.users.service.resp.RespPersonServiceImpl;
import com.users.service.validatorService.ValidatorServiceImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/resp_pers/v1")
public class RespPersonController {
    private String currentDate = LocalDateTime.now().toLocalDate().toString();

    @Autowired
    ValidatorServiceImpl validatorService;
    @Autowired
    RespPersonServiceImpl respPerson;

    @PostMapping()
    public Response createRespPerson(@Valid @RequestBody AddRespPersonDto respPersonDetails) {
        validatorService.checkRespPersonUniqueEmail(respPersonDetails.getEmailRespPerson());
        return respPerson.addResponsePerson(respPersonDetails);
    }

    @PutMapping()
    public Response updateRespPerson(@Valid @RequestBody RepsoncePersonDto respPersonDetails) {
        RepsoncePersonDto updatedPerson = respPerson.updateResponsePerson(respPersonDetails);
        return new Response(updatedPerson, HttpServletResponse.SC_FOUND, currentDate);
    }

    @GetMapping(path = "/{uuidRespPerson}")
    public Response getResponsePersonByUuid(@PathVariable String uuidRespPerson) {
        RepsoncePersonDto repsoncePersonDto = respPerson.getRespPersonByUuid(uuidRespPerson);
        return new Response(repsoncePersonDto, HttpServletResponse.SC_FOUND, currentDate);
    }

    @DeleteMapping(path = "/{uuidRespPerson}")
    public Response deleteResponsePerson(@PathVariable String uuidRespPerson) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        Boolean deleted = respPerson.deleteResponsePerson(uuidRespPerson);
        if(deleted){
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }else{
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }
        return new Response(returnValue, HttpServletResponse.SC_OK, currentDate);
    }


    @GetMapping()
    public Response getAllResponsePersons(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "2") int limit) {
        List<RespPersonResponseDto> respPersosons = respPerson.getAllResponsePerson(page, limit);
        Type listType = new TypeToken<List<RespPersonResponseDto>>() {
        }.getType();
        List<RespPersonResponseDto> returnValue = new ModelMapper().map(respPersosons, listType);
        return new Response(returnValue, HttpServletResponse.SC_FOUND, currentDate);
    }


}
