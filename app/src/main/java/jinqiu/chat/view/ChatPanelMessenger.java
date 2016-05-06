package jinqiu.chat.view;

import android.os.Message;

public interface ChatPanelMessenger {
    int FROM_APPLICATION_SERVER = 0;

    void sendMessage(Message message);
}
