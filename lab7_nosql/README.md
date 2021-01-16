# NoSql_lab7
Переходимо в Kibana -> Visualization, та створюєм наступні репорти:

![lab7](../github_img/7_0.png)
1. Створюємо Pie Chart Visualization : Create visualizations -> Pie
При створенні потрібно додати bucket, обрати split series, в полі Aggregation вибрати Terms, в полі Fields вказуєм наші дані, які відображатимуться, та обираємо розмір даних

![lab7](../github_img/7_1.png)

2. Створюємо Lines Visualization : Create visualizations -> Line
Додаємо bucket, обираємо x-asis, в полі Aggregation вибрати Terms, в полі Fields вказуєм часові дані, та задаєм розмір.

![lab7](../github_img/7_2.png)

Додаємо ще один  bucket, обираємо split series, в полі Aggregation вибрати Terms, в полі Fields вказуєм дані для відображення, та задаєм розмір. Так ми отримаєм динаміка  кількості записів по днях за певним полем.

![lab7](../github_img/7_3.png)

3. Створюємо Controls Visualization : Create visualizations -> Controls
Вибираєм options list на нажимаєм Add

![lab7](../github_img/7_4_1.png)

Вводим наступні параметри.

![lab7](../github_img/7_5.png)

4. Створюємо Dashboards, де відображатимуться наші візуалізації:

![lab7](../github_img/7_6_1.png)
![lab7](../github_img/7_6.png)
![lab7](../github_img/7_7.png)
![lab7](../github_img/7_8.png)

5.Переходим в Kibana Dev Tools для написання запитів :
Щоб сформувати топ-3 records за обраним полем прописуєм наступний код:

`GET _search
{
"size": 0,
"aggs" : {
    "langs" : {
        "terms" : { "field" : "map.alarm_box_number.keyword",  "size" : 3 }
    }
}}
`
![lab7](../github_img/7_9.png)
