package ru.otus.xmessenger.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.xmessenger.dao.Message;
import ru.otus.xmessenger.dao.MessageDao;
import ru.otus.xmessenger.dao.tarantool.TarantoolDao;
import ru.otus.xmessenger.service.UserSecurityService;
import ru.otus.xmessenger.web.dto.DialogMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dialog")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final TarantoolDao messageDao;
    private final UserSecurityService userSecurityService;

    @GetMapping("/{user_id}/list")
    public List<Message> list(@PathVariable("user_id") UUID addressee) {
        log.info("GET LIST By UserId: {}", addressee);
        return messageDao.findDialogue(userSecurityService.getUuid(), addressee);
    }

    @PostMapping("/{user_id}/send")
    public Message send(@PathVariable("user_id") UUID addressee, @RequestBody DialogMessage message) {
        log.info("Send message to UserId: {}", addressee);
        return messageDao.insertMessage(userSecurityService.getUuid(), addressee, message.getText());
    }
}
