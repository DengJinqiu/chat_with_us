package jinqiu.chat.controller.message;


public class Message {

    public Message(UserType userType, int timestamp, String context) {
        this.userType = userType;
        this.timestamp = timestamp;
        this.context = context;
    }

    public enum UserType {COMPANY, USER};

    public UserType getUserType() {
        return userType;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getContext() {
        return context;
    }

    private UserType userType;

    private int timestamp;

    private String context;
}
