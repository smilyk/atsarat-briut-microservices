package com.gymnast.services.parseService;

import com.gymnast.dto.EmailDto;
import com.gymnast.services.rabbitService.RabbitService;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
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

    @Autowired
    RabbitService rabbitService;

    @Override
    public String sendFormToGymnast(String uuidChild) {
// TODO: 29/10/2020
//        get child name + child second name
//        get parent name + parent second name

        LocalDate start = LocalDate.now();
        WebDriver driver = getWebDriver();
        WebElement childName = driver.findElement(By.xpath("//body/div[1]/div[2]/form[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]"));
        childName.sendKeys("Ivan Ivanov");
        WebElement tzChild = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]/form[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]"));
        tzChild.sendKeys("111111111");
        WebElement parentName = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]/form[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]"));
        parentName.sendKeys("Natalya Ivanova");
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
                .email("")
                .childFirstName("")
                .childSecondName("")
                .firstName("")
                .lastName("")
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
