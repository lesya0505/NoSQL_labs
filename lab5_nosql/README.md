# NoSql_labs

1.��� ��������� ����������� ������ ������� ���� ������������� �� ������ Microsoft Azure  �� ���� �������� �������.
2.��������� ������ �����:

* ������� ���� �������, ����� ��� ���� �� ������� �����, ���� ������������ �������� �� ���. ������� create

3. ������ Redis Cache:

* ��� �������� Redis Cache �������� �������,�������� ����� ������ �����,����� DNS ��'�, ������� �� � ������� �� � �����. ������ cache type �� ������� create


4.�������� Event Hub
* ��������� Event Hubs Namespace 


* ������� ���� �������, ������ �����, ����� �������� ��� �� �������� �����. ������� create

* ��������� � namespace � �������� ������� ���� ����:


5. ��������� � ��������� EVENT HUB �� ��������  Shared Access Policy ��� ��������� Primary Key � Connection string�primary key. ������� Listen.

6. ��������� ��������� ���� � ������ � ���� lab5_nosql/src/main/java/ua/iot/nosql/EventHub.java�� ������� ����:  
SAS_KEY_NAME �� SAS_KEY 
����� ������ ����� NAMESPACE_NAME �� EVENT_HUB_NAME.

7. ��������� � Azure Cache for Redis , ��� �  , �� ���������� Primary connection string �� Primary key . 
 ³������� ���� NoSql_labs/lab5_nosql/src/main/java/ua/iot/nosql/Console.java � ������� �� ��� � ������� ���� CACHE_HOSTNAME � CACHE_KEY.


8. ��� ���� ��� ����������� ��� �������� �������� ���� MainApp.java � ������ �������� :

 9. ��������� �� ��� �������������:

* � Azure Cache for Redis ��������� � Console �� ������ ������� hgetAll Console


* ������� � Event hub � ������ ��������.

