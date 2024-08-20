package com.example.myevents;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;


public class Participant extends androidx.appcompat.widget.AppCompatTextView {
    public Participant(Context context) {
        super(context);
        init();
    }

    public Participant(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Participant(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        int size = 20;
        // עיצוב עיגול
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(Color.parseColor("#A8A99387")); // צבע הרקע

        drawable.setSize(size, size);
        setBackground(drawable);

        // הגדרת גובה ורוחב
        setPadding(16, 16, 16, 16);
        setTextColor(Color.BLACK);
        setGravity(Gravity.CENTER);
        setTextSize(14);
    }
}
