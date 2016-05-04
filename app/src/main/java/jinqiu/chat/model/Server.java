package jinqiu.chat.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import jinqiu.chat.controller.ServerHandler;

// Mimic the background server
public class Server {
    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }

        return instance;
    }

    // value is a json string
    // value has fields: timestamp (epoch)
    //                   user (0 customer, 1 company)
    //                   context
    public void newMessageRequest(String value) {
        Log.i("newMessageRequest", "Get message " + value);
        try {
            JSONObject jsonValue = new JSONObject(value);
            Long timeStamp = jsonValue.getLong("timestamp");
            int user = jsonValue.getInt("user");
            String context = jsonValue.getString("context");

            if (context.contains("bill")) {
                Log.i("newMessageRequest", "Request success, need to auto reply a statement");
                ServerHandler.getInstance().newMessageResponse("{type: 1}");
            } else {
                Log.i("newMessageRequest", "Request success, no auto replay");
                ServerHandler.getInstance().newMessageResponse("{type: 0}");

                if (user == 0) {
                    Log.i("newMessageRequest", "Simulate a response by echoing back the message");
                    this.newMessageRequest(value);
                }
            }
        } catch (JSONException e) {
            Log.e("newMessageRequest", "Request failed, convert json: " + e.getMessage());
            ServerHandler.getInstance().newMessageResponse("{type: -1}");
        }
    }

    private static Server instance = null;

    private Server() {}
}
