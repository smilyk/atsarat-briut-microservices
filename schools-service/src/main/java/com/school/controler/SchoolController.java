package com.school.controler;

import com.google.common.reflect.TypeToken;
import com.school.dto.AddChildDetailsDto;
import com.school.dto.OperationStatusModel;
import com.school.dto.Response;
import com.school.dto.ResponseChildDetails;
import com.school.enums.RequestOperationName;
import com.school.enums.RequestOperationStatus;
import com.school.services.parser.SchoolCrawlerService;
import com.school.services.usersDetails.UserDetailsService;
import com.school.services.validator.ValidatorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/school/v1")
public class SchoolController {

    private static final String NOT_FOUND_STRING = " Can`t find record for child with uuid: ";
    private String currentDate = LocalDateTime.now().toLocalDate().toString();
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    SchoolCrawlerService schoolCrawlerService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    ValidatorService validatorService;

    @PostMapping()
    public Response createChildDetails(@Valid @RequestBody AddChildDetailsDto childDetails) {
        validatorService.checkChildUuidUnique(childDetails.getUuidChild());
        return userDetailsService.addChildDetails(childDetails);
    }
//
    @PutMapping()
    public Response updateChildDetails(@Valid @RequestBody AddChildDetailsDto childDetails) {
        ResponseChildDetails updateChildDetails = userDetailsService.updateChildDetails(childDetails);
        if(updateChildDetails == null){
            return new Response(NOT_FOUND_STRING + childDetails.getUuidChild(),
                    HttpServletResponse.SC_NO_CONTENT, currentDate);
        }
        return new Response(updateChildDetails, HttpServletResponse.SC_FOUND, currentDate);
    }

    @GetMapping(path = "/{uuidChildDetails}")
    public Response getChildDetailsByUuid(@PathVariable String uuidChildDetails) {
        ResponseChildDetails responseChildDetails = userDetailsService.getChildDetailsByUuid(uuidChildDetails);
        if(responseChildDetails == null){
            return new Response(NOT_FOUND_STRING + uuidChildDetails,
                    HttpServletResponse.SC_NO_CONTENT, currentDate);
        }
        return new Response(responseChildDetails, HttpServletResponse.SC_FOUND, currentDate);
    }

    @GetMapping(path = "child/{uuidChildDetails}")
    public Response getChildDetailsByChildUuid(@PathVariable String uuidChildDetails) {
        ResponseChildDetails responseChildDetails = userDetailsService.getChildDetailsByChildUuid(uuidChildDetails);
        if(responseChildDetails == null){
            return new Response(NOT_FOUND_STRING + uuidChildDetails,
                    HttpServletResponse.SC_NO_CONTENT, currentDate);
        }
    return new Response(responseChildDetails, HttpServletResponse.SC_FOUND, currentDate);
    }

    @DeleteMapping(path = "/{uuidChildDetails}")
    public Response deleteResponsePerson(@PathVariable String uuidChildDetails) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        Boolean deleted = userDetailsService.deleteChildDetails(uuidChildDetails);
        if(deleted){
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }else{
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }
        return new Response(returnValue, HttpServletResponse.SC_OK, currentDate);
    }

    @GetMapping()
    public Response getAllChildDetails(@RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<ResponseChildDetails> respPersosons = userDetailsService.getAllchildDetails(page, limit);
        Type listType = new TypeToken<List<ResponseChildDetails>>() {
        }.getType();
        List<ResponseChildDetails> returnValue = new ModelMapper().map(respPersosons, listType);
        return new Response(returnValue, HttpServletResponse.SC_FOUND, currentDate);
    }

//parsing

    @GetMapping("/parse")
    public String parseSchool()  {
       return schoolCrawlerService.sendFormToSchool();
    }
}
