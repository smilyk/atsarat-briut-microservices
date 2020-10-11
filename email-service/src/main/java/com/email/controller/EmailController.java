package com.email.controller;

import com.email.dto.ConfirmationDto;
import com.email.dto.EmailVerificationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;

@RestController
public class EmailController {


    @Autowired
    JavaMailSender emailSender;
    @Value("${email.address}")
    String adminEmail;
    @Autowired
    Environment env;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);
    public static final String APPLICATION_NAME = " Atsarat Briut ";
    private static final String CURRENTLY_DATE = LocalDateTime.now().toLocalDate().toString();
//    private final String ADMIN_EMAIL = env.getProperty("email.password");
    private static final String GYMNAST_SERVICE = " gymnast service";
    private static final String SCHOOL_SERVICE = " school Ben Gurion service ";
    private static final String TSOFIM_SERVICE = " tsofim group כפיר service";
    private static final String VERIFICATION_SERVICE = " verification email ";
    @PostMapping("/verification-email")
    String sendSimpleEmail(@RequestBody EmailVerificationDto mail) throws MessagingException {
      env.getProperty("verification.link");
        MimeMessage msg = emailSender.createMimeMessage();

        String email = mail.getEmail();
        String tokenValue = mail.getTokenValue();
        String lastName = mail.getUserLastName();
        String firstName = mail.getUserName();

        String VERIFY_LINK = env.getProperty("verification.link");


        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        ////        The HTML body for the email.
        final String htmlMsg =
                "<h1> Hi " + firstName + " " + lastName + ". Please verify your email address</h1>"
                        + "<p>Thank you for registering with our app. To complete registration process and be able to log in,"
                        + " click on the following link: "
                        + " <a href='" + VERIFY_LINK + "?token=" + tokenValue + "'>" + "<br/><br/>" + " Final step to complete your registration" +
                        "</a><br/><br/>"
                        + "Thank you! And we are waiting for you inside!";
        helper.setTo(email);

        helper.setTo(email);
        helper.addAttachment("logo.png", new ClassPathResource("logo.png"));
        helper.setSubject("Verification email from" + APPLICATION_NAME);
        helper.setText(htmlMsg, true);
        helper.addAttachment("logo.png", new ClassPathResource("logo.png"));
        try {
            emailSender.send(msg);
            LOGGER.info(" Verification email was send to e-mail: " + email);
        } catch (Exception ex) {
            LOGGER.error("verification email: something is wrong" + ex);
            emailError(adminEmail, VERIFICATION_SERVICE, lastName, firstName);
            LOGGER.error("send error-email to administrator");
            System.out.println(ex.getMessage());
        }
        return "Email send!";
    }

    @PostMapping("/gymnast-email")
    String sendGymnastEmail(@RequestBody ConfirmationDto mail) throws MessagingException {
        String email = mail.getEmail();
        String picture = mail.getPicture();
        String lastName = mail.getUserLastName();
        String firstName = mail.getUserName();
        String childFirstName = mail.getChildFirstName();
        String childSecondName = mail.getChildSecondName();

        MimeMessage msg = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        final String htmlMsg =
                "<h1> Hi " + firstName + " " + lastName + "</h1>" +
                        "<br><hr> you filled out a confirmation that the child " + childFirstName + " " + childSecondName + " " +
                        "can attend gymnastics training in group \"gymnast mazceret batya - acrobatic and gymnastic\"  <br><hr>" +
                        "Date: " + CURRENTLY_DATE + "<br><hr>" +
                        "Resolution documents you can check in attached file";
        helper.setTo(email);
        byte[] decodedImage = Base64.getDecoder().decode(picture);
        helper.addAttachment("gymnast_resolution.jpg", new ByteArrayResource(decodedImage));
        helper.addAttachment("logo.png", new ClassPathResource("logo.png"));

        helper.setSubject("Atsarat Briut for Gymnast email from" + APPLICATION_NAME);
        helper.setText(htmlMsg, true);
        try {
            emailSender.send(msg);
            LOGGER.info(" Gymnast atsarat briut was send to e-mail: " + email);
        } catch (Exception ex) {
            LOGGER.error("gymnast atsarat briut email: something is wrong" + ex);
            emailError(adminEmail, GYMNAST_SERVICE, lastName, firstName);
            LOGGER.error("send error-email to administrator");
            System.out.println(ex.getMessage());
            return "Error -> email not send";
        }
        return "Email send!";
    }

    @PostMapping("/ben-gurion-email")
    String sendSchoolEmail(@RequestBody ConfirmationDto mail) throws MessagingException {
        String email = mail.getEmail();
        String picture = mail.getPicture();
        String lastName = mail.getUserLastName();
        String firstName = mail.getUserName();
        String childFirstName = mail.getChildFirstName();
        String childSecondName = mail.getChildSecondName();

        MimeMessage msg = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        final String htmlMsg =
                "<h1> Hi " + firstName + " " + lastName + ".</h1>" +
                        "<br> you filled out a confirmation that the child " + childFirstName + " " + childSecondName +
                        "<br> can attend school Ben Gurion in Mazceret Batya  <hr>" +
                        "<br>Date: " + CURRENTLY_DATE + "<hr>" +
                        "<br>Resolution documents you can check in attached file";
        helper.setTo(email);
        byte[] decodedImage = Base64.getDecoder().decode(picture);
        helper.addAttachment("school_ben_gurion_resolution.jpg", new ByteArrayResource(decodedImage));
        helper.addAttachment("logo.png", new ClassPathResource("logo.png"));
        helper.setSubject("Atsarat Briut for School Ben Gurion In Mazceret Batya email from" + APPLICATION_NAME);
        helper.setText(htmlMsg, true);
        try {
            emailSender.send(msg);
            LOGGER.info(" School Ben Gurion atsarat briut was send to e-mail: " + email);
        } catch (Exception ex) {
            LOGGER.error("scgool Ben Gurion atsarat briut email: something is wrong" + ex);
            emailError(adminEmail,SCHOOL_SERVICE, lastName, firstName);
            LOGGER.error("send error-email to administrator");
            System.out.println(ex.getMessage());
        }
        return "Email send!";
    }

    @PostMapping("/tsofim-email")
    String sendTsofimEmail(@RequestBody ConfirmationDto mail) throws MessagingException {
        String email = mail.getEmail();
        String picture = mail.getPicture();
        String lastName = mail.getUserLastName();
        String firstName = mail.getUserName();
        String childFirstName = mail.getChildFirstName();
        String childSecondName = mail.getChildSecondName();

        MimeMessage msg = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        final String htmlMsg =
                "<h1> Hi " + firstName + " " + lastName + "</h1>" +
                        "<br><hr> you filled out a confirmation that the child " + childFirstName + " " + childSecondName + " " +
                        "can attend tsofim group  <br><hr>" +
                        "Date: " + CURRENTLY_DATE + "<br><hr>" +
                        "Resolution documents you can check in attached file";
        helper.setTo(email);
        byte[] decodedImage = Base64.getDecoder().decode(picture);
        helper.addAttachment("tsofim_resolution.jpg", new ByteArrayResource(decodedImage));
        helper.addAttachment("logo.png", new ClassPathResource("logo.png"));

        helper.setSubject("Atsarat Briut for Tsofim email from" + APPLICATION_NAME);
        helper.setText(htmlMsg, true);
        try {
            emailSender.send(msg);
            LOGGER.info(" Tsofim atsarat briut was send to e-mail: " + email);
        } catch (Exception ex) {
            LOGGER.error(" tsofim atsarat briut email: something is wrong" + ex);
            emailError(adminEmail, TSOFIM_SERVICE, lastName, firstName);
            LOGGER.error("send error-email to administrator");
            System.out.println(ex.getMessage());
            return "Error -> email not send";
        }
        return "Email send!";
    }

    private void emailError(String email, String service, String userFirstName, String userLastName)
            throws MessagingException {
        MimeMessage msg = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        final String htmlMsg =
                "<h1> Hi  administrator </h1>>" +
                        " <hr><br>there is some problems during sending email from " + service
                + "to e-mail: " + email + " for " + userFirstName + userLastName + ". \n" +
                        "<br>please, check logfile.";
        helper.setTo(email);
        helper.addAttachment("logo.png", new ClassPathResource("logo.png"));
        helper.setSubject("Error email" + APPLICATION_NAME);
        helper.setText(htmlMsg, true);
        try {
            emailSender.send(msg);
            LOGGER.info(" Error email was send to  administrator to e-mail: " + email);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}