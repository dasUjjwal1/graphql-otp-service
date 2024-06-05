package com.surelygql.otpservice;

import com.surelygql.otpservice.handler.OtpHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OtpServiceApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(OtpServiceApplication.class, args);
        OtpHandler otpHandler = new OtpHandler();
        otpHandler.init();
    }

}
