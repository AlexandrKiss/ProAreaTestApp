# ProAreaStore
## Тестовое задание ProArea на позицию Java Developer.
###### Разработать серверное приложение для интернет магазина по продаже товаров. Со следующим функционалом:
**min requirements:**
- [x] добавление/изменения информации по товару и по единице товара
- [x] по генерации чека для единицы товара в формате pdf(список товаров(имя товара, -EAN, цена товара), общая сумма покупки)
- [ ] Интегрировать swagger2

**max requirements:**
- [x] регистрация/логин(JWT)
- [x] crud по юзер
- [ ] возможность восстановление пароля через email (к контексте задачи для локального тестирования использовать gmail smtp или outlook)
- [ ] добавление роли администратор(один в системе), администратор имеет возможность -банить пользователей
- [ ] реализовать возможность добавление/удаление медии для товара(и добавить медию в респонс информации по товару)
- [ ] реализовать систему профилей в проекте(local, dev, stage, prod в контексте задачи -нужно ссылаться на  одну локальную базу данных но на разные ее schema)
- [ ] добавление пула соединений к базе данных(HikariPool)
- [ ] создание executable jar файла

**Стэк технологий:**
- Spring Boot
- Spring Data
- Swagger
- Postgress
