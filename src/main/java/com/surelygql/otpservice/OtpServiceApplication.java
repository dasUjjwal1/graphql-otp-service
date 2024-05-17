package com.surelygql.otpservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.params.XReadGroupParams;
import redis.clients.jedis.params.XReadParams;
import redis.clients.jedis.resps.StreamEntry;

import java.util.*;

@SpringBootApplication
public class OtpServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OtpServiceApplication.class, args);
        Logger Log = LoggerFactory.getLogger(OtpServiceApplication.class);
        HostAndPort hostAndPort = new HostAndPort("localhost", 6379);
        JedisPooled jedis = new JedisPooled(hostAndPort,
                DefaultJedisClientConfig.builder()
                        .socketTimeoutMillis(5000)  // set timeout to 5 seconds
                        .connectionTimeoutMillis(5000) // set connection timeout to 5 seconds
                        .build());
        Map<String, StreamEntryID> stream = new HashMap<>();
//        jedis.xgroupCreate("stream1", "stream1", StreamEntryID.LAST_ENTRY, true);
        stream.put("stream1", StreamEntryID.UNRECEIVED_ENTRY);
        while (true) {
            List<Map.Entry<String, List<StreamEntry>>> result =
                    jedis.xreadGroup("stream1", "stream1", XReadGroupParams.xReadGroupParams().count(1)
                            .block(1000), stream);
            if (result != null) {
                for (Map.Entry<String, List<StreamEntry>> e1 : result) {
                    List<StreamEntry> streamEntries = e1.getValue();
                    for (StreamEntry se : streamEntries) {
                        Log.info("E1", se.getFields().toString());
                        jedis.xack("stream1", "stream1", se.getID());
                    }
                }
            }

        }

    }

}
