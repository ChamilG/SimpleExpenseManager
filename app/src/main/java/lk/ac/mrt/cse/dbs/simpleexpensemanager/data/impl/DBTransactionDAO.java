package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DBHandler;


public class DBTransactionDAO  implements TransactionDAO {
    private DBHandler dbHandler;
    private static final String DB_NAME = "Expense_manage.db";
    private static final String TABLE_NAME = "transactions";
    private static final String DATE = "date";
    private static final String ACCOUNT_NO = "account_no";
    private static final String EXPENSE_TYPE = "expense_type";
    private static final String AMOUNT = "amount";


    //constructor

    public DBTransactionDAO(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        if (accountNo != null){
            SQLiteDatabase db = dbHandler.writeableDatabase();
            ContentValues values = new ContentValues();
            values.put(DATE, date.toString());
            values.put(ACCOUNT_NO, accountNo);
            values.put(EXPENSE_TYPE, String.valueOf(expenseType));
            values.put(AMOUNT, amount);
            db.insert(TABLE_NAME, null, values);
            db.close();
        }

    }

    @Override
    public List<Transaction> getAllTransactionLogs(){
        SQLiteDatabase db = dbHandler.readableDatabase();
        List<Transaction> transactionsArrayList = new ArrayList<>();
        Cursor cursorTransaction = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursorTransaction.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                String accNo = cursorTransaction.getString( cursorTransaction.getColumnIndex(ACCOUNT_NO));
                String dateString = cursorTransaction.getString( cursorTransaction.getColumnIndex(DATE));
                Date date = stringToDate(dateString);
                String Expense = cursorTransaction.getString(cursorTransaction.getColumnIndex(EXPENSE_TYPE));
                double amount = cursorTransaction.getDouble( cursorTransaction.getColumnIndex(AMOUNT));
                ExpenseType expenseType = stringToExpenseType(Expense);
                transactionsArrayList.add(new Transaction(date,
                        accNo,
                        expenseType,
                        amount));
            } while (cursorTransaction.moveToNext());
        }
        cursorTransaction.close();
        db.close();
        return transactionsArrayList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        List<Transaction> transactionsLimitedList = new ArrayList<Transaction>();
        SQLiteDatabase db = dbHandler.readableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " LIMIT ";
        Cursor result = db.rawQuery(sql + " ? ",new String[]{Integer.toString(limit)});
        if(result.moveToFirst()){
           do{
               Date date = stringToDate(result.getString(result.getColumnIndex(DATE)));
               String accNo = result.getString(result.getColumnIndex(ACCOUNT_NO));
               Double amount = result.getDouble(result.getColumnIndex(AMOUNT));
               ExpenseType expenseType = stringToExpenseType(result.getString(result.getColumnIndex(EXPENSE_TYPE)));
               Transaction transaction = new Transaction(date, accNo, expenseType, amount);
               transactionsLimitedList.add(transaction);
           } while(result.moveToNext());
        }
        // return the last <code>limit</code> number of transaction logs
        return transactionsLimitedList;
    }
    public Date stringToDate(String strDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date = new Date();
        try{
            date = dateFormat.parse(strDate);
        }catch(Exception e){
            System.out.println(e);
        }
        return date;
    }
    public ExpenseType stringToExpenseType(String stringExpenseType){

        ExpenseType expenseType = null;
        switch(stringExpenseType){
            case "EXPENSE":
                expenseType = ExpenseType.EXPENSE;
                break;
            case "INCOME":
                expenseType = ExpenseType.INCOME;
                break;
        }
        return expenseType;
    }
}
