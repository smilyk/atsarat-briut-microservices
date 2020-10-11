package com.children.services.children;

import com.children.dto.AddChildDto;
import com.children.dto.Response;
import com.children.dto.ResponseChildDto;
import com.children.dto.UpdateChildDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChildService {
    Response addChild(AddChildDto childDetails);

    ResponseChildDto getChildByUuid(String uuidChild);

//    ResponseChildDto checkChildProfile(String uuidChild);

    UpdateChildDto updateChild(String uuidChild, UpdateChildDto childDetails);

    Boolean deleteChild(String uuidChild);

    List<ResponseChildDto> getAllChilds(int page, int limit);
}
