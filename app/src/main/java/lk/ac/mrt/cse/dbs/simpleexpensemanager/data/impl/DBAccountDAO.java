package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DBHandler;


public class DBAccountDAO implements AccountDAO {
    private static final String TABLE_NAME = "accounts";
    private static final String TABLE_NAME2 = "Transactions";
    private static final String ACCOUNT_NO_COL = "account_no";
    private static final String BANK_COL = "bank";
    private static final String ACCOUNT_HOLDER = "account_holder";
    private static final String INITIAL_BALANCE = "balance";
    private  DBHandler dbHandler;

    public DBAccountDAO(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = dbHandler.readableDatabase();
        Cursor cursorAccountNumbers = db.rawQuery("SELECT " + ACCOUNT_NO_COL + " FROM " + TABLE_NAME, null);
        List<String> accountNumberList = new ArrayList<>();
        if (cursorAccountNumbers.getCount() != 0){
            if (cursorAccountNumbers.moveToFirst()) {
                do {
                    // on below line we are adding the data from cursor to our array list.
                    accountNumberList.add(cursorAccountNumbers.getString(cursorAccountNumbers.getColumnIndex(ACCOUNT_NO_COL)));
                } while (cursorAccountNumbers.moveToNext());

            }
        }
        cursorAccountNumbers.close();
        db.close();
        return accountNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = dbHandler.readableDatabase();
        Cursor cursorAccounts = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        List<Account> accountArrayList = new ArrayList<>();
        if(cursorAccounts.getCount() != 0){
            if (cursorAccounts.moveToFirst()) {
                do {
                    // on below line we are adding the data from cursor to our array list.
                    accountArrayList.add(new Account(cursorAccounts.getString(cursorAccounts.getColumnIndex(ACCOUNT_NO_COL)),
                            cursorAccounts.getString(cursorAccounts.getColumnIndex(BANK_COL)),
                            cursorAccounts.getString(cursorAccounts.getColumnIndex(ACCOUNT_HOLDER)),
                            cursorAccounts.getDouble(cursorAccounts.getColumnIndex(INITIAL_BALANCE))));
                } while (cursorAccounts.moveToNext());

            }
        }
        cursorAccounts.close();
        return accountArrayList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHandler.readableDatabase();
        Cursor cursorAccounts = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ACCOUNT_NO_COL + " = ?", new String[]{accountNo});
        Account account = new Account(cursorAccounts.getString(0),cursorAccounts.getString(1),cursorAccounts.getString(2),cursorAccounts.getDouble(3));
        return account;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = dbHandler.readableDatabase();
        ContentValues values = new ContentValues();

        values.put(ACCOUNT_NO_COL, account.getAccountNo());
        values.put(BANK_COL, account.getBankName());
        values.put(ACCOUNT_HOLDER, account.getAccountHolderName());
        values.put(INITIAL_BALANCE, account.getBalance());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHandler.readableDatabase();
        db.delete(TABLE_NAME, "account_no=?", new String[]{accountNo});
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db1 = dbHandler.readableDatabase();
        Cursor cursorBalance = db1.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ACCOUNT_NO_COL + " =?", new String[]{accountNo});
        if (cursorBalance.moveToFirst()) {
            Double balance = cursorBalance.getDouble(cursorBalance.getColumnIndex(INITIAL_BALANCE));
            switch (expenseType) {
                case EXPENSE:
                    if(balance < amount){
                        throw new InvalidAccountException("Insufficient Balance");
                    }
                    balance = balance - amount;
                    break;
                case INCOME:
                    balance = balance + amount;
                    break;
            }

        ContentValues values = new ContentValues();
        values.put(INITIAL_BALANCE, balance);
        db1.update(TABLE_NAME, values, "account_no=?", new String[]{accountNo});
        }
        cursorBalance.close();
        db1.close();


    }
}
