FROM tarantool/tarantool
RUN tt init
RUN mkdir -p /opt/tarantool/instances.enabled/messages
COPY init.lua /opt/tarantool/instances.enabled/messages
COPY config.yaml /opt/tarantool/instances.enabled/messages
COPY instances.yml /opt/tarantool/instances.enabled/messages
CMD ["tt", "run", "/opt/tarantool/instances.enabled/messages/init.lua"]