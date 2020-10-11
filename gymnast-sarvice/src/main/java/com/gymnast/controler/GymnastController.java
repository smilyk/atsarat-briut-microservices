package com.gymnast.controler;

import com.gymnast.services.parse.GymnastCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gymnast/v1")
public class GymnastController {
    @Autowired
    GymnastCrawlerService gymnastCrawlerService;

    @GetMapping()
    public String parseShop(){
       return gymnastCrawlerService.sendFormToGymnast();
    }
}
