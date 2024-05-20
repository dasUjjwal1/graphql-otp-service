package com.surelygql.otpservice.service;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

public interface SendOtp {
    public void sendOtp(String email) throws MailjetSocketTimeoutException, MailjetException;
}
