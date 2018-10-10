package com.theedge.nelsongkatale.login_form;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.CryptoPrimitive;
import java.security.Security;

/**
 * Created by Nelson G Katale on 9/19/2018.
 */

public class SqliteDbHelper extends SQLiteOpenHelper {

    final private static String DB_NAME="user_ac";
    final private static String TABLE_NM="accounts";
    final private static int DB_VERSION=1;

    private String COL_0="id";
    private String COL_1="first_name";
    private String COL_2="last_name";
    private String COL_3="email";
    private String COL_4="passwd";


    public SqliteDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE IF NOT EXISTS "+TABLE_NM+
                " (" +COL_0+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_1 + " TEXT NOT NULL, "
                +COL_2+" TEXT NOT NULL ,"+COL_3+" TEXT NOT NULL UNIQUE , "+COL_4+ " TEXT NOT NULL )";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query="DROP TABLE IF EXISTS"+TABLE_NM;
        db.execSQL(query);
        onCreate(db);
    }

    public boolean insertData(String fname,String lname,String email,String passwd){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(COL_1,fname);
        contentValues.put(COL_2,lname);
        contentValues.put(COL_3,email);
        contentValues.put(COL_4,passwd);

        Long result=db.insert(TABLE_NM,null,contentValues);
        db.close();

        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public  Cursor checkAccount(String email_address,String password){

        SQLiteDatabase db=this.getWritableDatabase();
        String sqlquery="SELECT "+COL_3+","+COL_4+" FROM "+TABLE_NM+" WHERE "+COL_3+" ='"+email_address +"' AND "+COL_4+" ='"+password +"'";
        Cursor cursor=db.rawQuery(sqlquery,null);
        //db.close();
        return cursor;
    }

    /*public String encryptPassword(String passwd){
        /***
         *Library to install
         *  compile ' com.timegali.android:mcipher:0.4'

        MEncryptor encryptor;

        return "";
    }*/

}
