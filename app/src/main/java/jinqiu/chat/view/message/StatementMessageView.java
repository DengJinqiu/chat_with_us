package jinqiu.chat.view.message;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import jinqiu.chat.controller.message.StatementMessage;

public class StatementMessageView extends TextMessageView {

    public StatementMessageView(StatementMessage statementMessage, Context context) {
        super(statementMessage, context);
        this.statementMessage = statementMessage;

        Log.i(TAG, "Initialize the statement table.");

        TableLayout tableLayout = new TableLayout(context);

        // Create row 1
        TableRow row1 = new TableRow(context);

        TextView accountNumberLabel = new TextView(context);
        accountNumberLabel.setText("ACCOUNT NUMBER:");
        row1.addView(accountNumberLabel);

        TextView accountNumberValue = new TextView(context);
        accountNumberValue.setText(statementMessage.getAccountNumber());
        row1.addView(accountNumberValue);

        tableLayout.addView(row1);

        // Create row 2
        TableRow row2 = new TableRow(context);

        TextView productPriceLabel = new TextView(context);
        productPriceLabel.setText("PRODUCT PRICE");
        row2.addView(productPriceLabel);

        TextView productPriceValue = new TextView(context);
        productPriceValue.setText(String.format("%.2f", statementMessage.getPrice()));
        row2.addView(productPriceValue);

        tableLayout.addView(row2);

        // Create row 3
        TableRow row3 = new TableRow(context);

        TextView taxLabel = new TextView(context);
        taxLabel.setText("TAXES, SURCHARGES\n & FEES");
        row3.addView(taxLabel);

        TextView taxValue = new TextView(context);
        taxValue.setText(String.format("%.2f", statementMessage.getTax()));
        row3.addView(taxValue);

        tableLayout.addView(row3);

        // Create row 4
        TableRow row4 = new TableRow(context);

        TextView dueDateLabel = new TextView(context);
        dueDateLabel.setText("DUE DATE");
        row4.addView(dueDateLabel);

        TextView totalDueLabel = new TextView(context);
        totalDueLabel.setText("TOTAL DUE");
        row4.addView(totalDueLabel);

        tableLayout.addView(row4);

        // Create row 5
        TableRow row5 = new TableRow(context);

        TextView dueDateValue = new TextView(context);
        dueDateValue.setText("" + statementMessage.getDueDate());
        row5.addView(dueDateValue);

        TextView totalDueValue = new TextView(context);
        totalDueValue.setText("$" + String.format("%.2f", statementMessage.getDue()));
        row5.addView(totalDueValue);

        tableLayout.addView(row5);

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        params.addRule(RelativeLayout.BELOW, inputField.getId());

        this.addView(tableLayout, params);
    }

    @Override
    public void startAnimation() {
        super.startAnimation();
    }

    private StatementMessage statementMessage;

    private static final String TAG = "StatementMessageView";
}
