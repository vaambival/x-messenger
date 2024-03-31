package ru.otus.xmessenger.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class Message {
    private String id;
    private UUID authorId;
    private LocalDateTime created;
    private String text;
}
