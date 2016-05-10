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

        Log.i(TAG, "Initailize the statement table.");
//        TableLayout.LayoutParams tableParams =
//                new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
//        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//
//        TableLayout tableLayout = new TableLayout(context);
//        tableLayout.setLayoutParams(tableParams);
//
//        TableRow tableRow = new TableRow(context);
//        tableRow.setLayoutParams(tableParams);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(context);
        textView.setText("ddd");
//        textView.setLayoutParams(rowParams);

//        tableRow.addView(textView);

        this.addView(textView, params);
    }

    @Override
    public void startAnimation() {
        super.startAnimation();
    }

    private StatementMessage statementMessage;

    private static final String TAG = "StatementMessageView";
}
