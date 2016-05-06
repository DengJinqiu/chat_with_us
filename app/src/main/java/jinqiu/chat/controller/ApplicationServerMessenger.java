package jinqiu.chat.controller;

import android.os.Message;

public interface ApplicationServerMessenger {
    int FROM_CHAT_PANEL = 0;
    int FROM_BACKEND_SERVER = 1;

    void deliverMessage(Message message);
}
