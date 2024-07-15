package com.pb.criconet.layout;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
public class ExpandableTextView extends AppCompatTextView {

    private static Context context;
    private TextView textView;
    private int maxLine = 3;
    private boolean isViewMore = true;

    public ExpandableTextView(Context context) {
        super(context);
        ExpandableTextView.context = context;
        textView = this;
        initViews();
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ExpandableTextView.context = context;
        textView = this;
        initViews();
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ExpandableTextView.context = context;
        textView = this;
        initViews();
    }

    public void initViews() {
        if (textView.getText().toString().isEmpty()) {
            return;
        }
        if (textView.getTag() == null) {
            textView.setTag(textView.getText());
        }

        //textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/opensans_semibold.ttf"));
        ViewTreeObserver vto = textView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text, expandText = "See ";
                int lineEndIndex;
                ViewTreeObserver obs = textView.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                int lineCount = textView.getLayout().getLineCount();

                expandText += isViewMore ? "More" : "Less";
                if (lineCount <= maxLine) {
                    lineEndIndex = textView.getLayout().getLineEnd(textView.getLayout().getLineCount() - 1);
                    text = textView.getText().subSequence(0, lineEndIndex).toString();
                } else if (isViewMore && maxLine > 0 && textView.getLineCount() >= maxLine) {
                    lineEndIndex = textView.getLayout().getLineEnd(maxLine - 1);
                    text = textView.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = textView.getLayout().getLineEnd(textView.getLayout().getLineCount() - 1);
                    text = textView.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                textView.setText(text);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                if (lineCount > maxLine)
                    textView.setText(addClickablePartTextViewResizable(expandText),
                            BufferType.SPANNABLE);
                textView.setSelected(true);
            }
        });
    }

    private SpannableStringBuilder addClickablePartTextViewResizable(final String expandText) {
        String string = textView.getText().toString();
        SpannableStringBuilder expandedStringBuilder = new SpannableStringBuilder(string);
        if (string.contains(expandText)) {
            expandedStringBuilder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    textView.setLayoutParams(textView.getLayoutParams());
                    textView.setText(textView.getTag().toString(), BufferType.SPANNABLE);
                    textView.invalidate();
                    maxLine = isViewMore ? -1 : 3;
                    isViewMore = !isViewMore;
                    initViews();
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setUnderlineText(true);
                    ds.setColor(context.getResources().getColor(com.potyvideo.library.R.color.red));
                    //ds.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/opensans_semibold.ttf"));

                }

            }, string.indexOf(expandText), string.length(), 0);
        }
        return expandedStringBuilder;
    }
}
