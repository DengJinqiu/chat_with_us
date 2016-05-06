package jinqiu.chat.view;

import android.os.Message;

public interface ChatPanelMessenger {
    int FROM_APPLICATION_SERVER = 0;
    int FROM_DB = 1;

    void deliverMessage(Message message);
}
