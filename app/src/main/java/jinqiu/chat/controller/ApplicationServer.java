package jinqiu.chat.controller;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import jinqiu.chat.model.BackendServer;
import jinqiu.chat.model.BackendServerMessenger;

// Simulate the server running on the application side
public class ApplicationServer extends HandlerThread {
    public ApplicationServer(int serverId) {
        super("ApplicationServer " + serverId);

        this.serverId = serverId;

        applicationServerMessenger = new ApplicationServerMessenger() {
            @Override
            public void deliverMessage(Message message) {
                Log.i(TAG, "Sending message: " + message + " to application server");
                Message msg = new Message();
                handler.sendMessage(msg);
            }
        };
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();

        handler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ApplicationServerMessenger.FROM_CHAT_PANEL:
                        if (msg.obj != null && msg.obj.toString() != "") {
                            sendNewMessageRequest(msg.obj.toString());
                        } else {
                            Log.e(TAG, "Message is invalid, do not send to backend server");
                        }
                        break;
                    case ApplicationServerMessenger.FROM_BACKEND_SERVER:
                        break;
                    default:
                        break;
                }
            }
        };
    }

    // Send new message added by user to backend server
    public void sendNewMessageRequest(String request) {
        Message message = new Message();
        message.what = BackendServerMessenger.FROM_APPLICATION_SERVER;
        message.obj = request;
        backendServerMessenger.deliverMessage(message);
    }

    // The response returned by SendNewMessage
    public void sendNewMessageResponse(String response) {

    }

    // Receive message from backend server
    public void receiveMessage(String message) {

    }

    public int getServerId() {
        return serverId;
    }

    public void registerBackendServer(BackendServer backendServer) {
        this.backendServerMessenger = backendServer.getBackendServerMessenger();
        backendServer.registerApplicationServer(this);
    }

    public ApplicationServerMessenger getApplicationServerMessenger() {
        return applicationServerMessenger;
    }

    private ApplicationServerMessenger applicationServerMessenger;

    private int serverId;

    private BackendServerMessenger backendServerMessenger;

    private Handler handler;

    private static final String TAG = "BackendServer";
}