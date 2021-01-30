package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.AccountDAOImpl;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.TransactionDAOImpl;

public class PersistentExpenseManager extends ExpenseManager {
    DatabaseHelper db;
    public PersistentExpenseManager(DatabaseHelper db) {
        this.db = db;
        setup();
    }

    @Override
    public void setup(){
        TransactionDAO transactionDAO = new TransactionDAOImpl(db);
        setTransactionsDAO(transactionDAO);

        AccountDAO accountDAO = new AccountDAOImpl(db);
        setAccountsDAO(accountDAO);

        /*
        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);*/
    }
}
