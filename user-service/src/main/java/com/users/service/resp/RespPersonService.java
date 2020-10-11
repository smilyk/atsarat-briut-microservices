package com.users.service.resp;

import com.users.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RespPersonService {

    Response addResponsePerson(AddRespPersonDto respPersonDetails);

    RepsoncePersonDto getRespPersonByUuid(String uuidRespPerson);

    Boolean deleteResponsePerson(String id);

    List<RespPersonResponseDto> getAllResponsePerson(int page, int limit);

    RepsoncePersonDto updateResponsePerson(RepsoncePersonDto respPersonDetails);
}
