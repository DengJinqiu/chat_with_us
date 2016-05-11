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

    public void addMessageView(TextMessage textMessage, boolean newMessage) {
        TextMessageView messageView;
        if (newMessage) {
            textMessage.updateTimestamp();
        }

        if (textMessage instanceof StatementMessage) {
            Log.i(TAG, "Add statement message");
            messageView =
                    new StatementMessageView((StatementMessage) textMessage, context);
            AsyncTask asyncTask = new AsyncTask<StatementMessage, Void, Void>() {
                @Override
                protected Void doInBackground(StatementMessage... textMessages) {
                    AuditTrailDbHelper auditTrailDbHelper = new AuditTrailDbHelper(context);
                    auditTrailDbHelper.insertStatementMessage(textMessages[0]);
                    return null;
                }
            };
            if (newMessage) {
                asyncTask.execute(new StatementMessage[]{(StatementMessage) textMessage});
            }
        } else if (textMessage instanceof TextMessage) {
            Log.i(TAG, "Add text message");
            messageView = new TextMessageView(textMessage, messageContainer.getContext());
            AsyncTask asyncTask = new AsyncTask<TextMessage, Void, Void>() {
                @Override
                protected Void doInBackground(TextMessage... textMessages) {
                    AuditTrailDbHelper auditTrailDbHelper = new AuditTrailDbHelper(context);
                    auditTrailDbHelper.insertTextMessage(textMessages[0]);
                    return null;
                }
            };
            if (newMessage) {
                asyncTask.execute(new TextMessage[]{textMessage});
            }
        } else {
            Log.e(TAG, "The new text message is not valid.");
            return;
        }
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 15, 30, 15);

        if (!newMessage) {
            messageViews.addLast(messageView);
            messageContainer.addView(messageView, 0, params);

        } else {
            messageViews.addFirst(messageView);
            messageContainer.addView(messageView, params);
            messageView.startAnimation();
        }
    }

    public long getFirstMessageEpoch() {
        if (messageViews.size() == 0) {
            Log.d(TAG, "There is no message");
            return -1;
        } else {
            Log.d(TAG, "The first time epoch is " +
                        messageViews.getLast().getTextMessage().getTimestamp());
            return messageViews.getLast().getTextMessage().getTimestamp();
        }
    }

    private Deque<TextMessageView> messageViews;

    private LinearLayout messageContainer;

    private Context context;

    private static final String TAG = "MessageViewManager";
}
