package jinqiu.chat.view.message;

import android.content.Context;
import android.widget.RelativeLayout;

import jinqiu.chat.controller.message.TextMessage;


public class MessageView extends RelativeLayout {
    public MessageView(Context context) {
        super(context);
    }

    private TextMessage message;
}
