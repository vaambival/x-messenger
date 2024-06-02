package ru.otus.xmessenger.dao.tarantool;

import io.tarantool.driver.api.TarantoolClient;
import io.tarantool.driver.api.TarantoolResult;
import io.tarantool.driver.api.tuple.DefaultTarantoolTupleFactory;
import io.tarantool.driver.api.tuple.TarantoolTuple;
import io.tarantool.driver.mappers.factories.DefaultMessagePackMapperFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.xmessenger.constant.Time;
import ru.otus.xmessenger.dao.Message;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TarantoolDao {

    private final TarantoolClient<TarantoolTuple, TarantoolResult<TarantoolTuple>> client;

    public List<Message> findDialogue(UUID owner, UUID addressee) {
        try {
            return mapToList(client.callForTupleResult("select", List.of(getDialogueId(owner, addressee)),
                    TarantoolTuple.class).get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Message insertMessage(UUID owner, UUID addressee, String text) {
        var id = getDialogueId(owner, addressee);
        var createdDate = ZonedDateTime.now(Time.DEFAULT_ZONE_ID).toLocalDateTime();
        client.call("insert", id, owner, createdDate, text);
        return new Message()
                .setAuthorId(owner)
                .setCreated(createdDate)
                .setText(text);
    }

    private List<Message> mapToList(TarantoolResult<TarantoolTuple> tarantoolTuples) {
        return tarantoolTuples
                .stream()
                .map(this::toMessage)
                .collect(Collectors.toList());
    }

    private Message toMessage(TarantoolTuple fields) {
        return new Message()
                .setAuthorId(fields.getUUID("author_id"))
                .setCreated(fields.getObject("created", LocalDateTime.class).orElseThrow())
                .setText(fields.getString("text"));
    }

    private String getDialogueId(UUID owner, UUID addressee) {
        if (owner.compareTo(addressee) < 0) {
            return owner.toString() + addressee;
        }
        return addressee.toString() + owner;
    }
}
