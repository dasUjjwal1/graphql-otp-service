package com.surelygql.otpservice;

import com.surelygql.otpservice.handler.OtpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.params.XReadGroupParams;
import redis.clients.jedis.params.XReadParams;
import redis.clients.jedis.resps.StreamEntry;

import java.util.*;

@SpringBootApplication
public class OtpServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OtpServiceApplication.class, args);
        OtpHandler otpHandler = new OtpHandler();
        otpHandler.init();
    }

}
