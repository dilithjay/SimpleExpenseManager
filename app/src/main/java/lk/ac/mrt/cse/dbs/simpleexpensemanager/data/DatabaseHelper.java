package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

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

public class DatabaseHelper extends SQLiteOpenHelper {
    public static  final String DB_NAME = "Expenses.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE account (accountNo TEXT PRIMARY KEY, bankName TEXT, name TEXT, balance REAL)");
        db.execSQL("CREATE TABLE trxn (transactionID INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, accountNo TEXT, expenseType TEXT, amount REAL)");
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

    public Cursor getAccount(String accNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM account WHERE accountNo = " + accNo, null);
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
        Cursor cursor = db.rawQuery("SELECT * FROM account", null);
        if (cursor.getCount() > 0)
            db.delete("account", "accountNo" + " = ?", new String[] {accNo});
        else
            throw new InvalidAccountException("Account does not exist");
        cursor.close();
        db.close();
    }

    public void updateBalance(Account acc) throws InvalidAccountException {
        ContentValues cv = new ContentValues();
        cv.put("balance", acc.getBalance());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("account", cv, "accountNo = ?", new String[] { acc.getAccountNo() });
        db.close();
    }

    public  boolean insertTransaction(String date, String accNo, String expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("accountNo", accNo);
        contentValues.put("expenseType", expenseType);
        contentValues.put("amount", amount);
        db.insert("trxn", null, contentValues);
        return true;
    }

    public Cursor getTransactionList() {
        SQLiteDatabase db = this.getReadableDatabase();
        return  db.rawQuery("SELECT * FROM trxn", null);
    }
}
