package jinqiu.chat.model;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import jinqiu.chat.controller.ApplicationServer;
import jinqiu.chat.controller.ApplicationServerMessenger;
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
            public void handleMessage(Message msg) {
                if (msg != null && msg.obj != null) {
                    receiveNewMessage((String) msg.obj);
                } else {
                    Log.e(TAG, "Got invalid message");
                }
            }
        };
    }

    // Send message to application server
    public void sendMessageRequest(String request) {
        if (applicationServerMessenger == null) {
            Log.i(TAG, "No application server connected");
            return;
        }
        Log.i(TAG, "Send message: " + request + " to all application server");
        Message msg = new Message();
        msg.what = ApplicationServerMessenger.FROM_BACKEND_SERVER;
        msg.obj = request;
        applicationServerMessenger.deliverMessage(msg);
    }

    // The response returned by sendMessageRequest
    public void sendMessageResponse(String response) {

    }

    // Receive new added message
    // The response are:
    // message.arg1 is 0: request failed,
    // message.arg1 is 1: request success,
    //    message.arg2 is 0: no additional fields in response
    //    message.arg2 is 1: has additional fields in response (for return statement)
    public void receiveNewMessage(String request) {
        Message message = new Message();
        message.what = ApplicationServerMessenger.FROM_BACKEND_SERVER;

        boolean simulateResponse = false;
        TextMessage responseTextMessage = null;

        if (request == null || request.length() == 0) {
            Log.e(TAG, "The new message is empty");
            message.arg1 = 0;
        } else {
            Log.i(TAG, "Receive new message " + request);
            message.arg1 = 1;
            try {
                TextMessage textMessage = new TextMessage(request);
                if (textMessage.getContext().contains("bill")) {
                    message.arg2 = 1;
                    Log.i(TAG, "Need to auto reply a statement");
                    JSONObject response = new JSONObject();
                    JSONObject detail = new JSONObject();
                    detail.put("account_number", "728323981238921");
                    detail.put("price", 60.85);
                    detail.put("tax", 8.4);
                    detail.put("due_date", 20160226);
                    detail.put("total_due", 135.2);
                    response.put("type", 1);
                    response.put("detail", detail);
                } else {
                    message.arg2 = 2;
                    if (textMessage.getUserType() == TextMessage.CLIENT) {
                        Log.i(TAG, "Simulate a response from the company");
                        simulateResponse = true;
                        responseTextMessage = new TextMessage(TextMessage.COMPANY,
                                textMessage.getTimestamp(), textMessage.getContext());
                    }
                }
            } catch (JSONException e) {
                message.arg1 = 0;
                Log.e(TAG, "Request failed, convert json: " + e.getMessage());
            }
        }
        applicationServerMessenger.deliverMessage(message);

        if (simulateResponse && responseTextMessage != null) {
            // Simulate the scenairo that the company replays this new message after 1 second
            SystemClock.sleep(1000);
            sendMessageResponse(responseTextMessage.toString());
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
