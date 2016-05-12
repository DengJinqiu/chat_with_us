package jinqiu.chat.view.message;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

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
        this.firstTimestamp = -1;
        this.lastestTimestamp = -1;
    }

    public void addMessageView(TextMessage textMessage, boolean newMessage) {
        if (textMessage == null || textMessage.getContext() == null ||
            textMessage.getContext().length() == 0) {
            Log.e(TAG, "The input text mesage is invalid, cannot add it to the chat panl.");
            return;
        }
        final TextMessageView messageView;
        if (newMessage) {
            textMessage.updateTimestamp();
        }

        if (textMessage instanceof StatementMessage) {
            Log.i(TAG, "Add statement message");
            messageView =
                    new StatementMessageView((StatementMessage) textMessage, newMessage, context);
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
            messageView = new TextMessageView(textMessage, newMessage, messageContainer.getContext());
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

        if (newMessage) {
            if (textMessage.getTimestamp() - lastestTimestamp > SHOW_TIME_LABEL_INTERVAL ||
                lastestTimestamp < 0) {
                Log.d(TAG, "Add timestamp label for new message " + textMessage.getTimestamp());
                messageContainer.addView(getTimestampLabel(textMessage.getTimestamp()));
                lastestTimestamp = textMessage.getTimestamp();
                firstTimestamp = lastestTimestamp;
            }
            messageViews.addFirst(messageView);
            messageContainer.addView(messageView);
            //messageView.startAnimation();
        } else {
            messageViews.addLast(messageView);
            messageContainer.addView(messageView, 0);
            if (firstTimestamp - textMessage.getTimestamp() > SHOW_TIME_LABEL_INTERVAL ||
                firstTimestamp < 0) {
                Log.d(TAG, "Add timestamp label for old message " + textMessage.getTimestamp());
                messageContainer.addView(getTimestampLabel(textMessage.getTimestamp()), 0);
                firstTimestamp = textMessage.getTimestamp();
                lastestTimestamp = firstTimestamp;
            }
        }
    }

    private TextView getTimestampLabel(long epoch) {
        TextView textView = new TextView(context);
        textView.setText("epoch is " + epoch);
        textView.setPadding(60, 40, 60, 40);
        return textView;
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

    private long lastestTimestamp;

    private long firstTimestamp;

    private static final double SHOW_TIME_LABEL_INTERVAL = 60000; // 1 min

    private static final String TAG = "MessageViewManager";
}
