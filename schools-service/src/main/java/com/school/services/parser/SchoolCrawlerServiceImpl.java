package com.school.services.parser;

import com.school.dto.EmailDto;
import com.school.entity.SchoolDetails;
import com.school.enums.ResponseMessages;
import com.school.repository.SchoolDetailsRepo;
import com.school.security.AES;
import com.school.services.rabbit.RabbitService;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class SchoolCrawlerServiceImpl implements SchoolCrawlerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchoolCrawlerServiceImpl.class);

    @Value("${url.school}")
    private String schoolUrl;

    @Value("secretPassword")
    String secretWord;

    @Value("${service}")
    private String service;

    @Autowired
    SchoolDetailsRepo schoolDetailsRepo;

    @Autowired
    RabbitService rabbitService;

    LocalDate start = LocalDate.now();
    @Override
    public String sendFormToSchool(String uuidChild) {
//        LocalDate start = LocalDate.now();/
        String uuid = uuidChild;

        Optional<SchoolDetails> optionalSchoolDetails = schoolDetailsRepo.findByUuidChildAndDeleted(
                uuid, false);
        if (!optionalSchoolDetails.isPresent()) {
            return ResponseMessages.CHILD + ResponseMessages.WITH_UUID + uuid + ResponseMessages.NOT_FOUND;
        }
        SchoolDetails schoolDetails = optionalSchoolDetails.get();
        WebDriver driver = getWebDriver();
        WebElement buttonToSchoolLoginByMisradHinuh = driver.findElement(By.xpath("//a[@id='misradHachinuch']"));
        buttonToSchoolLoginByMisradHinuh.click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement studentCode = driver.findElement(By.xpath("//input[@id='HIN_USERID']"));
        CharSequence childNickName = schoolDetails.getSchoolUserName();
        studentCode.sendKeys(childNickName);
        WebElement studentPassword = driver.findElement(By.xpath("//input[@id='Ecom_Password']"));
        CharSequence childPassword = AES.decrypt(schoolDetails.getSchoolPassword(), secretWord);
        studentPassword.sendKeys(childPassword);
        WebElement buttonLogin = driver.findElement(By.xpath("//button[@id='loginButton2']"));
        buttonLogin.submit();

        driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
        WebElement makeDocument = driver.findElement(
                By.xpath("/html[1]/body[1]/form[1]/main[1]/div[2]/div[1]/div[1]/div[1]/a[1]"));
        WebDriverWait wait = new WebDriverWait(driver, 60); //here, wait time is 40 seconds
        wait.until(ExpectedConditions.visibilityOf(makeDocument));
        driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
        makeDocument.click();
//        WebDriverWait wait2 = new WebDriverWait(driver, 300);
        WebElement x = driver.findElement(By.xpath("/html[1]/body[1]/form[1]/div[3]"));
        WebDriverWait wait1 = new WebDriverWait(driver, 60);
        wait1.until(ExpectedConditions.visibilityOf(x));
        wait1 = new WebDriverWait(driver, 60); //here, wait time is 120 seconds
        wait1.until(ExpectedConditions.visibilityOf(x));
//TODO заполнить тофес
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File("screenshot_school.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver.quit();
        LOGGER.info(start + ": -> " + "parse school page with url: " + schoolUrl);
        String file = fileToBase64();
        EmailDto emailDto = EmailDto.builder()
                .email("")
                .childFirstName("")
                .childSecondName("")
                .firstName("")
                .lastName("")
                .picture(file)
                .service(service)
                .build();
        rabbitService.sendToEmailService(emailDto);
        return fileToBase64();

    }

    private String fileToBase64() {
        String encodeString = "";
        try {
            byte[] file = FileUtils.readFileToByteArray(new File("screenshot_school.jpg"));
            encodeString = Base64.getEncoder().encodeToString(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info(start + ": -> " + "encoded screenshot");
        return encodeString;
    }

    private WebDriver getWebDriver() {
        WebDriver driver = new FirefoxDriver();
        driver.get(schoolUrl);
        return driver;
    }
}
