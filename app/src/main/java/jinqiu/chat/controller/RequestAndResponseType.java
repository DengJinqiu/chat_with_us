package jinqiu.chat.controller;

// There are two pairs of request and response between backend server and application server
// 1. Send new added message from application server to backend server
// 2. Send message from backend server to application server
//
// message.arg1 defines whether is it request or response
// message.arg1 is 0: request
// message.arg1 is 1: response
//
// message.arg2 defines the status of response
//      message.arg2 is 0: failed
//      message.arg2 is 1: success, no additional fields in response
//      message.arg2 is 2: success, has additional fields in response (for auto replay)
public class RequestAndResponseType {
    public final static int REQUEST = 0;
    public final static int RESPONSE = 1;

    public final static int FAILED = 0;
    public final static int SUCCESS = 1;
    public final static int SUCCESS_WITH_ADDITIONAL_FIELDS = 2;
}
