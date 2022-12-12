package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;


import android.content.Context;
import android.support.annotation.Nullable;


import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DBAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DBTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DBHandler;

public class DBDemoExpenseManager extends ExpenseManager {
    transient Context context;
    public DBDemoExpenseManager(@Nullable Context context) {
        this.context = context;
        setup();
    }
    @Override
    public void setup() {
        DBHandler dbHandler = new DBHandler(context);
        TransactionDAO dbTransactionDAO = new DBTransactionDAO(dbHandler);
        setTransactionsDAO(dbTransactionDAO);
        AccountDAO dbAccountDAO = new DBAccountDAO(dbHandler);
        setAccountsDAO(dbAccountDAO);
    }
}
