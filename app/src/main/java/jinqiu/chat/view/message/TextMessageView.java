package jinqiu.chat.view.message;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jinqiu.chat.R;
import jinqiu.chat.controller.message.TextMessage;

public class TextMessageView extends LinearLayout {
    public TextMessageView(TextMessage textMessage, Context context) {
        super(context);

        this.textMessage = textMessage;

        this.setOrientation(LinearLayout.VERTICAL);

        GradientDrawable gradientDrawable = new GradientDrawable();

        TextView textView = new TextView(context);
        textView.setText(textMessage.getContext());

        textView.setPadding(60, 40, 60, 40);

        RelativeLayout.LayoutParams inputFieldParams =
                new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        if (textMessage.getUserType() == TextMessage.CLIENT) {
            gradientDrawable.setStroke(0, Color.WHITE);
            gradientDrawable.setColor(ContextCompat.getColor(context, R.color.colorMessageBackground));

            inputFieldParams.setMargins(50, 0, 0, 0);

            gradientDrawable.setCornerRadii(new float[] { 80, 80, 80, 80, 0, 0, 80, 80 });
            textView.setGravity(Gravity.RIGHT);
            inputFieldParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            gradientDrawable.setStroke(5, Color.parseColor("#DFDEE2"));
            gradientDrawable.setColor(Color.WHITE);

            inputFieldParams.setMargins(0, 0, 50, 0);

            gradientDrawable.setCornerRadii(new float[] { 80, 80, 80, 80, 80, 80, 0, 0 });
            textView.setGravity(Gravity.LEFT);
            inputFieldParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }

        textView.setTypeface(Typeface.DEFAULT, 0);
        textView.setTextSize(18);
        textView.setBackground(gradientDrawable);

        RelativeLayout relativeLayout = new RelativeLayout(context);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        relativeLayout.addView(textView, inputFieldParams);

        this.addView(relativeLayout, layoutParams);
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

    private TextMessage textMessage;
}
