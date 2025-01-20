<h1 align="center">Библиотека, задание для Modsen</h1>

<details>
 <summary><strong>
  Техническое задание
</strong></summary>

#### ЗАДАНИЕ:

* Разработать CRUD(Create/Read/Update/Delete) Web API для имитации библиотеки.

</details>

<details>
 <summary><strong>
  Стек
</strong></summary>

#### При разработке были использованы:

* Java 21
* Gradle 8.9
* Spring Boot 3.4.1
* PostgreSQL
* Kafka
* Spring Cloud Feign Client
* Mapstruct
* Jsonwebtoken
* Liquibase
* Junit 5
* SpringDoc Open API
* Docker

</details>

<details>
 <summary><strong>
  Запуск проекта
</strong></summary>

#### Запуск приложения в Docker:

* В корне проекта находится `docker-compose.yml` файл, который нужно запустить
  командой ```docker-compose up -d```.

* Если проект не запускается, то попробуйте сделать build каждого микросервиса отдельно с помощью `./gradlew build` и
  после этого запустить `docker-compose up -d`

</details>

<details>
 <summary><strong>
  Тесты
</strong></summary>

* В микросервисах реализованы unit тесты (покрытие сервисного слоя 100%).
* Запустить тесты можно с помощью команды ```./gradlew test```, находясь в одном из микросервисов.

</details>

<details>
 <summary><strong>
  Документация и функциональность
</strong></summary>

### Разработаны три микросервиса, которые взаимодействуют между собой с помощью kafka и feign client:

* #### Auth-service - сервис хранения пользователей, регистрации и аутентификации.
    * Swagger-Документация: `http://localhost:8082/swagger-ui/index.html`
    * service-url: `http://localhost:8082/api/v1`, database-url: `jdbc:postgresql://localhost:5445/users_db`
    * Использует `Spring Security`.

* #### Book-storage-service - сервис хранения книг и содержащий функционал взаимодействия с книгами (создание, получение страницы, получение по id/ISBN, изменение, удаление)
    * Swagger-Документация: `http://localhost:8081/swagger-ui/index.html`
    * service-url: `http://localhost:8081/api/v1`, database-url: `jdbc:postgresql://localhost:5444/book_storage_db`
    * Использует `Spring Security`: с bearer токеном из запроса идет в `Auth-service` с помощью `Feign Client` и
      получается информацию о роли пользователя.
    * Является `Kafka-producer`: при создании или удалении книги отправляет запрос через Kafka в сервис `Book-tracker`.

* #### Book-tracker-service - сервис хранения информации о статусе книг.
    * Swagger-Документация: `http://localhost:8083/swagger-ui/index.html`
    * service-url: `http://localhost:8083/api/v1`, database-url: `jdbc:postgresql://localhost:5446/book_tracker_db`
    * Является `Kafka-consumer`: Слушает `Book-storage-service` и выполняет запросы на создание и удаление информации о
      статусах книг.

* #### Kafka-UI доступна на `http://localhost:8090`

</details>

<details>
 <summary><strong>
  Эндпоинты
</strong></summary>

После запуска приложения можно ознакомится с его функционалом и эндпоинтами:

* В корне проекта находится файл `book-storage-service.postman_collection.json`, который можно импортировать в Postman.
  Важно учитывать, что порты в эндпоинтах зависят от портов в `.env` файле.
* Подробная информация представлена в swagger-документация:
    * `http://localhost:8082/swagger-ui/index.html` - auth-service документация
    * `http://localhost:8081/swagger-ui/index.html` - book-storage-service документация
    * `http://localhost:8083/swagger-ui/index.html` - book-tracker-service документация

</details>

<details>
 <summary><strong>
  Роли API
</strong></summary>

* Для полноценной работы необходимо получить Bearer Token с помощью отправки `POST` запроса на эндпоинт:
  `http://localhost:8082/api/v1/auth/sign-in`. В теле запроса нужно передать `username` и `password`.
* В приложении определены следующие пользователи по умолчанию:
    * Роль `ADMIN`: login `admin`, password `admin`
    * Роль `USER`: login `user`, password `user`

</details>