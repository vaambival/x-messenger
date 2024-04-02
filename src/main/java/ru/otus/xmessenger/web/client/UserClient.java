package ru.otus.xmessenger.web.client;

import java.util.UUID;

public interface UserClient {

    UUID getUser(String token);
}
