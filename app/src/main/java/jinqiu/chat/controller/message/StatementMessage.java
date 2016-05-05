package jinqiu.chat.controller.message;

public class StatementMessage extends Message  {
    public StatementMessage(UserType userType, int timestamp, String context,
                            String accoutNumber, double price, double tax, int dueDate, double due) {
        super(userType, timestamp, context);

        this.accoutNumber = accoutNumber;
        this.price = price;
        this.tax = tax;
        this.dueDate = dueDate;
        this.due = due;
    }

    private String accoutNumber;
    private double price;
    private double tax;
    private int dueDate;
    private double due;
}
