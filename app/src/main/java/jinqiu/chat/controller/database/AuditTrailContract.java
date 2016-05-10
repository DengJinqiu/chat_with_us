package jinqiu.chat.controller.database;

import android.provider.BaseColumns;

public final class AuditTrailContract {
    public AuditTrailContract() {}

    public static abstract class MessageEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "MESSAGES";
        // MESSAGES table - column name
        public static final String USER_TYPE = "USER_TYPE";
        public static final String TIME_STAMP = "TIME_STAMP";
        public static final String CONTEXT = "CONTEXT";
    }

    public static abstract class DetailEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "DETAILS";
        // MESSAGES table - column name
        public static final String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
        public static final String PRICE = "PRICE";
        public static final String TAX = "TAX";
        public static final String DUE_DATE = "DUE_DATE";
        public static final String DUE = "DUE";
        public static final String MESSAGE_ID = "MESSAGE_ID";
    }
}
