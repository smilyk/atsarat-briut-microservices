package com.gymnast.services.parseService;

import com.gymnast.dto.EmailDto;
import com.gymnast.dto.Response;
import com.gymnast.services.hystrix.child.ChildHystrixDto;
import com.gymnast.services.hystrix.child.ChildServiceClient;
import com.gymnast.services.hystrix.user.parent.UserHystrixDto;
import com.gymnast.services.hystrix.user.parent.UserServiceClient;
import com.gymnast.services.hystrix.user.respPerson.RespPersonHystrixDto;
import com.gymnast.services.hystrix.user.respPerson.RespPersonServiceClient;
import com.gymnast.services.rabbitService.RabbitService;
import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;

@Service
@RefreshScope
public class GymnastCrawlerServiceImpl implements GymnastCrawlerService {
    @Value("${url.gymnast}")
    private String gymnastUrl;
    @Value("${service}")
    private String service;
    @Value("authorization.token.header.prefix")
    private String tokenPrefix;
    @Value("admin.token")
    private String adminToken;

    @Autowired
    ChildServiceClient childHystrix;
    @Autowired
    UserServiceClient userHystrix;
    @Autowired
    RespPersonServiceClient respPersonHystrix;

    @Autowired
    RabbitService rabbitService;

    ModelMapper modelMapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(GymnastCrawlerServiceImpl.class);
    LocalDate start = LocalDate.now();
    String TOKEN = tokenPrefix + " " + adminToken;

    @Override
    public String sendFormToGymnast(String uuidChild) {
        Response childFromHystrix = this.childHystrix.getChildByChildUuid(uuidChild, TOKEN);
        ChildHystrixDto child = modelMapper.map(childFromHystrix.getContent(), ChildHystrixDto.class);
        String childFirstNAme = child.getFirstName();
        String childSecondName = child.getSecondName();
        String childTZ = child.getTz();
        String childParentUuid = child.getUuidParent();
        String respPersonUuid = child.getUuidRespPers();
        RespPersonHystrixDto respPerson = new RespPersonHystrixDto();
        UserHystrixDto parent = new UserHystrixDto();
        String parentFirstName;
        String parentSecondName;
        String parentTZ;
        String email;
        if (respPersonUuid != null) {
            Response respPersonFromHystrix = respPersonHystrix.getResponsePersonByUserUuid(respPersonUuid, TOKEN);
            respPerson = modelMapper.map(respPersonFromHystrix.getContent(), RespPersonHystrixDto.class);
            parentFirstName = respPerson.getFirstName();
            parentSecondName = respPerson.getSecondName();
            parentTZ = respPerson.getTzRespPers();
            email = respPerson.getEmailRespPerson();
        } else {
            userHystrix.getUserByUserUuid(childParentUuid, TOKEN);
            Response parentFromHystrix = userHystrix.getUserByUserUuid(childParentUuid, TOKEN);
            parent = modelMapper.map(parentFromHystrix.getContent(), UserHystrixDto.class);
            parentFirstName = parent.getFirstName();
            parentSecondName = parent.getSecondName();
            parentTZ = parent.getTz();
            if (parent.getAltEmail() == null) {
                email = parent.getMainEmail();
            } else {
                email = parent.getAltEmail();
            }
        }

        WebDriver driver = getWebDriver();
        WebElement childName = driver.findElement(By.xpath("//body/div[1]/div[2]/form[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]"));
        childName.sendKeys(childFirstNAme + " " + childSecondName);
        WebElement tzChild = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]/form[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]"));
        tzChild.sendKeys(childTZ);
        WebElement parentName = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]/form[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]"));
        parentName.sendKeys(parentFirstName + " " + parentSecondName);
//       first check
        driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]/form[1]/div[2]/div[1]/div[2]/div[4]/div[1]/div[1]/div[2]/div[1]/div[1]/label[1]/div[1]/div[1]/div[2]")).click();
//       second check
        driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]/form[1]/div[2]/div[1]/div[2]/div[5]/div[1]/div[1]/div[2]/div[1]/div[1]/label[1]/div[1]/div[1]/div[2]")).click();
//       third check
        driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]/form[1]/div[2]/div[1]/div[2]/div[6]/div[1]/div[1]/div[2]/div[1]/div[1]/label[1]/div[1]/div[1]/div[2]")).click();
//       button
        driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]/form[1]/div[2]/div[1]/div[3]/div[1]/div[1]/div[1]/span[1]/span[1]")).click();

        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File("screenshot_gymnast.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver.quit();
        String file = fileToBase64();
        EmailDto emailDto = EmailDto.builder()
                .email(email)
                .childFirstName(childFirstNAme)
                .childSecondName(childSecondName)
                .firstName(parentFirstName)
                .lastName(parentSecondName)
                .service(service)
                .picture(file)
                .build();
        rabbitService.sendToEmailService(emailDto);
        return fileToBase64();
    }

    private WebDriver getWebDriver() {
        WebDriver driver = new FirefoxDriver();
        driver.get(gymnastUrl);
        return driver;
    }

    private String fileToBase64() {
        String encodeString = "";
        try {
            byte[] file = FileUtils.readFileToByteArray(new File("screenshot_gymnast.jpg"));
            encodeString = Base64.getEncoder().encodeToString(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodeString;
    }
}
