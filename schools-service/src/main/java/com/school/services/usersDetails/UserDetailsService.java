package com.school.services.usersDetails;

import com.school.dto.AddChildDetailsDto;
import com.school.dto.Response;
import com.school.dto.ResponseChildDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserDetailsService {
    Response addChildDetails(AddChildDetailsDto childDetails);

    ResponseChildDetails updateChildDetails(AddChildDetailsDto childDetails);

    ResponseChildDetails getChildDetailsByUuid(String uuidChildDetails);

    ResponseChildDetails getChildDetailsByChildUuid(String uuidChildDetails);

    Boolean deleteChildDetails(String uuidChildDetails);

    List<ResponseChildDetails> getAllchildDetails(int page, int limit);
}
