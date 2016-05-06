package jinqiu.chat.view;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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

        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
            }
        };

        chatPanelMessenger = new ChatPanelMessenger() {
            @Override
            public void sendMessage(android.os.Message message) {
                handler.sendMessage(message);
            }
        };

        backendServer = new BackendServer();
        backendServer.start();

        applicationServer = new ApplicationServer(applicationServerId);
        applicationServer.registerBackendServer(backendServer);
        applicationServer.start();

        final MessageViewManager messageViewManager = new MessageViewManager((RelativeLayout) findViewById(R.id.message_container));

        final EditText inputField = (EditText) findViewById(R.id.input_field);

        Button button = (Button) findViewById(R.id.send_button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Long tsLong = System.currentTimeMillis() / 1000;
                TextMessage newMessage = new TextMessage(TextMessage.CLIENT, tsLong, inputField.getText().toString());
                Log.i(TAG, "Add new message to chat panel");
                messageViewManager.addNewMessageView(newMessage);
                Log.i(TAG, "send new added message to application server");
                android.os.Message msg = new android.os.Message();
                msg.what = ApplicationServerMessenger.FROM_CHAT_PANEL;
                msg.obj = newMessage;

                applicationServer.getApplicationServerMessenger().deliverMessage(msg);
                inputField.setText("");
                inputField.clearFocus();
            }
        });
    }

    private Handler handler;

    private ChatPanelMessenger chatPanelMessenger;

    // Dummy application server id, there could be multiple application server connect to
    // the backend server, each of them should have a unique id
    private static final Integer applicationServerId = 1;
    // Simulate the application server
    private ApplicationServer applicationServer;
    // Simulate the backend server
    private BackendServer backendServer;

    private static final String TAG = "ChatPanel";
}