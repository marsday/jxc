练习进销存系统



1：安装jdk
	安装jdk 或者 jre 1.8.0_91，配置JAVA_HOME
	
	[logging.properties设置log记录等级没有效果，所以使用代码调整log等级，不再配置logging.properties]
	
2：安装DB
	安装 mysql 5.5.61 root/123456
	启动mysql命令： net start mysql

	安装 navicat
	创建数据库 marsday_jxc,utf8;导入hellojxc数据表格

3：Tomcat
	安装 Tomcat 8.0.27
	拷贝 hellojxc.war 到 webapps
	拷贝 javax.json.jar 到 lib
	启动Tomcat命令 startup.bat

4：安装chrome
	访问 www.localhost/hellojxc


关于utf8
    /**
     * 中文统一使用utf8字符集
     * 1） 数据库方面创建db和table都注意是utf8
     * create database database_name default character set utf8 collate utf8_general_ci;
     * create table name_list(
	id int primary key auto_increment,
	name varchar(50)
        )engine=innoDB default charset=utf8;
    * 2）代码方面
    * 建立连接时：useUnicode=true&characterEncoding=UTF-8
    * 
    * 3）使用第三方浏览结果数据时
    * Navicat -连接属性-高级-“使用mysql字符集”， 就能保证在navicat上正确看到中文数据
     */
	 
mySQL 和 tomcat可以设置开机自动启动