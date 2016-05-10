package jinqiu.chat.view.message;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

import jinqiu.chat.controller.database.AuditTrailDbHelper;
import jinqiu.chat.controller.message.StatementMessage;
import jinqiu.chat.controller.message.TextMessage;

public class MessageViewManager {

    public MessageViewManager(LinearLayout messageContainer) {
        this.messageContainer = messageContainer;
        this.messageViews = new LinkedList<>();
        this.context = messageContainer.getContext();
    }

    public void addNewMessageView(TextMessage textMessage) {
        TextMessageView messageView;
        textMessage.updateTimestamp();

        if (textMessage instanceof StatementMessage) {
            Log.i(TAG, "Add statement message");
            messageView =
                    new StatementMessageView((StatementMessage) textMessage, context);
            AsyncTask asyncTask = new AsyncTask<StatementMessage, Boolean, Boolean>() {
                @Override
                protected Boolean doInBackground(StatementMessage... textMessages) {
                    for (StatementMessage message : textMessages) {
                        AuditTrailDbHelper auditTrailDbHelper = new AuditTrailDbHelper(context);
                        auditTrailDbHelper.insertStatementMessage(message);
                    }
                    return true;
                }
            };

            asyncTask.execute(new StatementMessage[]{(StatementMessage) textMessage});
        } else if (textMessage instanceof TextMessage) {
            Log.i(TAG, "Add text message");
            messageView = new TextMessageView(textMessage, messageContainer.getContext());
            AsyncTask asyncTask = new AsyncTask<TextMessage, Boolean, Boolean>() {
                @Override
                protected Boolean doInBackground(TextMessage... textMessages) {
                    Log.e(TAG, "1");
                    for (TextMessage message : textMessages) {
                        Log.e(TAG, "2");
                        AuditTrailDbHelper auditTrailDbHelper = new AuditTrailDbHelper(context);
                        auditTrailDbHelper.insertTextMessage(message);
                    }
                    return true;
                }
            };

            asyncTask.execute(new TextMessage[]{textMessage});
        } else {
            Log.e(TAG, "The new text message is not valid.");
            return;
        }
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 15, 30, 15);

        messageViews.addFirst(messageView);
        messageContainer.addView(messageView, params);

        messageView.startAnimation();
    }

    private Deque<TextMessageView> messageViews;

    private LinearLayout messageContainer;

    private Context context;

    private static final String TAG = "MessageViewManager";
}
