package jinqiu.chat.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import jinqiu.chat.R;
import jinqiu.chat.controller.application_server.ApplicationServer;
import jinqiu.chat.controller.application_server.ApplicationServerMessenger;
import jinqiu.chat.controller.database.AuditTrailDbHelper;
import jinqiu.chat.controller.message.TextMessage;
import jinqiu.chat.model.backend_server.BackendServer;
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

        messageViewManager = new MessageViewManager((LinearLayout) findViewById(R.id.message_container));
        final EditText inputField = (EditText) findViewById(R.id.input_field);
        Button button = (Button) findViewById(R.id.send_button);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.message_container);
        linearLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);

            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.d(TAG, "New layout: " + top + " " + left + " " + bottom + " " + right);
                Log.d(TAG, "Old layout: " + oldTop + " " + oldLeft + " " + oldBottom + " " + oldRight);

                Log.d(TAG, "Message view height: " + view.getHeight() + ". Scroll view height: " + scrollView.getHeight());
                Log.d(TAG, "Scroll to: " + Math.max(0, view.getHeight() - scrollView.getHeight()));
                scrollView.scrollTo(0, Math.max(0, view.getHeight() - scrollView.getHeight()));
            }
        });

        findViewById(R.id.message_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "Touch screen, clear the focus of input field.");
                    InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    findViewById(R.id.input_field).clearFocus();
                }
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextMessage textMessage =
                        new TextMessage(TextMessage.CLIENT, inputField.getText().toString());
                Log.d(TAG, "Add new message " + textMessage.toString() + " to chat panel");
                messageViewManager.addNewMessageView(textMessage);
                Log.d(TAG, "Send new message " +  textMessage.toString() + " to application server");
                Message msg = new Message();
                msg.what = ApplicationServerMessenger.FROM_CHAT_PANEL;
                msg.obj = textMessage;

                applicationServer.getApplicationServerMessenger().deliverMessage(msg);
                inputField.setText("");
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