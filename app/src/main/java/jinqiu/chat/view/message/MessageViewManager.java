package jinqiu.chat.view.message;

import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;

import java.util.Deque;
import java.util.LinkedList;

import jinqiu.chat.controller.message.TextMessage;

public class MessageViewManager {

    public MessageViewManager(RelativeLayout messageContainer, ScrollView scrollView) {
        this.messageContainer = messageContainer;
        this.scrollView = scrollView;
        messageViews = new LinkedList<>();
    }

    public void addNewMessageView(TextMessage textMessage) {
        TextMessageView messageView =
                new TextMessageView(textMessage, messageContainer.getContext());
//        PercentRelativeLayout.LayoutParams params =
//                new PercentRelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        params.getPercentLayoutInfo().widthPercent = 0.9f;
//        params.setMargins(0, 15, 0, 15);
        if (textMessage.getUserType() == TextMessage.CLIENT) {
//            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else if (textMessage.getUserType() == TextMessage.COMPANY) {
//            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else {
            Log.e(TAG, "User type is unknown");
        }

        if (messageViews.size() == 0) {
            Log.d(TAG, "Add first new message to view");
            //params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            Log.d(TAG, "Append new message to view below " + messageViews.getFirst().getId());
            //params.addRule(RelativeLayout.BELOW, messageViews.getFirst().getId());
        }

        messageViews.addFirst(messageView);
        Log.d(TAG, "Message container pre height: " + messageContainer.getHeight());
        messageContainer.addView(messageView, 0);
        Log.d(TAG, "Message container height: " + messageContainer.getHeight());
        messageView.startAnimation();
    }

    private Deque<TextMessageView> messageViews;

    private RelativeLayout messageContainer;

    private ScrollView scrollView;

    private static final String TAG = "MessageViewManager";
}
