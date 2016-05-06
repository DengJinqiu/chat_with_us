package jinqiu.chat.controller.message;

public class StatementMessage extends TextMessage {
    public StatementMessage(int userType, Long timestamp, String context,
                            String accountNumber, double price, double tax, int dueDate, double due) {
        super(userType, timestamp, context);

        this.accountNumber = accountNumber;
        this.price = price;
        this.tax = tax;
        this.dueDate = dueDate;
        this.due = due;
    }

    private String accountNumber;
    private double price;
    private double tax;
    private int dueDate;
    private double due;
}
