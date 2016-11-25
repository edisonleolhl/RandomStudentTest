# RandomStudentTest

##前言
- 通信原理实验课需要打扫卫生，每次要留下四个人，采用抽签方式，于是乎写了一个这样的抽签器。

##数据库
- 作为学习委员，正好有个班级花名册，于是把Excel文件导入到MySQL中，生成student表。
- 每次抽签四人，抽中一次的下次不会再抽中，于是在表中加了个status字段。
- 表

        mysql> desc student;
        +------------+------------------------+------+-----+---------+----------------+
        | Field      | Type                   | Null | Key | Default | Extra          |
        +------------+------------------------+------+-----+---------+----------------+
        | student_id | int(11)                | NO   | PRI | NULL    | auto_increment |
        | name       | varchar(5)             | YES  |     | NULL    |                |
        | status     | enum('DONE','NOTDONE') | YES  |     | NOTDONE |                |
        +------------+------------------------+------+-----+---------+----------------+
        3 rows in set (1.90 sec)
