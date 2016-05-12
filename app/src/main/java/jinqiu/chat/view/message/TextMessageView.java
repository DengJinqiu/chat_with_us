package jinqiu.chat.view.message;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jinqiu.chat.R;
import jinqiu.chat.controller.message.TextMessage;

public class TextMessageView extends RelativeLayout {
    public TextMessageView(TextMessage textMessage, boolean newMessage, Context context) {
        super(context);

        this.textMessage = textMessage;

        this.animators = new ArrayList<> ();

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (textMessage.getUserType() == TextMessage.CLIENT) {
            textView = (TextView) layoutInflater.inflate(R.layout.text_message_view_right, this, false);
        } else if (textMessage.getUserType() == TextMessage.COMPANY) {
            textView = (TextView) layoutInflater.inflate(R.layout.text_message_view_left, this, false);
        } else {
            Log.e(TAG, "Text message type is not valid.");
            return;
        }

        textView.setText(textMessage.getContext());
        this.addView(textView);
        if (newMessage) {
            textView.setVisibility(VISIBLE);
        }

        if (newMessage) {
            // Do the animation before draw on screen
            getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        startAnimation();
                    }
                }
            );
            textView.setVisibility(INVISIBLE);
        }
    }

    private void startAnimation() {
        Log.d(TAG, "Start animation.");
        initAnimation();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animators);
        animatorSet.start();
    }

    protected void initAnimation() {
        if (textView != null) {
            ObjectAnimator animator;
            if (textMessage.getUserType() == TextMessage.CLIENT) {
                animator = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(),
                        R.animator.text_message_view_right_animation);
                textView.setPivotX(textView.getMeasuredWidth());
                textView.setPivotY(textView.getMeasuredHeight());
            } else if (textMessage.getUserType() == TextMessage.COMPANY) {
                animator = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(),
                        R.animator.text_message_view_left_animation);
                textView.setPivotX(0);
                textView.setPivotY(textView.getMeasuredHeight());
            } else {
                Log.e(TAG, "User type is invalid, cannot animate");
                return;
            }

            AddAnimatorToView(textView, animator);
            animators.add(animator);
        } else {
            Log.e(TAG, "Text view is empty, cannot animate");
        }
    }

    protected void AddAnimatorToView(final View view, Animator animator) {
        animator.setTarget(view);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public TextMessage getTextMessage() {
        return this.textMessage;
    }

    private TextMessage textMessage;

    protected TextView textView;

    protected List<Animator> animators;

    private static final String TAG = "TextMessageView";
}
