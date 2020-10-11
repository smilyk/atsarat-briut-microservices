package com.children.services.validator;

import org.springframework.stereotype.Service;

@Service
public interface ValidatorService {
    Boolean checkUniqueTZ(String tz);
}
