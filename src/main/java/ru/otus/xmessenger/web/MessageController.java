package ru.otus.xmessenger.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.xmessenger.dao.Message;
import ru.otus.xmessenger.dao.MessageDao;
import ru.otus.xmessenger.service.UserSecurityService;
import ru.otus.xmessenger.web.dto.DialogMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dialog")
@RequiredArgsConstructor
public class MessageController {

    private final MessageDao messageDao;
    private final UserSecurityService userSecurityService;

    @GetMapping("/{user_id}/list")
    public List<Message> list(@PathVariable("user_id") UUID addressee) {
        return messageDao.findDialogue(userSecurityService.getUuid(), addressee);
    }

    @GetMapping("/{user_id}/list/continue")
    public List<Message> list(@PathVariable("user_id") UUID addressee,
                              @RequestParam("created_date") LocalDateTime createdDate,
                              @RequestParam("limit") int limit) {
        return messageDao.findDialogueContinue(userSecurityService.getUuid(), addressee, createdDate, limit);
    }

    @PostMapping("/{user_id}/send")
    public Message send(@PathVariable("user_id") UUID addressee, @RequestBody DialogMessage message) {
        return messageDao.insertMessage(userSecurityService.getUuid(), addressee, message.getText());
    }
}
