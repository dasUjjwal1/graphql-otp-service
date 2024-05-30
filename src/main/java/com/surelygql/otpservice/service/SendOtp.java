package com.surelygql.otpservice.service;


import com.mashape.unirest.http.exceptions.UnirestException;

public interface SendOtp {
     void sendTextOtp(String email,String otp) throws UnirestException;
}
