package com.surelygql.otpservice.handler;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.surelygql.otpservice.OtpServiceApplication;
import com.surelygql.otpservice.service.SendOtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.params.XReadGroupParams;
import redis.clients.jedis.resps.StreamEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private SendOtpService sendOtpService = new SendOtpService();
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
                            sendOtpService.sendOtp(key.getFields().get("email"));
                            jedis.xack(stream1, group1, key.getID());
                            jedis.xtrim(stream1, 100, true);
                        }
                    }
                }
            } catch (JedisConnectionException es) {
                Log.warn((es.getLocalizedMessage()));
            } catch (MailjetSocketTimeoutException | MailjetException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
