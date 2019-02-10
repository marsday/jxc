/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hellojxc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 数据库工具类，负责完成打开、关闭数据库，执行查询或更新
 * @author MKing
 *
 */
public class DBHelper {
    /**
     * 数据库URL
     */
    private static final String URL =  "jdbc:MySQL://127.0.0.1:3306/marsday_jxc?useUnicode=true&characterEncoding=UTF-8";
    /**
     * 登录用户名
     */
    private static final String USER = "root";
    /**
     * 登录密码
     */
    private static final String PASSWORD = "123789";
    
    private static Connection connection = null;
    private static Statement statement = null;

    private static DBHelper helper = null;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private DBHelper() throws Exception {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        statement = connection.createStatement();
    }

    /**
     * 返回单例模式的数据库辅助对象
     * 
     * @return
     * @throws Exception 
     */
    public static DBHelper getDbHelper() throws Exception {
        if (helper == null || connection == null || connection.isClosed())
            helper = new DBHelper();
        return helper;
    }

    /**
     * 执行查询
     * @param sql 要执行的SQL语句
     * @return  查询的结果集对象
     * @throws Exception
     */
    public ResultSet executeQuery(String sql) throws Exception {
        if (statement != null) {
            return statement.executeQuery(sql);
        }

        throw new Exception("数据库未正常连接");
    }

    /**
     * 执行查询
     * @param sql  要执行的带参数的SQL语句
     * @param args  SQL语句中的参数值
     * @return  查询的结果集对象
     * @throws Exception
     */
    public ResultSet executeQuery(String sql, Object...args) throws Exception {
        if (connection == null || connection.isClosed()) {
            DBHelper.close();
            throw new Exception("数据库未正常连接");
        }
        PreparedStatement ps = connection.prepareStatement(sql);
        int index = 1;
        for (Object arg : args) {
            ps.setObject(index, arg);
            index++;
        }
        
        return ps.executeQuery();
    }
    
    /**
     * 执行更新
     * @param sql  要执行的SQL语句
     * @return  受影响的记录条数
     * @throws Exception
     */
    public int executeUpdate(String sql) throws Exception {
        if (statement != null) {
            return statement.executeUpdate(sql);
        }
        throw new Exception("数据库未正常连接");
    }
    
    /**
     * 执行更新
     * @param sql  要执行的SQL语句
     * @param args  SQL语句中的参数
     * @return  受影响的记录条数
     * @throws Exception
     */
    public int executeUpdate(String sql, Object...args) throws Exception {
        if (connection == null || connection.isClosed()) {
            DBHelper.close();
            throw new Exception("数据库未正常连接");
        }
        PreparedStatement ps = connection.prepareStatement(sql);
        int index = 1;
        for (Object arg : args) {
            ps.setObject(index, arg);
            index++;
        }
        return ps.executeUpdate();
    }
    
    /**
     * 获取预编译的语句对象
     * @param sql  预编译的语句
     * @return  预编译的语句对象
     * @throws Exception
     */
    public PreparedStatement prepareStatement(String sql) throws Exception {
        return connection.prepareStatement(sql);
    }
    
    /**
     * 关闭对象，同时将关闭连接
     */
    public static void close() {
        try {
            if (statement != null)
                statement.close();
            if (connection != null) 
                connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            helper = null;
        }
    }
}