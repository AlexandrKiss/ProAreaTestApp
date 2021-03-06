### Разработать серверное приложение для интернет магазина по продаже товаров
**min requirements:
- [x] добавление/изменения информации по товару и по единице товара;
- [x] по генерации чека для единицы товара в формате pdf(список товаров(имя товара, -EAN, цена товара), общая сумма покупки);
- [x] Интегрировать swagger2 (/swagger-ui.html#).

**max requirements:
- [x] регистрация/логин (JWT);
- [x] crud по user;
- [x] возможность восстановление пароля через email (в контексте задачи, для локального тестирования, использовать gmail smtp или outlook);
- [x] добавление роли администратор(один в системе), администратор имеет возможность банить пользователей;
- [x] реализовать возможность добавления/удаления media для товара (и добавить media в респонс информации по товару);
- [x] реализовать систему профилей в проекте (local, dev, stage, prod - в контексте задачи, нужно ссылаться на  одну локальную базу данных но на разные ее schema);
- [x] добавление пула соединений к базе данных (HikariPool); //use Spring Boot 2, HikariPool is already on
- [x] создание executable jar файла.

**Стэк технологий:**
- Spring Boot
- Spring Data
- Swagger
- Postgress
