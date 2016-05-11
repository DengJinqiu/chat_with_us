package jinqiu.chat.view.message;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jinqiu.chat.R;
import jinqiu.chat.controller.message.TextMessage;

public class TextMessageView extends RelativeLayout {
    public TextMessageView(TextMessage textMessage, Context context) {
        super(context);

        this.textMessage = textMessage;

        GradientDrawable gradientDrawable = new GradientDrawable();

        inputField = new TextView(context);
        inputField.setId(inputField.generateViewId());
        inputField.setText(textMessage.getContext());

        inputField.setPadding(60, 40, 60, 40);

        RelativeLayout.LayoutParams inputFieldParams =
                new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        if (textMessage.getUserType() == TextMessage.CLIENT) {
            gradientDrawable.setStroke(0, Color.WHITE);
            gradientDrawable.setColor(ContextCompat.getColor(context, R.color.colorMessageBackground));

            inputFieldParams.setMargins(50, 0, 0, 0);

            gradientDrawable.setCornerRadii(new float[] { 80, 80, 80, 80, 0, 0, 80, 80 });
            inputField.setGravity(Gravity.RIGHT);
            inputFieldParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            gradientDrawable.setStroke(5, Color.parseColor("#DFDEE2"));
            gradientDrawable.setColor(Color.WHITE);

            inputFieldParams.setMargins(0, 0, 50, 0);

            gradientDrawable.setCornerRadii(new float[] { 80, 80, 80, 80, 80, 80, 0, 0 });
            inputField.setGravity(Gravity.LEFT);
            inputFieldParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }

        inputField.setTypeface(Typeface.DEFAULT, 0);
        inputField.setTextSize(18);
        inputField.setBackground(gradientDrawable);

        this.addView(inputField, inputFieldParams);
    }

    public void startAnimation() {
        int tmp = textMessage.getUserType() == TextMessage.CLIENT ? 1 : 0;
        Animation animation = new ScaleAnimation(
                0, 1f, // Start and end values for the X axis scaling
                0, 1f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, tmp, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        animation.setFillAfter(true); // Needed to keep the result of the animation
        animation.setDuration(500);
        super.startAnimation(animation);
    }

    public TextMessage getTextMessage() {
        return this.textMessage;
    }

    private TextMessage textMessage;

    protected TextView inputField;
}
