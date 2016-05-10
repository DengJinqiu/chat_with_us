package jinqiu.chat.model.backend_server;

import android.os.Message;

public interface BackendServerMessenger {
    int FROM_APPLICATION_SERVER = 0;

    void deliverMessage(Message message);
}
