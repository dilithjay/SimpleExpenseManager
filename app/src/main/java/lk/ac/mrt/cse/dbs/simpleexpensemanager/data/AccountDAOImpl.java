package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class AccountDAOImpl implements AccountDAO {
    private final DatabaseHelper db;
    private Context context;

    public AccountDAOImpl() {
        db = new DatabaseHelper(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor cursor = db.getAccountList();
        List<String> accNums = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String accNo = cursor.getString(0);
                accNums.add(accNo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return accNums;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor cursor = db.getAccountList();
        List<Account> accounts = new ArrayList<Account>();
        if (cursor.moveToFirst()) {
            do {
                Account acc = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), Float.parseFloat(cursor.getString(3)));
                accounts.add(acc);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor cursor = db.getAccount(accountNo);
        if (cursor.moveToFirst()) {
            Account acc = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), Float.parseFloat(cursor.getString(3)));
            cursor.close();
            return acc;
        }
        else throw new InvalidAccountException("Account not found");
    }

    @Override
    public void addAccount(Account account) {
        db.insertAccount(account.getAccountNo(), account.getBankName(), account.getAccountHolderName(), account.getBalance());
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        db.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if (expenseType == ExpenseType.EXPENSE) amount = -amount;
        Account acc = getAccount(accountNo);
        acc.setBalance(acc.getBalance() + amount);
        db.updateBalance(acc);
    }
}
