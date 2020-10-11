package com.users.service.validatorService;

import org.springframework.stereotype.Service;

@Service
public interface ValidatorService {

   Boolean checkUniqueEmail(String eMail);

    void checkRespPersonUniqueEmail(String emailRespPerson);
}
