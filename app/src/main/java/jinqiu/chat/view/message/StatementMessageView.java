package jinqiu.chat.view.message;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jinqiu.chat.R;
import jinqiu.chat.controller.message.StatementMessage;

public class StatementMessageView extends TextMessageView {

    public StatementMessageView(StatementMessage statementMessage, boolean newMessage, Context context) {
        super(statementMessage, newMessage, context);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.i(TAG, "Initialize the statement table.");

        this.statementMessage = (TableLayout) layoutInflater.inflate(R.layout.statement_message_view, this, false);
        this.rowAccount = (TableRow) this.statementMessage.findViewById(R.id.row_account);
        this.rowLine1 = this.statementMessage.findViewById(R.id.row_line_1);
        this.rowPrice = (TableRow) this.statementMessage.findViewById(R.id.row_price);
        this.rowTax = (TableRow) this.statementMessage.findViewById(R.id.row_tax);
        this.rowLine2 = this.statementMessage.findViewById(R.id.row_line_2);
        this.rowDueTitle = (TableRow) this.statementMessage.findViewById(R.id.row_due_title);
        this.rowDueValue = (TableRow) this.statementMessage.findViewById(R.id.row_due_value);

        ((TextView) this.statementMessage.findViewById(R.id.account_number_value)).setText(statementMessage.getAccountNumber());
        ((TextView) this.statementMessage.findViewById(R.id.product_price)).setText(String.format("%.2f", statementMessage.getPrice()));
        ((TextView) this.statementMessage.findViewById(R.id.tax_value)).setText(String.format("%.2f", statementMessage.getTax()));
        ((TextView) this.statementMessage.findViewById(R.id.due_date_value)).setText(""+statementMessage.getDueDate());
        ((TextView) this.statementMessage.findViewById(R.id.total_due_value)).setText("$" + String.format("%.2f", statementMessage.getDue()));

        LayoutParams params = (LayoutParams) this.statementMessage.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, textView.getId());

        this.addView(this.statementMessage, params);

        if (newMessage) {
            this.statementMessage.setVisibility(INVISIBLE);
            this.rowAccount.setVisibility(INVISIBLE);
            this.rowLine1.setVisibility(INVISIBLE);
            this.rowPrice.setVisibility(INVISIBLE);
            this.rowTax.setVisibility(INVISIBLE);
            this.rowLine2.setVisibility(INVISIBLE);
            this.rowDueTitle.setVisibility(INVISIBLE);
            this.rowDueValue.setVisibility(INVISIBLE);
        }
    }

    @Override
    protected void initAnimation() {
        super.initAnimation();

        ObjectAnimator statementTableAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(),
                R.animator.statement_table_animation);
        statementMessage.setPivotX(0);
        statementMessage.setPivotY(0);
        AddAnimatorToView(statementMessage, statementTableAnimator);
        animators.add(statementTableAnimator);

        AnimatorSet upperPartSet = new AnimatorSet();
        List<Animator> upperPartAnimators = new ArrayList<> ();

        ObjectAnimator rowAccountAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(),
                R.animator.statement_row_animation);
        rowAccount.setPivotX(0);
        rowAccount.setPivotY(rowAccount.getHeight());
        AddAnimatorToView(rowAccount, rowAccountAnimator);
        upperPartAnimators.add(rowAccountAnimator);

        ObjectAnimator rowLine1Animator = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(),
                R.animator.statement_row_animation);
        rowLine1.setPivotX(0);
        rowLine1.setPivotY(rowLine1.getHeight());
        AddAnimatorToView(rowLine1, rowLine1Animator);
        upperPartAnimators.add(rowLine1Animator);

        ObjectAnimator rowPriceAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(),
                R.animator.statement_row_animation);
        rowPrice.setPivotX(0);
        rowPrice.setPivotY(rowPrice.getHeight());
        AddAnimatorToView(rowPrice, rowPriceAnimator);
        upperPartAnimators.add(rowPriceAnimator);

        ObjectAnimator rowTaxAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(),
                R.animator.statement_row_animation);
        rowTax.setPivotX(0);
        rowTax.setPivotY(rowTax.getHeight());
        AddAnimatorToView(rowTax, rowTaxAnimator);
        upperPartAnimators.add(rowTaxAnimator);

        ObjectAnimator rowLine2Animator = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(),
                R.animator.statement_row_animation);
        rowLine2.setPivotX(0);
        rowLine2.setPivotY(rowLine2.getHeight());
        AddAnimatorToView(rowLine2, rowLine2Animator);
        upperPartAnimators.add(rowLine2Animator);

        upperPartSet.playTogether(upperPartAnimators);
        animators.add(upperPartSet);

        AnimatorSet lowerPartSet = new AnimatorSet();
        List<Animator> lowerPartAnimators = new ArrayList<> ();

        ObjectAnimator rowDueTitleAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(),
                R.animator.statement_row_animation);
        rowDueTitle.setPivotX(0);
        rowDueTitle.setPivotY(rowAccount.getHeight());
        AddAnimatorToView(rowDueTitle, rowDueTitleAnimator);
        lowerPartAnimators.add(rowDueTitleAnimator);

        ObjectAnimator rowDueValueAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(),
                R.animator.statement_row_animation);
        rowDueValue.setPivotX(0);
        rowDueValue.setPivotY(rowAccount.getHeight());
        AddAnimatorToView(rowDueValue, rowDueValueAnimator);
        lowerPartAnimators.add(rowDueValueAnimator);

        lowerPartSet.playTogether(lowerPartAnimators);

        animators.add(lowerPartSet);

    }

    private TableLayout statementMessage;
    private TableRow rowAccount;
    private View rowLine1;
    private TableRow rowPrice;
    private TableRow rowTax;
    private View rowLine2;
    private TableRow rowDueTitle;
    private TableRow rowDueValue;

    private static final String TAG = "StatementMessageView";
}
