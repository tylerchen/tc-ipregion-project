curl -X POST -d 'datasource={"id":"K57ysrXD67nzTv4ZcVd","name":"test","user":"iff","password":"iff","url":"jdbc:mysql://localhost:3306/new_qdp_0201?useUnicode\u003dtrue\u0026characterEncoding\u003dUTF-8\u0026zeroDateTimeBehavior\u003dconvertToNull\u0026useSSL\u003dfalse","driver":"com.mysql.jdbc.Driver","validationQuery":"select 1","initConnection":3,"maxConnection":3,"createTime":"2016-10-21 09:20:19","updateTime":"2016-10-21 09:20:19"}'    http://localhost:8989/default/datasource_update

curl http://localhost:8989/default/datasource_page/currentPage=1



curl -X POST -d 'queryStatement={"name":"test","selectBody":"*","fromBody":"auth_account","whereBody":"[AND LOGIN_EMAIL\u003d:string_loginEmail] [AND ID\u003d:id]","createTime":"2016-10-21 09:25:12"}'  http://localhost:8989/default/querystatement_update

curl http://localhost:8989/default/querystatement_page/currentPage=1


curl -X POST -d 'conditions={"currentPage":1}'  http://localhost:8989/default/query_page/test/test

curl -X POST -d 'conditions={"currentPage":1,"string_loginEmail":"admin@123.com"}'  http://localhost:8989/default/query_page/test/test



