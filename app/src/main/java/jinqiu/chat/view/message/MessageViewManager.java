package jinqiu.chat.view.message;

import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import java.util.Deque;
import java.util.LinkedList;

import jinqiu.chat.controller.message.TextMessage;

public class MessageViewManager {

    public MessageViewManager(RelativeLayout messageContainer) {
        this.messageContainer = messageContainer;
        messageViews = new LinkedList<>();
    }

    public void addNewMessageView(TextMessage message) {
        MessageView messageView = new MessageView(messageContainer.getContext());
        TextView tv = new TextView(messageContainer.getContext());
        tv.setText(message.getContext());
        messageView.addView(tv);

        messageView.setId(messageView.generateViewId());

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 100);
        if (messageViews.size() == 0) {
            Log.i(TAG, "Add first new message");
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            Log.i(TAG, "Append new message " + messageViews.getFirst().getId());
            params.addRule(RelativeLayout.BELOW, messageViews.getFirst().getId());
        }

        messageViews.addFirst(messageView);
        messageContainer.addView(messageView, params);
    }

    private Deque<MessageView> messageViews;

    private RelativeLayout messageContainer;

    private static final String TAG = "MessageViewManager";
}
