package jinqiu.chat.view.message;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Deque;

import jinqiu.chat.controller.ApplicationServer;

public class MessageManager {

//    // value is a json string
//    // return additional fields want to display on the auto replay
//    // value has fields: type (-1 request failed, 0 no auto replay, 1 auto replay statement)
//    //                   detail (for statement message)
//    public void newMessageResponse(String value) {
//        try {
//            JSONObject response = new JSONObject(value);
//            switch (response.getInt("type")) {
//                case 0:
//                    break;
//                case 1:
//                    break;
//                case -1:
//                    break;
//                default:
//                    break;
//            }
//        } catch (JSONException e) {
//            Log.e("newMessageResponse", "The response is not valid: " + e.getMessage());
//        }
//    }

    private Deque<Message> messages;
}
