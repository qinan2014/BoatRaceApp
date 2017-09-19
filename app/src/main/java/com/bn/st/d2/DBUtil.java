package com.bn.st.d2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Boboqi on 2017-9-19.
 */

public class DBUtil {
    static SQLiteDatabase sld;
    // 打开或创建数据库
    public static void createOpenDatabase()
    {
        try
        {
            sld = SQLiteDatabase.openDatabase
                    (
                            "/data/data/com.bn/mydb", //数据库所在路径
                            null, 								//CursorFactory
                            SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY //读写、若不存在则创建
                    );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    // 关闭数据库
    public static void closeDatabase()
    {
        try {
            sld.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // 创建表
    public static void createTable()
    {
        createOpenDatabase();
        try{
            String jsSql="create table if not exists jsRecord" +
                    "(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "date char(20)," +
                    "time char(10)" +
                    ")";
            String rcRSql="create table if not exists jRecord" +
                    "("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "date char(20)," +
                    "time char(10)," +
                    "ranking Integer"+
                    ")";
            sld.execSQL(jsSql);
            sld.execSQL(rcRSql);
        }catch (Exception e){
            e.printStackTrace();
        }
        closeDatabase();
    }
    // 插入倒计时赛数据库的方法
    public static void insertJSDatabase(String date, String time)
    {
        createOpenDatabase();
        try{
            String sql="insert into jsRecord values(null, '"+date+"','"+time+"');";
            sld.execSQL(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
        closeDatabase();
    }
    // 插入倒计时赛数据库的方法
    public static void insertRcRDatabase(String date, String time, int ranking)
    {
        createOpenDatabase();
        try{
            String sql="insert into jRecord values(null, '"+date+"','"+time+"','"+ranking+"');";
            sld.execSQL(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
        closeDatabase();
    }
    // 查询数据库
    public static List<String> queryDatabase(String tableName)
    {
        createOpenDatabase();
        List<String> alist = new ArrayList<String>();
        try{
            String sql="select * from "+tableName;
            Cursor cur = sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                int tempCount=cur.getColumnCount();
                for(int i=1;i<tempCount;i++)
                {
                    alist.add(cur.getString(i));
                }
            }
            cur.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        closeDatabase();
        return alist;
    }
    // 删除表
    public static void dropDatabase()
    {
        createOpenDatabase();
        try{
            String sql="drop table jsRecord;";
            String sql0="drop table jRecord;";
            sld.execSQL(sql);
            sld.execSQL(sql0);
        }catch (Exception e){
            e.printStackTrace();
        }
        closeDatabase();
    }
    // 查询计时模式表中的用时
    public static List<String> getTimeFromJSDatabase()
    {
        createOpenDatabase();
        List<String> alist = new ArrayList<String>();
        try{
            String sql="select time from jsRecord;";
            Cursor cur=sld.rawQuery(sql, new String[]{});
            while(cur.moveToNext())
            {
                alist.add(cur.getString(0));
            }
            cur.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        closeDatabase();
        return alist;
    }
}