package jinqiu.chat.controller.application_server;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;

import jinqiu.chat.controller.message.StatementMessage;
import jinqiu.chat.controller.message.TextMessage;
import jinqiu.chat.model.backend_server.BackendServer;
import jinqiu.chat.model.backend_server.BackendServerMessenger;
import jinqiu.chat.view.ChatPanelMessenger;

// Simulate the server running on the application side
public class ApplicationServer extends HandlerThread {
    public ApplicationServer(ChatPanelMessenger chatPanelMessenger) {
        super("ApplicationServer ");
        applicationServerMessenger = new ApplicationServerMessenger() {
            @Override
            public void deliverMessage(Message message) {
                if (handler == null) {
                    Log.e(TAG, "Server is not running");
                    return;
                }
                Log.d(TAG, "Deliver message to application server");
                handler.sendMessage(message);
            }
        };

        this.chatPanelMessenger = chatPanelMessenger;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();

        handler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message message) {
                if (message == null) {
                    Log.e(TAG, "Got invalid message");
                    return;
                }
                switch (message.what) {
                    case ApplicationServerMessenger.FROM_CHAT_PANEL:
                        receiveNewAddedMessageFromChatPanel((TextMessage) message.obj);
                        break;
                    case ApplicationServerMessenger.FROM_BACKEND_SERVER:
                        if (message.arg1 == RequestAndResponseType.RESPONSE) {
                            sendMessageResponse(message.arg2, (String) message.obj);
                        } else if (message.arg1 == RequestAndResponseType.REQUEST) {
                            receiveMessage((String) message.obj);
                        } else {
                            Log.e(TAG, "Got invalid message");
                        }
                        break;
                    default:
                        Log.e(TAG, "Got invalid message");
                        break;
                }
            }
        };
    }

    private void receiveNewAddedMessageFromChatPanel(TextMessage textMessage) {
        if (textMessage == null || textMessage.getContext() == null ||
            textMessage.getContext().length() == 0) {
            Log.e(TAG, "Got invalid message");
            return;
        }
        sendMessageRequest(textMessage.toString());
    }

    // Send message to backend server
    private void sendMessageRequest(String request) {
        if (backendServerMessenger == null) {
            Log.i(TAG, "No backend server connected");
            return;
        }
        Log.i(TAG, "Send message: " + request + " to backend server");
        Message message = new Message();
        message.what = BackendServerMessenger.FROM_APPLICATION_SERVER;
        message.arg1 = RequestAndResponseType.REQUEST;
        message.obj = request;
        backendServerMessenger.deliverMessage(message);
    }

    // The response returned by sendMessageRequest
    private void sendMessageResponse(int status, String details) {
        Log.i(TAG, "Got response for sending message to backend server.");
        switch (status) {
            case RequestAndResponseType.SUCCESS:
                Log.d(TAG, "Message send to application server successfully.");
                break;
            case RequestAndResponseType.SUCCESS_WITH_ADDITIONAL_FIELDS:
                Log.d(TAG, "Message send to application server successfully, " +
                           "has auto replay " + details);
                try {
                    StatementMessage statementMessage  = new StatementMessage(details);
                    Message messageToChatPanel = new Message();
                    messageToChatPanel.what = ChatPanelMessenger.FROM_APPLICATION_SERVER;
                    messageToChatPanel.obj = statementMessage;
                    chatPanelMessenger.deliverMessage(messageToChatPanel);
                } catch (JSONException e) {
                    Log.e(TAG, "Cannot parse the auto reply " + details);
                    e.printStackTrace();
                }
                break;
            default:
                Log.e(TAG, "Message send to backend server failed.");
                break;
        }
    }

    // Receive message from backend server
    private void receiveMessage(String request) {
        Log.i(TAG, "Receive message: " + request + " from backend server.");
        Message messageToBackendServer = new Message();
        messageToBackendServer.what = BackendServerMessenger.FROM_APPLICATION_SERVER;
        messageToBackendServer.arg1 = RequestAndResponseType.RESPONSE;
        messageToBackendServer.arg2 = RequestAndResponseType.SUCCESS;

        try {
            TextMessage textMessage = new TextMessage(request);
            Message messageToChatPanel = new Message();
            messageToChatPanel.what = ChatPanelMessenger.FROM_APPLICATION_SERVER;
            messageToChatPanel.obj = textMessage;
            chatPanelMessenger.deliverMessage(messageToChatPanel);
        } catch (JSONException e) {
            e.printStackTrace();
            messageToBackendServer.arg2 = RequestAndResponseType.FAILED;
        }
        backendServerMessenger.deliverMessage(messageToBackendServer);
    }

    public void registerBackendServer(BackendServer backendServer) {
        this.backendServerMessenger = backendServer.getBackendServerMessenger();
        backendServer.registerApplicationServer(this);
    }

    public ApplicationServerMessenger getApplicationServerMessenger() {
        return applicationServerMessenger;
    }

    private ApplicationServerMessenger applicationServerMessenger;

    private BackendServerMessenger backendServerMessenger;

    private ChatPanelMessenger chatPanelMessenger;

    private Handler handler;

    private static final String TAG = "ApplicationServer";
}