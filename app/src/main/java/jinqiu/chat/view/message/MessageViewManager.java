package jinqiu.chat.view.message;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;

import java.util.Deque;
import java.util.LinkedList;

import jinqiu.chat.controller.message.StatementMessage;
import jinqiu.chat.controller.message.TextMessage;

public class MessageViewManager {

    public MessageViewManager(LinearLayout messageContainer) {
        this.messageContainer = messageContainer;
        messageViews = new LinkedList<>();
    }

    public void addNewMessageView(TextMessage textMessage) {
        TextMessageView messageView;
        if (textMessage instanceof StatementMessage) {
            Log.i(TAG, "Add statement message");
            messageView =
                    new StatementMessageView((StatementMessage) textMessage, messageContainer.getContext());
        } else if (textMessage instanceof TextMessage) {
            Log.i(TAG, "Add text message");
            messageView =
                    new TextMessageView(textMessage, messageContainer.getContext());
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

    private static final String TAG = "MessageViewManager";
}
