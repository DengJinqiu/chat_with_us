package jinqiu.chat.view.message;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
        gd.setColor(ContextCompat.getColor(context, R.color.colorMessageBackground));
        gd.setCornerRadius(100);
        gd.setStroke(2, Color.WHITE);

        setBackground(gd);


        TextView tv = new TextView(context);
        tv.setText(textMessage.getContext());
        this.addView(tv);
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
