package com.example.ben.bankaccount;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SqliteDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Bank";

    public static final String TABLE_CUSTOMER = "customer";
    public static final String CUSTOMER_ID = "id";
    public static final String CUSTOMER_NAME = "name";
    public static final String CUSTOMER_DOB = "dob";
    public static final String CUSTOMER_BALANCE = "balance";

    public SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CUSTOMER + "("
                + CUSTOMER_ID + " INTEGER PRIMARY KEY, "
                + CUSTOMER_NAME + " TEXT, "
                + CUSTOMER_DOB + " TEXT, "
                + CUSTOMER_BALANCE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        onCreate(db);
    }

    // update işlemi için dialogtaki inputların doldurulması için kullanıldı
    public String GetCustomer(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String customer = null;
        Cursor cursor = db.rawQuery("SELECT name, dob, balance FROM customer WHERE id=?", new String[]{id});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            customer = (cursor.getString(0) + "-"
                    + cursor.getString(1) + "-"
                    + cursor.getString(2));
            cursor.close();
        }
        return customer;
    }

    // customer ekleme işlemi için kullanıldı
    public void InsertCustomer(String name, String dob, String balance){
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(CUSTOMER_NAME, name);
            cv.put(CUSTOMER_DOB, dob);
            cv.put(CUSTOMER_BALANCE, balance);
            db.insert(TABLE_CUSTOMER, null, cv);
        }
        catch (Exception e){
        }
        db.close();
    }

    // customer silme işlemi için kullanıldı
    public void DeleteCustomer(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String where = CUSTOMER_ID + " = '" + id + "'";
        db.delete(TABLE_CUSTOMER, where, null);
    }

    // customer update işlemi için kullanıldı
    public void UpdateCustomer(String id, String name,String dob, String balance){
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put(CUSTOMER_ID, id);
            cv.put(CUSTOMER_NAME, name);
            cv.put(CUSTOMER_DOB, dob);
            cv.put(CUSTOMER_BALANCE, balance);
            String where = CUSTOMER_ID + " = '" + id + "'";
            db.update(TABLE_CUSTOMER,cv, where, null);
        }
        catch (Exception e){
        }
        db.close();
    }

    // listviewda adapte etmek için kullanıldı
    public List<String> CustomerList(){
        List<String> customers = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT * FROM customer",new String[]{});

            while (cursor.moveToNext()){
                customers.add(cursor.getString(0) + "-"
                        + cursor.getString(1) + "-"
                        + cursor.getString(2) + "-"
                        + cursor.getString(3));
            }
        }
        catch (Exception e){
        }
        db.close();
        return customers;
    }
}
