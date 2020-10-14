package com.tsofim.servicers.parser;

import org.springframework.stereotype.Service;

@Service
public interface TsofimCrawlerService {
    String sendFormToTsofim(String uuidChild);
}
