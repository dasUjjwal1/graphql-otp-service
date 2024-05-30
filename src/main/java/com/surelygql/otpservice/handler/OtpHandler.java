package com.surelygql.otpservice.handler;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.surelygql.otpservice.OtpServiceApplication;
import com.surelygql.otpservice.service.SendOtpServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.params.XReadGroupParams;
import redis.clients.jedis.resps.StreamEntry;

import java.util.*;

@Controller
public class OtpHandler {
    final String stream1 = "stream1";
    final String group1 = "stream1";
    Logger Log = LoggerFactory.getLogger(OtpServiceApplication.class);
    HostAndPort hostAndPort = new HostAndPort("localhost", 6379);
    final String consumer = UUID.randomUUID().toString();
    int maxAttempt = 5;
    JedisPool pool = new JedisPool(hostAndPort, DefaultJedisClientConfig.builder().socketTimeoutMillis(5000)  // set timeout to 5 seconds
            .connectionTimeoutMillis(5000) // set connection timeout to 5 seconds
            .build());
    @Autowired
    private SendOtpServiceImpl sendOtp = new SendOtpServiceImpl();

    public String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }


    public void init() {
        while (maxAttempt > 0) {
            try (Jedis jedis = pool.getResource()) {
                Map<String, StreamEntryID> stream = new HashMap<>();
                stream.put(stream1, StreamEntryID.UNRECEIVED_ENTRY);

                List<Map.Entry<String, List<StreamEntry>>> response = jedis.xreadGroup(group1, consumer, XReadGroupParams.xReadGroupParams().count(1).block(1000), stream);
                if (response != null) {
                    for (Map.Entry<String, List<StreamEntry>> stringListEntry : response) {
                        List<StreamEntry> streamEntries = stringListEntry.getValue();
                        for (StreamEntry key : streamEntries) {
                            String email = key.getFields().get("email");
                            String type = key.getFields().get("type");
                            String otpNumber = getRandomNumberString();
                            sendOtp.sendTextOtp(email, otpNumber);
                            jedis.set(type + "-" + email, otpNumber);
                            jedis.expire(type + "-" + email, 300);
                            jedis.xack(stream1, group1, key.getID());
                            jedis.xtrim(stream1, 10, true);

                        }
                    }
                }
            } catch (JedisConnectionException | UnirestException es) {
                Log.warn((es.getLocalizedMessage()));
            }
        }

    }


}
