//package com.saopay.apiyouzan;
//
//import lombok.extern.slf4j.Slf4j;
//import org.base.hd.util.Config;
//import org.base.hd.util.InitConfig;
//import org.base.hd.util.redis.JedisUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//import redis.clients.jedis.Jedis;
//
//
//@Component
//@Slf4j
//public class StartupRunner extends Config implements ApplicationRunner {
//
//    @Override
//    @Value("${wechat.public.token_url}")
//    public void setTokenUrl(String tokenUrl) {
//        super.setTokenUrl(tokenUrl);
//    }
//
//    @Override
//    @Value("${wechat.public.user_token_url}")
//    public void setUserTokenUrl(String userTokenUrl) {
//        super.setUserTokenUrl(userTokenUrl);
//    }
//
//    @Override
//    @Value("${wechat.public.jsapi_ticket_url}")
//    public void setJsapiTicketUr(String jsapiTicketUr) {
//        super.setJsapiTicketUr(jsapiTicketUr);
//    }
//
//    @Override
//    @Value("${redis.host}")
//    public void setHost(String host) {
//        super.setHost(host);
//    }
//
//    @Override
//    @Value("${redis.password}")
//    public void setPassword(String password) {
//        super.setPassword(password);
//    }
//
//    @Override
//    @Value("${redis.port}")
//    public void setPort(int port) {
//        super.setPort(port);
//    }
//
//    @Override
//    @Value("${redis.database}")
//    public void setDatabase(int database) {
//        super.setDatabase(database);
//    }
//
//
//    @Override
//    public void run(ApplicationArguments applicationArguments) throws Exception {
//        InitConfig.init(this);
//        Jedis resource = JedisUtils.getResource();
//    }
//
//}
