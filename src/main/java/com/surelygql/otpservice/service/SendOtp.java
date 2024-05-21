package com.surelygql.otpservice.service;


import com.mashape.unirest.http.exceptions.UnirestException;

public interface SendOtp {
     void sendOtp(String email,String otp) throws UnirestException;
}
