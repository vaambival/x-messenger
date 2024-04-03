# Подготовка к запуску

## Подготовка БД

В проекте используется СУБД [PostgreSQL](https://www.postgresql.org/download/). Для корректного запуска приложения следует
создать БД x_messenger. Для этого рекомендуется создать пользователя, где `хххххх` - пароль пользователя:
```postgres-psql
CREATE ROLE otus_x WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  CREATEDB
  NOCREATEROLE
  REPLICATION
 PASSWORD 'xxxxxx';
```
Далее необходимо создать БД:
```postgres-psql
CREATE DATABASE x_messenger
    WITH
    OWNER = otus_x
    ENCODING = 'UTF8'
    LC_COLLATE = 'ru_RU.UTF-8'
    LC_CTYPE = 'ru_RU.UTF-8'
    CONNECTION LIMIT = -1
    TEMPLATE = template0;
```
Миграции по созданию схем и таблиц БД запустятся автоматически при старте приложения.
Для старта приложения необходимо в файле [application.yaml](src/main/resources/application.yaml)
следует указать настройки по подключению к БД:
* url - строка-ссылка для подключения к БД
* username - имя пользователя БД
* password - пароль для подключения к БД

## Настройка JAVA

Для запуска приложения необходимо использовать OpenJDK-21 (Oracle OpenJDK version 21.0.2).
Найти можно [здесь](https://jdk.java.net/21/).
Для запуска следует запустить команду [из корня проекта](.):
```
./gradlew spring-boot:run
```

Приложение запустится на порту 8081. Если он у вас занят, то
можно указать свободный порт в файле [application.yaml](src/main/resources/application.yaml):
```yaml
server:
  port: 8081
```

# API
API можно посмотреть в [свагере](http://localhost:8081/swagger-ui/index.html#/) или воспользоваться [postman-коллекцией](postman).

# Запуск в контейнерах

Для запуска с использованием docker необходимо запустить сервис пользователей в контейнерах и воспользоваться
[docker-compose](docker-compose.yaml) текущего проекта
```shell
docker compose up
```

Сервис пользователей (ДЗ №1 и ДЗ №2) доступен по порту `8080`, а текущий сервис диалогов доступен по порту `8081`

# Шардирование

Для описания шардирования обратитесь в соответствующий [раздел](docker/sharding/README.md)