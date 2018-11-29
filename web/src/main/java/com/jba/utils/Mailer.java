package com.jba.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Component
@EnableAsync
public class Mailer {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async("mailerTaskExecutor")
    public void sendSimpleMessage(String to, String subject, String text, boolean isHTML){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, isHTML);
            mailSender.send(message);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Bean(name="mailerTaskExecutor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("wholepool-mailer");
        executor.initialize();
        return executor;
    }

    @Async("mailerTaskExecutor")
    public void sendAccountCreatedMessage(String to, String userName){
        Map<String, String> map = new HashMap<>();
        map.put("user", userName);
        sendSimpleMessage(to, "Utworzono konto w serwisie Wholepool", build("mail/welcome-mail", map), true);
    }

    @Async("mailerTaskExecutor")
    public void sendChangedPasswordMessage(String to, String userName){
        Map<String, String> map = new HashMap<>();
        map.put("user", userName);
        sendSimpleMessage(to, "Twoje hasło w serwisie Wholepool zostało zmienione", build("mail/password-changed", map), true);
    }

    public String build(String template, Map<String, String> params){
        Context context = new Context();
        params.forEach((key, value) -> context.setVariable(key,value));
        return templateEngine.process(template, context);
    }
}
