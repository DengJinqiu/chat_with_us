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
            public void sendMessage(Message message) {
                Log.i(TAG, "Sending message: " + message + " to application server");
                Message msg = new Message();
                handler.sendMessage(msg);
            }
        };
    }

    // Send new message added by user to backend server
    public void SendNewMessageRequest(String request) {

    }

    // The response returned by SendNewMessage
    public void SendNewMessageResponse(String response) {

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