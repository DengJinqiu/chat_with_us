package jinqiu.chat.controller;

public class ServerHandler {
    public static ServerHandler getInstance() {
        if (instance == null) {
            instance = new ServerHandler();
        }

        return instance;
    }

    // value is a json string
    // return additional fields want to display on the auto replay
    // value has fields: type (-1 request failed, 0 no auto replay, 1 auto replay statement)
    //                   detail (for statement message)
    public void newMessageResponse(String value) {

    }

    private static ServerHandler instance = null;

    private ServerHandler() {}
}
