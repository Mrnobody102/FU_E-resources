package fpt.edu.eresourcessystem.controller.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.web.bind.annotation.RestController
public class MailServiceRestController {

    final
    JavaMailSender javaMailSender;

    public MailServiceRestController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @RequestMapping("/sendEmail")
    public String helloSpringBoot(){

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("maihoa362001@gmail.com");
        message.setTo("huypq1801@gmail.com");
        message.setSubject("Subject : Sample Email Subject");
        message.setText("Body : This is sample mail mail Body.");

        javaMailSender.send(message);

        System.out.println("Mail successfully sent..");

        return "Your Mail sent successfully";
    }

}