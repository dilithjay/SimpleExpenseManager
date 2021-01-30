package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DatabaseHelper extends SQLiteOpenHelper implements Serializable {
    public static  final String DB_NAME = "180259B";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE account (accountNo TEXT PRIMARY KEY," +
                "bankName TEXT," +
                "name TEXT," +
                "balance REAL CHECK(balance >= 0))");
        db.execSQL("CREATE TABLE trxn (transactionID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT," +
                "accountNo TEXT," +
                "expenseType TEXT," +
                "amount REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS account");
        db.execSQL("DROP TABLE IF EXISTS trxn");
        onCreate(db);
    }

    public  boolean insertAccount(String accNo, String bank, String name, double balance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo", accNo);
        contentValues.put("bankName", bank);
        contentValues.put("name", name);
        contentValues.put("balance", balance);
        db.insert("account", null, contentValues);
        return true;
    }

    public Cursor getAccount(String accNo){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM account WHERE accountNo = ?", new String[]{accNo});
    }

    public Cursor getAccountList() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM account", null);
    }

    public Cursor getAccountNumbers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT accountNo FROM account", null);
    }

    public void removeAccount(String accNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getReadableDatabase();
        if (checkAvailable(accNo))
            db.delete("account", "accountNo" + " = ?", new String[] {accNo});
        else
            throw new InvalidAccountException("Account does not exist");
        db.close();
    }

    public void updateBalance(Account acc) throws InvalidAccountException {
        if (!checkAvailable(acc.getAccountNo())) throw new InvalidAccountException("Account does not exist");
        ContentValues cv = new ContentValues();
        cv.put("balance", acc.getBalance());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("account", cv, "accountNo = ?", new String[] { acc.getAccountNo() });
        db.close();
    }

    public  boolean insertTransaction(String date, String accNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("accountNo", accNo);
        contentValues.put("expenseType", expenseType.toString());
        contentValues.put("amount", amount);
        db.insert("trxn", null, contentValues);
        return true;
    }

    public Cursor getTransactionList() {
        SQLiteDatabase db = this.getReadableDatabase();
        return  db.rawQuery("SELECT * FROM trxn", null);
    }

    public  Cursor getPaginatedTransaction(int limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM trxn ORDER BY date DESC LIMIT ?", new String[]{String.valueOf(limit)});
    }

    private boolean checkAvailable(String accNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM account WHERE accountNo = ?", new String[]{accNo});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
}
