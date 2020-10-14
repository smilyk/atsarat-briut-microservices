package com.tsofim.servicers.parser;

import com.tsofim.dto.EmailDto;
import com.tsofim.entity.TsofimDetails;
import com.tsofim.enums.ResponseMessages;
import com.tsofim.repository.TsofimDetailsRepo;
import com.tsofim.servicers.rabbitService.RabbitService;
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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class TsofimCrawlerServiceImpl implements TsofimCrawlerService {
    @Value("${url.tsofim}")
    private String tsofimUrl;

    @Value("secretPassword")
    String secretWord;

    @Autowired
    TsofimDetailsRepo tsofimDetailsRepo;
    @Autowired
    RabbitService rabbitService;
    LocalDate start = LocalDate.now();
    private static final Logger LOGGER = LoggerFactory.getLogger(TsofimCrawlerServiceImpl.class);

    @Override
    public String sendFormToTsofim(String uuidChild) {

        String uuid = "777";
        String childFirstNAme = "Liza";
        String childSecondName = "HREW";
        String chikldTZ = "111111111";
        String parentFirstName = "787878";
        String parentSecondNAme = "90909";
        String parentTZ = "333333333";

        Optional<TsofimDetails> optionalTsofimDetails = tsofimDetailsRepo.findByUuidChildAndDeleted(
                uuid, false);
        if (!optionalTsofimDetails.isPresent()) {
            return ResponseMessages.CHILD + ResponseMessages.WITH_UUID + uuid + ResponseMessages.NOT_FOUND;
        }
        TsofimDetails tsofimDetails = optionalTsofimDetails.get();

        WebDriver driver = getWebDriver();

//        parsing start
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement button1 = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/a[1]/button[1]"));
        WebDriverWait wait = new WebDriverWait(driver, 60); //here, wait time is 40 seconds
        wait.until(ExpectedConditions.visibilityOf(button1));
        button1.click();

        driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[2]/div[2]/input[1]")).sendKeys(
                childFirstNAme + " " + childSecondName
        );

        driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[2]/div[3]/input[1]")).sendKeys(
                chikldTZ
        );

        String place = tsofimDetails.getPlace().trim();
        WebElement choosePlace = driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[2]/div[4]"));
        choosePlace.click();
        List<WebElement> listChoosePlace = driver.findElements(By.xpath("//body/div[@id='root']/div[1]/div[2]/div[4]/ul/li"));
        for (WebElement chooseElement : listChoosePlace) {
            if (place.equals(chooseElement.getText())) {
                chooseElement.click();
                break;
            }
        }

        String group = tsofimDetails.getGroupTs().trim();
        WebElement groupElement = driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[2]/div[5]"));
        groupElement.click();
        List<WebElement> listGroup = driver.findElements(By.xpath("//body/div[@id='root']/div[1]/div[2]/div[5]/ul/li"));
        for (WebElement chooseElement : listGroup) {
            if (group.equals(chooseElement.getText())) {
                chooseElement.click();
                break;
            }
        }

        String classChild = tsofimDetails.getChildClass().trim();
        WebElement classChildElement = driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[2]/div[6]"));
        classChildElement.click();
        List<WebElement> childsElemenList = driver.findElements(By.xpath("//body/div[@id='root']/div[1]/div[2]/div[6]/ul/li"));
        for (WebElement chooseElement : childsElemenList) {
            if (classChild.equals(chooseElement.getText())) {
                chooseElement.click();
                break;
            }
        }

        String school = tsofimDetails.getSchool().trim();
        driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[2]/div[7]/input[1]")).sendKeys(school);

        driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[8]/button[1]")).click();

        driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[1]/label[1]/input[1]")).click();
        driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[1]/div[3]/input[1]")).click();
        driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[1]/div[5]/input[1]")).click();

        driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[2]/div[1]/input[1]")).sendKeys(
                parentFirstName + " " + parentSecondNAme);
        driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[2]/div[2]/input[1]")).sendKeys(
                parentTZ
        );
        driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[3]/div[1]/input[1]")).click();
        driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[3]/button[1]")).click();

        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[1]"));
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File("screenshot_tsofim.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver.quit();
        LOGGER.info(start + ": -> " + "parse tsofim page with url: " + tsofimUrl);
        String file = fileToBase64();
        EmailDto emailDto = EmailDto.builder()
                .email("")
                .childFirstName("")
                .childSecondName("")
                .firstName("")
                .lastName("")
                .picture(file)
                .build();
        rabbitService.sendToEmailService(emailDto);
        return fileToBase64();
    }

    private String fileToBase64() {
        String encodeString = "";
        try {
            byte[] file = FileUtils.readFileToByteArray(new File("screenshot_tsofim.jpg"));
            encodeString = Base64.getEncoder().encodeToString(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info(start + ": -> " + "encoded screenshot");
        return encodeString;
    }

    private WebDriver getWebDriver() {
        WebDriver driver = new FirefoxDriver();
        driver.get(tsofimUrl);
        return driver;
    }
}
