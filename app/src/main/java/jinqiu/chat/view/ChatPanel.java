package jinqiu.chat.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import jinqiu.chat.R;
import jinqiu.chat.controller.ApplicationServer;
import jinqiu.chat.controller.ApplicationServerMessenger;
import jinqiu.chat.controller.message.TextMessage;
import jinqiu.chat.model.BackendServer;
import jinqiu.chat.view.message.MessageViewManager;

public class ChatPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_panel);

        chatPanelMessenger = new ChatPanelMessenger() {
            @Override
            public void deliverMessage(Message message) {
                if (handler == null) {
                    Log.e(TAG, "Handler is not initialized");
                    return;
                }
                handler.sendMessage(message);
            }
        };

        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                if (message == null) {
                    Log.e(TAG, "Got invalid message");
                    return;
                }
                switch (message.what) {
                    case ChatPanelMessenger.FROM_APPLICATION_SERVER:
                        if (messageViewManager == null) {
                            Log.e(TAG, "The messageViewManager is not initialized");
                            return;
                        }
                        messageViewManager.addNewMessageView((TextMessage) message.obj);
                        break;
                    case ChatPanelMessenger.FROM_DB:
                        break;
                    default:
                        Log.e(TAG, "Got invalid message");
                        break;
                }
            }
        };

        backendServer = new BackendServer();
        backendServer.start();
        applicationServer = new ApplicationServer(chatPanelMessenger);
        applicationServer.start();

        applicationServer.registerBackendServer(backendServer);

        messageViewManager = new MessageViewManager((RelativeLayout) findViewById(R.id.message_container));
        final EditText inputField = (EditText) findViewById(R.id.input_field);
        Button button = (Button) findViewById(R.id.send_button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Long tsLong = System.currentTimeMillis() / 1000;
                TextMessage textMessage =
                        new TextMessage(TextMessage.CLIENT, tsLong, inputField.getText().toString());
                Log.d(TAG, "Add new message " + textMessage.toString() + " to chat panel");
                messageViewManager.addNewMessageView(textMessage);
                Log.d(TAG, "Send new message " +  textMessage.toString() + " to application server");
                Message msg = new Message();
                msg.what = ApplicationServerMessenger.FROM_CHAT_PANEL;
                msg.obj = textMessage;

                applicationServer.getApplicationServerMessenger().deliverMessage(msg);
                inputField.setText("");
                inputField.clearFocus();
            }
        });
    }

    private Handler handler;

    private MessageViewManager messageViewManager;

    private ChatPanelMessenger chatPanelMessenger;

    // Simulate the application server
    private ApplicationServer applicationServer;
    // Simulate the backend server
    private BackendServer backendServer;

    private static final String TAG = "ChatPanel";
}