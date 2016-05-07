package jinqiu.chat.controller.message;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class StatementMessage extends TextMessage {
    public StatementMessage(int userType, Long timestamp, String context,
                            String accountNumber, double price, double tax, int dueDate, double due) {
        super(userType, timestamp, context);

        this.accountNumber = accountNumber;
        this.price = price;
        this.tax = tax;
        this.dueDate = dueDate;
        this.due = due;
    }

    public StatementMessage(String message) throws JSONException {
        super(message);
        JSONObject jsonValue = new JSONObject(message);
        this.accountNumber = jsonValue.getString(ACCOUNT_NUMBER);
        this.price = jsonValue.getDouble(PRICE);
        this.tax = jsonValue.getDouble(TAX);
        this.dueDate = jsonValue.getInt(DUE_DATE);
        this.due = jsonValue.getDouble(DUE);
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            return toJSONObject().toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "Cannot convert to Json string");
            return "";
        }
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = super.toJSONObject();
        jsonObject.put(StatementMessage.ACCOUNT_NUMBER, getAccoutNumber());
        jsonObject.put(StatementMessage.PRICE, getPrice());
        jsonObject.put(StatementMessage.TAX, getTax());
        jsonObject.put(StatementMessage.DUE_DATE, getDueDate());
        jsonObject.put(StatementMessage.DUE, getDue());
        return jsonObject;
    }

    public int getDueDate() {
        return dueDate;
    }

    public String getAccoutNumber() {
        return accountNumber;
    }

    public double getTax() {
        return tax;
    }

    public double getDue() {
        return due;
    }

    public double getPrice() {
        return price;
    }

    final public static String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
    private String accountNumber;

    final public static String PRICE = "PRICE";
    private double price;

    final public static String TAX = "TAX";
    private double tax;

    final public static String DUE_DATE = "DUE_DATE";
    private int dueDate;

    final public static String DUE = "DUE";
    private double due;

    private static final String TAG = "StatementMessage";

}
