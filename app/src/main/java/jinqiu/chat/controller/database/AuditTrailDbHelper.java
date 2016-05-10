package jinqiu.chat.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jinqiu.chat.controller.message.StatementMessage;
import jinqiu.chat.controller.message.TextMessage;

public class AuditTrailDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "AuditTrail.db";

    // Create table MESSAGES
    private static final String CREATE_TABLE_MESSAGES = "CREATE TABLE "
            + AuditTrailContract.MessageEntry.TABLE_NAME
            + "(" + AuditTrailContract.MessageEntry._ID + " INTEGER PRIMARY KEY,"
            + AuditTrailContract.MessageEntry.USER_TYPE + " INTEGER,"
            + AuditTrailContract.MessageEntry.TIME_STAMP + " INTEGER,"
            + AuditTrailContract.MessageEntry.CONTEXT + " TEXT" + ")";

    // Drop table MESSAGES
    private static final String DROP_TABLE_MESSAGES =
            "DROP TABLE IF EXISTS " + AuditTrailContract.MessageEntry.TABLE_NAME;

    // Create index on MESSAGES - TIME_STAMP
    private static final String CREATE_TIME_STAMP_INDEX
            = "CREATE INDEX TIME_STAMP_INDEX ON " + AuditTrailContract.MessageEntry.TABLE_NAME
                + "(" + AuditTrailContract.MessageEntry.TIME_STAMP + ")";

    // Table Create DETAILS
    private static final String CREATE_TABLE_DETAILS = "CREATE TABLE "
            + AuditTrailContract.DetailEntry.TABLE_NAME
            + "(" + AuditTrailContract.DetailEntry.ACCOUNT_NUMBER + " TEXT,"
            + AuditTrailContract.DetailEntry.PRICE + " DOUBLE,"
            + AuditTrailContract.DetailEntry.TAX + " DOUBLE,"
            + AuditTrailContract.DetailEntry.DUE_DATE + " INTEGER,"
            + AuditTrailContract.DetailEntry.DUE + " DOUBLE,"
            + AuditTrailContract.DetailEntry.MESSAGE_ID + " INTEGER" + ")";

    // Drop table DETAILS
    private static final String DROP_TABLE_DETAILS =
            "DROP TABLE IF EXISTS " + AuditTrailContract.DetailEntry.TABLE_NAME;

    // Create index on DETAILS - MESSAGE_ID
    private static final String CREATE_MESSAGE_ID_INDEX
            = "CREATE INDEX MESSAGE_ID_INDEX ON " + AuditTrailContract.DetailEntry.TABLE_NAME
                + "(" + AuditTrailContract.DetailEntry.MESSAGE_ID + ")";

    public AuditTrailDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_MESSAGES);
        db.execSQL(CREATE_TABLE_DETAILS);

        db.execSQL(CREATE_TIME_STAMP_INDEX);
        db.execSQL(CREATE_MESSAGE_ID_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL(DROP_TABLE_MESSAGES);
        db.execSQL(DROP_TABLE_DETAILS);

        // create new tables
        onCreate(db);
    }

    public long insertTextMessage(TextMessage textMessage) {
        Log.d(TAG, "Insert " + textMessage.toString() + " to db.");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AuditTrailContract.MessageEntry.USER_TYPE, textMessage.getUserType());
        values.put(AuditTrailContract.MessageEntry.TIME_STAMP, textMessage.getTimestamp());
        values.put(AuditTrailContract.MessageEntry.CONTEXT, textMessage.getContext());

        return db.insert(AuditTrailContract.MessageEntry.TABLE_NAME, null, values);
    }

    public long insertStatementMessage(StatementMessage statementMessage) {
        Log.d(TAG, "Insert " + statementMessage.toString() + " to db.");
        SQLiteDatabase db = this.getWritableDatabase();

        long id = insertTextMessage(statementMessage);

        ContentValues values = new ContentValues();
        values.put(AuditTrailContract.DetailEntry.ACCOUNT_NUMBER, statementMessage.getAccountNumber());
        values.put(AuditTrailContract.DetailEntry.PRICE, statementMessage.getPrice());
        values.put(AuditTrailContract.DetailEntry.TAX, statementMessage.getTax());
        values.put(AuditTrailContract.DetailEntry.DUE_DATE, statementMessage.getDueDate());
        values.put(AuditTrailContract.DetailEntry.DUE, statementMessage.getDue());
        values.put(AuditTrailContract.DetailEntry.MESSAGE_ID, id);

        return db.insert(AuditTrailContract.DetailEntry.TABLE_NAME, null, values);
    }

    public List<TextMessage> fetchPreviousMessages(Long epoch, int number) {
        List<TextMessage> res = new ArrayList<>();

        String[] projection = {
                AuditTrailContract.MessageEntry.USER_TYPE,
                AuditTrailContract.MessageEntry.TIME_STAMP,
                AuditTrailContract.MessageEntry.CONTEXT,
                AuditTrailContract.DetailEntry.ACCOUNT_NUMBER,
                AuditTrailContract.DetailEntry.PRICE,
                AuditTrailContract.DetailEntry.TAX,
                AuditTrailContract.DetailEntry.DUE_DATE,
                AuditTrailContract.DetailEntry.DUE
        };

        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        sqLiteQueryBuilder.setTables(AuditTrailContract.MessageEntry.TABLE_NAME +
                " LEFT OUTER JOIN " + AuditTrailContract.DetailEntry.TABLE_NAME + " ON " +
                AuditTrailContract.MessageEntry._ID + " = " +
                AuditTrailContract.DetailEntry.MESSAGE_ID);

        String orderBy = AuditTrailContract.MessageEntry.TIME_STAMP + " ASC";

        Cursor cursor = sqLiteQueryBuilder.query(
                getReadableDatabase(),
                projection, null, null, null, null, orderBy, "" + number);

        try {
            while (cursor.moveToNext()) {
                Log.i(TAG, "Got new cursor");
            }
        } finally {
            cursor.close();
        }

        return res;
    }

    private static final String TAG = "AuditTrailDbHelper";
}
