package jinqiu.chat.model;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import jinqiu.chat.controller.ApplicationServer;
import jinqiu.chat.controller.ApplicationServerMessenger;

// Use a background thread to simulate the backend server
public class BackendServer extends HandlerThread {

    public BackendServer() {
        super("BackendServer");

        applicationServerMessengers = new HashMap<Integer, ApplicationServerMessenger>();

        backendServerMessenger = new BackendServerMessenger() {
            @Override
            public void sendMessage(String message) {
                Log.i(TAG, "Sending message: " + message + " to backend server");
                Message msg = new Message();
                handler.sendMessage(msg);
            }
        };
    }

    public void registerApplicationServer(ApplicationServer applicationServer) {
        if (!applicationServerMessengers.containsKey(applicationServer.getServerId())) {
            Log.i(TAG, "Register application server " + applicationServer.getServerId());
            applicationServerMessengers.put(applicationServer.getServerId(),
                                            applicationServer.getApplicationServerMessenger());
        } else {
            Log.w(TAG, "Application server " + applicationServer.getServerId() + " is registered");
        }
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();

        handler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                receiveNewMessage("ddd");
            }
        };
    }

    // Send message to application server
    public void sendMessageRequest(String request) {
//      /*  Log.i("newMessageRequest", "Get message " + value);
//        if (listener == null) {
//            Log.e("newMessageRequest", "Did not register listener");
//            return;
//        }
//        try {
//            JSONObject jsonValue = new JSONObject(value);
//            Long timeStamp = jsonValue.getLong("timestamp");
//            int user = jsonValue.getInt("user");
//            String context = jsonValue.getString("context");
//
//            if (context.contains("bill")) {
//                Log.i("newMessageRequest", "Request success, need to auto reply a statement");
//                JSONObject response = new JSONObject();
//                JSONObject detail = new JSONObject();
//                detail.put("account_number", "728323981238921");
//                detail.put("price", 60.85);
//                detail.put("tax", 8.4);
//                detail.put("due_date", 20160226);
//                detail.put("total_due", 135.2);
//                response.put("type", 1);
//                response.put("detail", detail);
//            } else {
//                Log.i("newMessageRequest", "Request success, no auto replay");
//
//                if (user == 0) {
//                    Log.i("newMessageRequest", "Simulate another request from company");
//                    JSONObject request = new JSONObject();
//                    request.put("timestamp", timeStamp);
//                    request.put("user", 1);
//                    request.put("context", context);
//                    this.newMessageRequest(request.toString());
//                }
//            }
//        } catch (JSONException e) {
//            Log.e("newMessageRequest", "Request failed, convert json: " + e.getMessage());
//        }*/
    }

    // The response returned by sendMessageRequest
    public void sendMessageResponse(String response) {

    }

    // Receive new added message
    public void receiveNewMessage(String message) {

    }

    public BackendServerMessenger getBackendServerMessenger() {
        return backendServerMessenger;
    }

    private Map<Integer, ApplicationServerMessenger> applicationServerMessengers;

    private Handler handler;

    private BackendServerMessenger backendServerMessenger;

    private static final String TAG = "BackendServer";
}
