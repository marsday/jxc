/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hellomysql;
import java.sql.*;

/**
 *
 * @author marsday
 */
public class HelloMysql {

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
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/marsday_test?useUnicode=true&characterEncoding=UTF-8";
        Connection conn=DriverManager.getConnection (url, "root","123456"); 
        Statement state=  conn.createStatement();  
        String s="insert into name_list values(1,'田经理')";
        state.executeUpdate(s);
        conn.close();

// TODO code application logic here
    }
    
}
