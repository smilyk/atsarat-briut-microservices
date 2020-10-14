package com.tsofim.controler;

import com.google.common.reflect.TypeToken;
import com.tsofim.dto.*;
import com.tsofim.enums.RequestOperationName;
import com.tsofim.enums.RequestOperationStatus;
import com.tsofim.servicers.details.TsofimDetailsService;
import com.tsofim.servicers.parser.TsofimCrawlerService;
import com.tsofim.servicers.validators.ValidatorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tsofiml/v1")
public class TsofimController {

    private static final String NOT_FOUND_STRING = " Can`t find record for child with uuid: ";
    private String currentDate = LocalDateTime.now().toLocalDate().toString();
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    TsofimCrawlerService tsofimCrawlerService;
    @Autowired
    TsofimDetailsService tsofimDetailsService;
    @Autowired
    ValidatorService validatorService;

    @PostMapping()
    public Response createChildDetails(@Valid @RequestBody AddTsofimDetailsDto tsofimDetails) {
        validatorService.checkChildUuidUnique(tsofimDetails.getUuidChild());
        return tsofimDetailsService.addTsofimDetails(tsofimDetails);
    }
//
    @PutMapping()
    public Response updateChildDetails(@Valid @RequestBody UpdateTsofimDetailDto tsofimDetails) {
        ResponseTsofimDetails updateChildDetails = tsofimDetailsService.updateTsofimDetails(tsofimDetails);
        if(updateChildDetails == null){
            return new Response(NOT_FOUND_STRING + tsofimDetails.getUuidChild(),
                    HttpServletResponse.SC_NO_CONTENT, currentDate);
        }
        return new Response(updateChildDetails, HttpServletResponse.SC_FOUND, currentDate);
    }

    @GetMapping(path = "/{uuidTsofimDetails}")
    public Response getChildDetailsByUuid(@PathVariable String uuidTsofimDetails) {
        ResponseTsofimDetails responseChildDetails = tsofimDetailsService.getTsofimDetaildByUuid(uuidTsofimDetails);
        if(responseChildDetails == null){
            return new Response(NOT_FOUND_STRING + uuidTsofimDetails,
                    HttpServletResponse.SC_NO_CONTENT, currentDate);
        }
        return new Response(responseChildDetails, HttpServletResponse.SC_FOUND, currentDate);
    }

    @GetMapping(path = "child/{uuidChild}")
    public Response getChildDetailsByChildUuid(@PathVariable String uuidChild) {
        ResponseTsofimDetails responseChildDetails = tsofimDetailsService.getChildDetailsByChildUuid(uuidChild);
        if(responseChildDetails == null){
            return new Response(NOT_FOUND_STRING + uuidChild,
                    HttpServletResponse.SC_NO_CONTENT, currentDate);
        }
    return new Response(responseChildDetails, HttpServletResponse.SC_FOUND, currentDate);
    }

    @DeleteMapping(path = "/{uuidChildDetails}")
    public Response deleteResponsePerson(@PathVariable String uuidChildDetails) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        Boolean deleted = tsofimDetailsService.deleteChildDetails(uuidChildDetails);
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
        List<ResponseTsofimDetails> childDetails = tsofimDetailsService.getAllTsofimDetails(page, limit);
        Type listType = new TypeToken<List<ResponseTsofimDetails>>() {
        }.getType();
        List<ResponseTsofimDetails> returnValue = new ModelMapper().map(childDetails, listType);
        return new Response(returnValue, HttpServletResponse.SC_FOUND, currentDate);
    }

//parsing
//
//    @GetMapping("/parse")
//    public String parseSchool()  {
//       return tsofimCrawlerService.sendFormToTsofim(uuidChild);
//    }
}
