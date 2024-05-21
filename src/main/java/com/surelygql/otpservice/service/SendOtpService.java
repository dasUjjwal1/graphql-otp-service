package com.surelygql.otpservice.service;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
public class SendOtpService implements SendOtp {
 Logger LOG = LoggerFactory.getLogger(SendOtpService.class);
    @Override
    public void sendOtp(String email,String otp) throws UnirestException {
        Map<String,String> requestHeader = new HashMap<>();
        requestHeader.put("header",)
       HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + "sandbox920b7a1aa5244075b1bb8f9f97df8318.mailgun.org" + "/messages")
                .basicAuth("api", "57cfbcdee75c5bb3761a74a2bc685389-a2dd40a3-53f7f2e6")
                .headers()
                .queryString("from", "Verification <hello@payrollbyte.com>")
                .queryString("to", "sona95986@gmail.com")
                .queryString("subject", "Verification")
                .queryString("text",otp ).asJson();
       LOG.info("response",response.getStatus());
       LOG.info("body",response.getBody());
    }
}
