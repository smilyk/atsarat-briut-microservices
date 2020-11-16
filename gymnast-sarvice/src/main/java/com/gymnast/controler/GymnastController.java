package com.gymnast.controler;

import com.gymnast.services.parseService.GymnastCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gymnast/v1")
public class GymnastController {
    @Autowired
    GymnastCrawlerService gymnastCrawlerService;

    @GetMapping("/parse/{uuidChild}")

    public String parseShop(@PathVariable String uuidChild){
        return gymnastCrawlerService.sendFormToGymnast(uuidChild);
    }
}
