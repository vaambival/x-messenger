package ru.otus.xmessenger.dao.tarantool;

import io.tarantool.driver.api.TarantoolClient;
import io.tarantool.driver.api.TarantoolClientFactory;
import io.tarantool.driver.api.TarantoolResult;
import io.tarantool.driver.api.tuple.TarantoolTuple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("${tarantool.host}")
    private String host;
    @Value("${tarantool.port}")
    private int port;
    @Value("${tarantool.username}")
    private String username;
    @Value("${tarantool.password}")
    private String password;

    @Bean
    public TarantoolClient<TarantoolTuple, TarantoolResult<TarantoolTuple>> tarantoolClient() {
        return TarantoolClientFactory.createClient()
                .withAddress(host, port)
                .withCredentials(username, password)
                .build();
    }
}
