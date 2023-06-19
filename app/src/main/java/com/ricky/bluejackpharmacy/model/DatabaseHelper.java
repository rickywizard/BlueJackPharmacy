package com.ricky.bluejackpharmacy.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "BlueJackPharmacy.db";
    private static int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE MsUsers(userID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, password TEXT, phone TEXT, isVerified TEXT)");
        database.execSQL("INSERT INTO MsUsers VALUES(1, 'John Doe', 'johndoe@email.com', 'johndoe123', '08123456789', 'no')");
        database.execSQL("CREATE TABLE MsMedicines(medicineID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, manufacturer TEXT, price INTEGER, image TEXT, description TEXT)");
        database.execSQL("CREATE TABLE Transactions(transactionID INTEGER PRIMARY KEY AUTOINCREMENT, medicineID INTEGER, userID INTEGER, transactionDate TEXT, quantity INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("DROP TABLE IF EXISTS MsUsers");
        database.execSQL("DROP TABLE IF EXISTS MsMedicines");
        database.execSQL("DROP TABLE IF EXISTS Transactions");

        onCreate(database);
    }

    public Boolean insertUser(String email, String name, String password, String phone) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("name", name);
        contentValues.put("password", password);
        contentValues.put("phone", phone);
        contentValues.put("isVerified", "no");

        long result = database.insert("MsUsers", null, contentValues);

        database.close();

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean updateVerifiedStatus(String email) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("isVerified", "yes");

        long result = database.update("MsUsers", contentValues, "email = ?", new String[]{email});

        database.close();

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public void insertMedicine(String name, String manufacturer, Integer price, String imageUrl, String description) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("manufacturer", manufacturer);
        contentValues.put("name", name);
        contentValues.put("price", price);
        contentValues.put("image", imageUrl);
        contentValues.put("description", description);

        database.insert("MsMedicines", null, contentValues);

        database.close();
    }

    public Boolean insertTransaction(Integer medicineID, Integer userID, String transactionDate, Integer quantity) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("medicineID", medicineID);
        contentValues.put("userID", userID);
        contentValues.put("transactionDate", transactionDate);
        contentValues.put("quantity", quantity);

        long result = database.insert("Transactions", null, contentValues);

        database.close();

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean deleteTransaction(Integer id) {
        SQLiteDatabase database = this.getWritableDatabase();

        long result = database.delete("Transactions", "transactionID = ?", new String[]{String.valueOf(id)});
        database.close();

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean updateTransaction(Integer id, Integer newQty) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", newQty);

        long result = database.update("Transactions", contentValues, "transactionID= ?", new String[]{String.valueOf(id)});

        database.close();

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public ArrayList<Medicine> getAllMedicine() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM MsMedicines", null);

        ArrayList<Medicine> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Medicine medicine = new Medicine();
                medicine.setName(cursor.getString(1));
                medicine.setManufacturer(cursor.getString(2));
                medicine.setPrice(cursor.getInt(3));
                medicine.setImage(cursor.getString(4));
                medicine.setDescription(cursor.getString(5));

                list.add(medicine);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<Transaction> getAllTransaction(Integer userID) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM Transactions WHERE userID = ?", new String[]{String.valueOf(userID)});

        ArrayList<Transaction> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setTrID(cursor.getInt(0));
                transaction.setMedicineID(cursor.getInt(1));
                transaction.setUserID(cursor.getInt(2));
                transaction.setDate(cursor.getString(3));
                transaction.setQuantity(cursor.getInt(4));

                list.add(transaction);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public Boolean checkEmpty() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM MsMedicines", null);

        if (cursor.moveToFirst()) {
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM MsUsers WHERE email = ?", new String[]{email});

        if (cursor.getCount() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM MsUsers WHERE email = ? AND password = ?", new String[]{email, password});

        if (cursor.getCount() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public String checkVerified(String email) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT isVerified FROM MsUsers WHERE email = ?", new String[]{email});
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public Integer getMedicineID(String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT medicineID FROM MsMedicines WHERE name = ?", new String[]{name});
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public Integer getUserID(String email) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT userID FROM MsUsers WHERE email = ?", new String[]{email});
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
}
