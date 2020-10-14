package com.gymnast.services.parseService;

import org.springframework.stereotype.Service;

@Service
public interface GymnastCrawlerService {
    String sendFormToGymnast(String uuidChild);
}

