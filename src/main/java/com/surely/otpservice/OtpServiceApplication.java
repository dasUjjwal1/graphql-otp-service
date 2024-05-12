package com.surely.otpservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPooled;

@SpringBootApplication
public class OtpServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OtpServiceApplication.class, args);
        HostAndPort hostAndPort = new HostAndPort("localhost", 6379);
        JedisPooled jedis = new JedisPooled(hostAndPort,
                DefaultJedisClientConfig.builder()
                        .socketTimeoutMillis(5000)  // set timeout to 5 seconds
                        .connectionTimeoutMillis(5000) // set connection timeout to 5 seconds
                        .build());
        jedis.set("foo", "bar");
        System.out.println(jedis.get("foo"));
    }

}
