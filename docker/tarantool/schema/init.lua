box.cfg {
    listen = 3301,
    log_level = 6,
}

messages = box.schema.create_space('messages', { if_not_exists = true })
messages:format({
    { name = 'dialogue_id', type = 'string' },
    { name = 'author_id', type = 'uuid' },
    { name = 'created_date', type = 'datetime' },
    { name = 'text', type = 'string' }
})
messages:create_index('dia_index', { parts = { { 'dialogue_id' } } }, { if_not_exists = true })

box.schema.user.create('otus_x', { password = 'otus_x' }, { if_not_exists = true })
box.schema.user.grant('otus_x', 'read,write,execute', 'universe')