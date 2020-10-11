package com.children.controller;

import com.children.dto.*;
import com.children.enums.RequestOperationName;
import com.children.enums.RequestOperationStatus;
import com.children.services.children.ChildServiceImpl;
import com.children.services.validator.ValidatorService;
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
@RequestMapping("/child/v1")
public class ChildController {
    private String currentDate = LocalDateTime.now().toLocalDate().toString();
    @Autowired
    ValidatorService validatorService;
    @Autowired
    ChildServiceImpl childService;

    @PostMapping()
    public Response createChild(@Valid @RequestBody AddChildDto childDetails) {
        validatorService.checkUniqueTZ(childDetails.getTz());
        return childService.addChild(childDetails);
    }


    @GetMapping(path = "/{uuidChild}")
    public Response getChild(@PathVariable String uuidChild) {
        ResponseChildDto childDto = childService.getChildByUuid(uuidChild);
        return new Response(childDto, HttpServletResponse.SC_FOUND, currentDate);
    }

//    @GetMapping(path = "/check/{uuidChild}")
//    public Response checkChildProfile(@PathVariable String uuidChild){
//        ResponseChildDto checkChidProfile = childService.checkChildProfile(uuidChild);
//        return new Response(checkChidProfile,HttpServletResponse.SC_OK, currentDate);
//    }

    @PutMapping(path = "/{uuidChild}")
    public Response updateChild(@PathVariable String uuidChild,  @Valid @RequestBody UpdateChildDto childDetails) {
        UpdateChildDto updateChild = childService.updateChild(uuidChild, childDetails);
        return new Response(updateChild,HttpServletResponse.SC_OK, currentDate);
    }

    @DeleteMapping(path = "/{uuidChild}")
    public Response deleteChild(@PathVariable String uuidChild) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        Boolean deleted = childService.deleteChild(uuidChild);
        if(deleted){
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }else{
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }
        return new Response(returnValue, HttpServletResponse.SC_OK, currentDate);
    }


    @GetMapping()
    public Response getAllChildes(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "limit", defaultValue = "2") int limit) {
        List<ResponseChildDto> childs = childService.getAllChilds(page, limit);
        Type listType = new TypeToken<List<ResponseChildDto>>() {
        }.getType();
        List<ResponseChildDto> returnValue = new ModelMapper().map(childs, listType);
        return new Response(returnValue, HttpServletResponse.SC_FOUND, currentDate);
    }

}
