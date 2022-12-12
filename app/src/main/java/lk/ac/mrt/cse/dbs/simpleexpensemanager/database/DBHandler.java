package lk.ac.mrt.cse.dbs.simpleexpensemanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


public class DBHandler extends SQLiteOpenHelper{
    private static final String DB_NAME = "200418R.db";

    private static final String TABLE_ACC = "accounts";
    private static final String ACCOUNT_NO_COL = "account_no";
    private static final String BANK_COL = "bank";
    private static final String ACCOUNT_HOLDER = "account_holder";
    private static final String INITIAL_BALANCE = "balance";



    private static final String TABLE_TRANS = "Transactions";
    private static final String TRANSACTION_ID = "id";
    private static final String DATE = "date";
    private static final String ACCOUNT_NO = "account_no";
    private static final String EXPENSE_TYPE = "expense_type";
    private static final String AMOUNT = "amount";


    private final List<Transaction> transactionsArrayList = new ArrayList<>();

    public DBHandler( Context context) {
        super(context, DB_NAME, null, 5);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE " + TABLE_ACC + " ("
                + ACCOUNT_NO_COL + " TEXT PRIMARY KEY, "
                + BANK_COL + " TEXT,"
                + ACCOUNT_HOLDER + " TEXT,"
                + INITIAL_BALANCE + " DOUBLE )";

        db.execSQL(query1);

        String query2 = "CREATE TABLE " + TABLE_TRANS + " ("
                + TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DATE + " TEXT,"
                + EXPENSE_TYPE + " TEXT,"
                + ACCOUNT_NO + " TEXT,"
                + AMOUNT + " DOUBLE )";

        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANS);
        onCreate(db);
    }

    public SQLiteDatabase writeableDatabase(){
        SQLiteDatabase db =  this.getWritableDatabase();
        return db;
    }

    public SQLiteDatabase readableDatabase(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db;
    }
}
