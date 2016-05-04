package jinqiu.chat.controller;

import android.os.Handler;
import android.os.Message;

public interface ApplicationServerMessenger {
    void sendMessage(Message message);
}
