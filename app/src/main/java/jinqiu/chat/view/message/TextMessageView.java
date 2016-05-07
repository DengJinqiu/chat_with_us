package jinqiu.chat.view.message;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jinqiu.chat.R;
import jinqiu.chat.controller.message.TextMessage;

public class TextMessageView extends RelativeLayout {
    public TextMessageView(TextMessage textMessage, Context context) {
        super(context);

        this.setId(this.generateViewId());
        this.context = context;


        GradientDrawable gd = new GradientDrawable();

        TextView tv = new TextView(context);
        tv.setText(textMessage.getContext());

        RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setPadding(60, 40, 60, 40);

        if (textMessage.getUserType() == TextMessage.CLIENT) {
            gd.setStroke(0, Color.WHITE);
            gd.setColor(ContextCompat.getColor(context, R.color.colorMessageBackground));

            gd.setCornerRadii(new float[] { 80, 80, 80, 80, 0, 0, 80, 80 });
            tv.setGravity(Gravity.RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            gd.setStroke(5, Color.parseColor("#DFDEE2"));
            gd.setColor(Color.WHITE);
            tv.setGravity(Gravity.LEFT);
            gd.setCornerRadii(new float[] { 80, 80, 80, 80, 80, 80, 0, 0 });

            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }

        tv.setTypeface(Typeface.DEFAULT, 0);
        tv.setTextSize(18);
        tv.setBackground(gd);
        this.addView(tv, params);
    }

    public void startAnimation() {
        super.startAnimation(AnimationUtils.loadAnimation(context, R.anim.text_message));
    }

    public Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    private Context context;
}
