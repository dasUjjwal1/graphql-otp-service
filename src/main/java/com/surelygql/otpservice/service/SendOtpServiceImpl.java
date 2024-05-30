package com.surelygql.otpservice.service;


import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SendOtpServiceImpl implements SendOtp {
 Logger LOG = LoggerFactory.getLogger(SendOtpServiceImpl.class);
    @Override
    public void sendTextOtp(String email,String otp) throws UnirestException {
       LOG.info("Otp is {}",otp);
    }
}
