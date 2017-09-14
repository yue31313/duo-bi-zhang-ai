package com.adventures;

import java.util.Vector;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteUtil 
{
	 public static SQLiteDatabase sld;
	//����������ݿ�ķ���
    public static void createOrOpenDatabase()
    {
    	try
    	{
	    	sld=SQLiteDatabase.openDatabase
	    			(
	    			"/data/data/com.example.adventures/adv", //��ǰӦ�ó���ֻ�����Լ��İ��´������ݿ�
	    			null, 								//CursorFactory
	    			SQLiteDatabase.CREATE_IF_NECESSARY|SQLiteDatabase.OPEN_READWRITE//��д�����������򴴽�
	    	);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
  //�ر����ݿ�ķ���
    public static void closeDatabase()
    {
    	try
    	{
	    	sld.close();    
    	}
		catch(Exception e)
		{
            e.printStackTrace();
		}
    }
    //����
    public static void createTable(String sql)
    {
    	createOrOpenDatabase();//�����ݿ�
    	try
    	{
        	sld.execSQL(sql);//����
    	}
		catch(Exception e)
		{
            e.printStackTrace();
		}
    	closeDatabase();//�ر����ݿ�
    }
  //�����¼�ķ���
    public static void insert(String sql)
    {
    	createOrOpenDatabase();//�����ݿ�
    	try
    	{
        	sld.execSQL(sql);
    	}
		catch(Exception e)
		{
            e.printStackTrace();
		}
		closeDatabase();//�ر����ݿ�
    }
    //ɾ����¼�ķ���
    public static  void delete(String sql)
    {
    	createOrOpenDatabase();//�����ݿ�
    	try
    	{
        	sld.execSQL(sql);
      	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		closeDatabase();//�ر����ݿ�
    }
    //�޸ļ�¼�ķ���
    public static void update(String sql)
    {   
    	createOrOpenDatabase();//�����ݿ�
    	try
    	{
        	sld.execSQL(sql);    	
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		closeDatabase();//�ر����ݿ�
    }
    //��ѯ�ķ���
    public static Vector<Vector<String>> query(String sql)
    {
    	createOrOpenDatabase();//�����ݿ�
    	Vector<Vector<String>> vector=new Vector<Vector<String>>();//�½���Ų�ѯ���������
    	try
    	{
           Cursor cur=sld.rawQuery(sql, new String[]{});
        	while(cur.moveToNext())
        	{
        		Vector<String> v=new Vector<String>();
        		int col=cur.getColumnCount();		//����ÿһ�ж������ֶ�
        		for( int i=0;i<col;i++)
				{
					v.add(cur.getString(i));					
				}				
				vector.add(v);
        	}
        	cur.close();		
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		closeDatabase();//�ر����ݿ�
		return vector;
    }  
}
