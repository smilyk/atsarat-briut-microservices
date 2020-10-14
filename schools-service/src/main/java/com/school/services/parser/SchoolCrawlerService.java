package com.school.services.parser;

import org.springframework.stereotype.Service;

@Service
public interface SchoolCrawlerService {
    String sendFormToSchool(String uuidChild);
}
