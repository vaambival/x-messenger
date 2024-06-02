function insert(dialogue_id, author_id, created_date, text)
    box.space.messages:replace(box.tuple.new({dialogue_id, author_id, created_date, text}))
end

function select(dialogue_id)
    return messages.index.dialogue:select { dialogue_id }
end