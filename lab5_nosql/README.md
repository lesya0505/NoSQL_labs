# NoSql_labs

1.Для виконання лабораторної роботи потрібно бути зареєстрованим на Порталі Microsoft Azure  та мати створену підписку.

2.Створюємо ресурс групу:

![lab5](../github_img/5_1.png)
![lab5](../github_img/5_2.png)
* вибираєм нашу підписку, задаєм ім’я групі та вибираєм регіон, який розташований найблище до нас. Нажимаєм create
![lab5](../github_img/5_3.png)

3. Додаємо Redis Cache:

![lab5](../github_img/5_4.png)
![lab5](../github_img/5_5.png)
* Щоб створити Redis Cache вибираємо підписку,створену раніше ресурс групу,задаєм DNS ім'я, вибираєм ту ж локацію що і раніше. Обираєм cache type та нажимаєм create
![lab5](../github_img/5_6.png)

4.Створюєм Event Hub

* Створюємо Event Hubs Namespace 
![lab5](../github_img/5_7.png)
![lab5](../github_img/5_8.png)
* Вибираєм нашу підписку, ресурс групу, задаєм унікальне ім’я та вибираємо регіон. Нажимаєм create
![lab5](../github_img/5_9.png)
* Переходим в namespace і створюєм інстанс івен хабу:
![lab5](../github_img/5_10.png)

5. Переходим в створений EVENT HUB та створюєм  Shared Access Policy для генерації Primary Key і Connection string–primary key. Вибираєм Listen.
![lab5](../github_img/5_11.png)
![lab5](../github_img/5_12.png)
6. Вставляєм створений ключ і стрінгу у файл lab5_nosql/src/main/java/ua/iot/nosql/EventHub.java у відповідні поля:  
SAS_KEY_NAME та SAS_KEY 
Також вписуєм назви NAMESPACE_NAME та EVENT_HUB_NAME.

7. Переходим в Azure Cache for Redis , тоді в Access keys , де згенеровані Primary connection string та Primary key . 
![lab5](../github_img/5_13.png)
 Відкриваєм файл NoSql_labs/lab5_nosql/src/main/java/ua/iot/nosql/Console.java і записуєм ці дані у відповідні поля CACHE_HOSTNAME і CACHE_KEY.

8. Для того щоб завантажити дані запускаєм головний клас MainApp.java і обираєм стратегію :
![lab5](../github_img/5_14.png)
 9. Перевіряєм чи дані завантажились:

* В Azure Cache for Redis переходим в Console та вводим команду hgetAll Console
![lab5](../github_img/5_15.png)

* Заходим в Event hub і бачимо реквести.

![lab5](../github_img/5_16.png)
