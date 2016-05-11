package jinqiu.chat.model.backend_server;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import org.json.JSONException;

import jinqiu.chat.controller.application_server.ApplicationServer;
import jinqiu.chat.controller.application_server.ApplicationServerMessenger;
import jinqiu.chat.controller.application_server.RequestAndResponseType;
import jinqiu.chat.controller.message.StatementMessage;
import jinqiu.chat.controller.message.TextMessage;

// Use a background thread to simulate the backend server
public class BackendServer extends HandlerThread {

    public BackendServer() {
        super("BackendServer");
        backendServerMessenger = new BackendServerMessenger() {
            @Override
            public void deliverMessage(Message message) {
                if (handler == null) {
                    Log.e(TAG, "Server is not running");
                }
                Log.d(TAG, "Deliver message to backend server");
                handler.sendMessage(message);
            }
        };
    }

    public void registerApplicationServer(ApplicationServer applicationServer) {
        applicationServerMessenger = applicationServer.getApplicationServerMessenger();
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();

        handler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message message) {
                if (message == null || message.what != BackendServerMessenger.FROM_APPLICATION_SERVER) {
                    Log.e(TAG, "Got invalid message");
                    return;
                }
                if (message.arg1 == RequestAndResponseType.RESPONSE) {
                    sendMessageResponse(message.arg2);
                } else if (message.arg1 == RequestAndResponseType.REQUEST){
                    receiveMessage((String) message.obj);
                } else {
                    Log.e(TAG, "Got invalid message");
                }
            }
        };
    }

    // Send message to application server
    private void sendMessageRequest(String request) {
        if (applicationServerMessenger == null) {
            Log.i(TAG, "No application server connected");
            return;
        }
        Log.i(TAG, "Send message: " + request + " to application server");
        Message message = new Message();
        message.what = ApplicationServerMessenger.FROM_BACKEND_SERVER;
        message.arg1 = RequestAndResponseType.REQUEST;
        message.obj = request;
        applicationServerMessenger.deliverMessage(message);
    }

    // The response returned by sendMessageRequest
    private void sendMessageResponse(int status) {
        Log.i(TAG, "Got response for sending message to application server.");
        if (status == RequestAndResponseType.SUCCESS) {
            Log.d(TAG, "Message send to application server successfully");
        } else {
            Log.e(TAG, "Message send to application server failed");
        }
    }

    // Receive message from application server
    private void receiveMessage(String request) {
        Log.i(TAG, "Receive message: " + request + " from application server.");
        Message message = new Message();
        message.what = ApplicationServerMessenger.FROM_BACKEND_SERVER;
        message.arg1 = RequestAndResponseType.RESPONSE;

        boolean simulateReply = false;
        TextMessage replyTextMessage = null;

        if (request == null || request.length() == 0) {
            Log.e(TAG, "Message is invalid.");
            message.arg2 = RequestAndResponseType.FAILED;
        } else {
            try {
                TextMessage textMessage = new TextMessage(request);
                if (textMessage.getContext().contains("bill")) {
                    message.arg2 = RequestAndResponseType.SUCCESS_WITH_ADDITIONAL_FIELDS;

                    Log.d(TAG, "Simulate reply from company after 1 second");
                    SystemClock.sleep(1000);

                    // The timestamp will be update when adding this message to chat panel
                    StatementMessage statementMessage =
                            new StatementMessage(TextMessage.COMPANY, 0,
                                                 "Here is your current statement:",
                                                 "728323981238921", 60.85, 8.4, 20160226, 135.2);

                    Log.i(TAG, "Auto reply a statement to application server: " + statementMessage.toString());
                    message.obj = statementMessage.toString();
                } else {
                    Log.d(TAG, "No auto reply.");
                    message.arg2 = RequestAndResponseType.SUCCESS;

                    Log.i(TAG, "Simulate reply from company.");
                    // The timestamp will be update when adding this message to chat panel
                    replyTextMessage = new TextMessage(TextMessage.COMPANY, 0, textMessage.getContext());
                    simulateReply = true;
                }
            } catch (JSONException e) {
                message.arg2 = RequestAndResponseType.FAILED;
                Log.e(TAG, "Request failed, convert json: " + e.getMessage());
            }
        }
        applicationServerMessenger.deliverMessage(message);

        // Simulate the scenairo that the company replies this new message after 2 second
        if (simulateReply && replyTextMessage != null) {
            Log.d(TAG, "Simulate reply from company after 1 second");
            SystemClock.sleep(1000);
            Log.d(TAG, "Reply from company: " + replyTextMessage.toString());
            sendMessageRequest(replyTextMessage.toString());
        }
    }

    public BackendServerMessenger getBackendServerMessenger() {
        return backendServerMessenger;
    }

    private ApplicationServerMessenger applicationServerMessenger;

    private Handler handler;

    private BackendServerMessenger backendServerMessenger;

    private static final String TAG = "BackendServer";
}
