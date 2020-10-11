package com.tsofim.servicers.details;

import com.tsofim.dto.AddTsofimDetailsDto;
import com.tsofim.dto.Response;
import com.tsofim.dto.ResponseTsofimDetails;
import com.tsofim.dto.UpdateTsofimDetailDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TsofimDetailsService {
    Response addTsofimDetails(AddTsofimDetailsDto tsofimDetails);

    ResponseTsofimDetails updateTsofimDetails(UpdateTsofimDetailDto tsofimDetails);

    ResponseTsofimDetails getTsofimDetaildByUuid(String uuidTsofimDetails);

    ResponseTsofimDetails getChildDetailsByChildUuid(String uuidChild);

    Boolean deleteChildDetails(String uuidChildDetails);

    List<ResponseTsofimDetails> getAllTsofimDetails(int page, int limit);
}
