package com.tsofim.servicers.validators;

import org.springframework.stereotype.Service;

@Service
public interface ValidatorService {
    public void checkChildUuidUnique(String uuidChild);
}
