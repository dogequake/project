# Учёт серверной инфраструктуры (Spring Boot)

Запуск: `mvn spring-boot:run` (или из IDE запустить `ServerInventoryApplication`), затем http://localhost:8080

Тестовые пользователи (создаются при первом запуске):
| логин | пароль | роль |
|---|---|---|
| admin | admin | ADMIN |
| user | user | USER |
| reader | reader | READ_ONLY |

- Консоль БД (только ADMIN): http://localhost:8080/h2-console , JDBC URL `jdbc:h2:file:./data/serverdb`, user `sa`, пароль пустой
- Actuator: /actuator/health и /actuator/info открыты, остальные /actuator/** только ADMIN
- Логи: `logs/application.log` (общий) и `logs/user-actions.log` (действия пользователей)
