package ru.otus.xmessenger.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.otus.xmessenger.constant.Time;
import ru.otus.xmessenger.dao.exception.NotSaveException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MessageDao {

    private static final String FIND_DIALOGUE = "SELECT author_id, created_date, text FROM x_messenger.messages" +
            " WHERE id=? ORDER BY created_date desc LIMIT 10";
    private static final String FIND_DIALOGUE_CONTINUE = "SELECT author_id, created_date, text FROM " +
            "x_messenger.messages WHERE id=? AND created_date < ? ORDER BY created_date LIMIT ?";
    private static final String INSERT_MESSAGE = "INSERT INTO x_messenger.messages (id, author_id, created_date, " +
            "text) VALUES (?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public List<Message> findDialogue(UUID owner, UUID addressee) {
        var id = getDialogueId(owner, addressee);
        return jdbcTemplate.query(FIND_DIALOGUE, messageMapper(), id);
    }

    public List<Message> findDialogueContinue(UUID owner, UUID addressee, LocalDateTime createdDate, int limit) {
        var id = getDialogueId(owner, addressee);
        return jdbcTemplate.query(FIND_DIALOGUE_CONTINUE, messageMapper(), id, createdDate, limit);
    }

    public Message insertMessage(UUID owner, UUID addressee, String text) {
        var id = getDialogueId(owner, addressee);
        var createdDate = ZonedDateTime.now(Time.DEFAULT_ZONE_ID).toLocalDateTime();
        var rows = jdbcTemplate.update(INSERT_MESSAGE, statement -> {
            statement.setString(1, id);
            statement.setObject(2, owner);
            statement.setTimestamp(3, Timestamp.valueOf(createdDate));
            statement.setString(4, text);
        });
        if (rows == 0) {
            throw new NotSaveException();
        }
        return new Message()
                .setId(id)
                .setAuthorId(owner)
                .setCreated(createdDate)
                .setText(text);
    }

    private RowMapper<Message> messageMapper() {
        return (rs, rowNum) -> messageFromRs(rs);
    }

    private Message messageFromRs(ResultSet rs) throws SQLException {
        return new Message()
                .setId(rs.getString("dialogue_id"))
                .setAuthorId(rs.getObject("uuid", UUID.class))
                .setCreated(rs.getTimestamp("created_date").toLocalDateTime())
                .setText(rs.getString("text"));
    }

    private String getDialogueId(UUID owner, UUID addressee) {
        if (owner.compareTo(addressee) < 0) {
            return owner.toString() + addressee;
        }
        return addressee.toString() + owner;
    }
}
