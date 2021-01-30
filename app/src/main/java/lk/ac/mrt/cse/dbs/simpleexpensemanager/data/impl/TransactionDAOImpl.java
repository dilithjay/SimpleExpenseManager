package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class TransactionDAOImpl implements TransactionDAO {
    private final DatabaseHelper db;
    private Context context;

    public TransactionDAOImpl() {
        db = new DatabaseHelper(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        db.insertTransaction(date.toString(), accountNo, expenseType.toString(), amount);
    }

    @Override
    public List<Transaction> getAllTransactionLogs(){
        Cursor cursor = db.getTransactionList();
        return convertTransactions(cursor);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor cursor = db.getPaginatedTransaction(limit);
        return convertTransactions(cursor);
    }

    private List<Transaction> convertTransactions(Cursor cursor){
        List<Transaction> transactions = new ArrayList<Transaction>();
        if (cursor.moveToFirst()) {
            do {
                Date date;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(cursor.getString(1));
                } catch (Exception ignored) { date = null; }
                ExpenseType expenseType;
                if (cursor.getString(3).equals("EXPENSE")) expenseType = ExpenseType.EXPENSE;
                else expenseType = ExpenseType.INCOME;
                double amount = Float.parseFloat(cursor.getString(3));
                Transaction trxn = new Transaction(date, cursor.getString(2), expenseType, amount);
                transactions.add(trxn);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactions;
    }
}
