package jinqiu.chat.view;

import android.app.Activity;
import android.os.AsyncTask;
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

import java.util.List;

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
                        messageViewManager.addMessageView((TextMessage) message.obj, true);
                        break;
                    case ChatPanelMessenger.FROM_DB:
                        break;
                    default:
                        Log.e(TAG, "Got invalid message");
                        break;
                }
            }
        };

        isFetching = false;
        addingInitMessages = true;

        backendServer = new BackendServer();
        backendServer.start();
        applicationServer = new ApplicationServer(chatPanelMessenger);
        applicationServer.start();

        applicationServer.registerBackendServer(backendServer);

        messageViewManager = new MessageViewManager((LinearLayout) findViewById(R.id.message_container));

        final EditText inputField = (EditText) findViewById(R.id.input_field);
        Button button = (Button) findViewById(R.id.send_button);

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.message_container);

        // When hide/show keyboard, always scroll to bottom
        scrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if ((oldTop - oldBottom) != (top - bottom)) {
                    Log.d(TAG, "Scroll to bottom.");
                    scrollView.smoothScrollTo(0, Math.max(0, linearLayout.getHeight() - view.getHeight()));
                }
            }
        });

        // When fetching previous message, do not change the context on the current view
        // When opening the screen, scroll to bottom
        linearLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.d(TAG, "New layout top: " + top + ", bottom: " + bottom);
                Log.d(TAG, "Old layout top: " + oldTop + ", bottom " + oldBottom);
                if (addingInitMessages) {
                    addingInitMessages = false;
                    Log.d(TAG, "Scroll to bottom.");
                    scrollView.smoothScrollTo(0, Math.max(0, linearLayout.getHeight() - view.getHeight()));
                } else {
                    Log.d(TAG, "Scroll y by " + (bottom - oldBottom));
                    scrollView.scrollBy(0, bottom - oldBottom);
                }
            }
        });

        // When touching on the other part of the screen, hide keyboard and clear focus on edit field
        // When reaching the top of the view and scrolling down, fetch previous messges.
        findViewById(R.id.message_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "Touch screen, clear the focus of input field.");
                    InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    findViewById(R.id.input_field).clearFocus();
                    preY = motionEvent.getY();
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    if (motionEvent.getY() > preY) {
                        if (scrollView.getTop() == 0) {
                            Log.d(TAG, "Scroll to the top now, fetching previous message");
                            fetchPreviousMessages(NUMBER_OF_PRE_MESSAGE_TO_FETCH);
                        }
                    }
                    preY = motionEvent.getY();
                    return true;
                }
                return false;
            }
        });

        // Create new message
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputField.getText().toString().length() == 0) {
                    return;
                }
                // The timestamp will be update when adding this message to chat panel
                TextMessage textMessage =
                        new TextMessage(TextMessage.CLIENT, 0, inputField.getText().toString());
                Log.d(TAG, "Add new message " + textMessage.toString() + " to chat panel");
                messageViewManager.addMessageView(textMessage, true);
                Log.d(TAG, "Send new message " +  textMessage.toString() + " to application server");
                Message msg = new Message();
                msg.what = ApplicationServerMessenger.FROM_CHAT_PANEL;
                msg.obj = textMessage;

                applicationServer.getApplicationServerMessenger().deliverMessage(msg);
                inputField.setText("");
            }
        });

        fetchPreviousMessages(NUMBER_OF_INIT_MESSAGE);
    }

    private void fetchPreviousMessages(final int number) {
        if (isFetching) {
            Log.d(TAG, "There is anther db fetching running.");
            return;
        }
        long firstMessageEpoch = messageViewManager.getFirstMessageEpoch();
        Log.d(TAG, "The epoch time for the first view is " + firstMessageEpoch + ".");
        Log.d(TAG, "Set fetching db to true");
        isFetching = true;
        AsyncTask asyncTask = new AsyncTask<Long, Void, List<TextMessage>>() {
            @Override
            protected List<TextMessage> doInBackground(Long... firstMessageEpoch) {
                AuditTrailDbHelper auditTrailDbHelper = new AuditTrailDbHelper(getBaseContext());
                return auditTrailDbHelper.fetchPreviousMessages(firstMessageEpoch[0], number);
            }

            @Override
            protected void onPostExecute(List<TextMessage> textMessages) {
                Log.d(TAG, "Got " + textMessages.size() + " previous messages.");
                for (TextMessage textMessage : textMessages) {
                    Log.d(TAG, "Got previous message: " + textMessage.toString());
                    messageViewManager.addMessageView(textMessage, false);
                }

                final Handler h = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Set fetching db to false");
                        isFetching = false;
                    }
                };

                h.postDelayed(runnable, DELAY_TIME_AFTER_FINISH_FETCH);
            }
        };

        asyncTask.execute(new Long[]{firstMessageEpoch});
    }

    private Handler handler;

    private boolean isFetching;

    private boolean addingInitMessages;

    private final static int NUMBER_OF_INIT_MESSAGE = 10;

    private final static int NUMBER_OF_PRE_MESSAGE_TO_FETCH = 5;

    private final static int DELAY_TIME_AFTER_FINISH_FETCH = 500;

    private MessageViewManager messageViewManager;

    private ChatPanelMessenger chatPanelMessenger;

    // Simulate the application server
    private ApplicationServer applicationServer;
    // Simulate the backend server
    private BackendServer backendServer;

    private static final String TAG = "ChatPanel";

    private float preY;
}