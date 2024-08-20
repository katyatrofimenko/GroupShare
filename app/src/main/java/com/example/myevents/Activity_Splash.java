package com.example.myevents;


import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_Splash extends AppCompatActivity {

    private AppCompatImageView splash_IMG_icon;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_splash), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        splash_IMG_icon = findViewById(R.id.splash_IMG_icon);
        animate(splash_IMG_icon);
    }

    private void animate(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setY( - displayMetrics.heightPixels/2 - view.getHeight());
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        view
                .animate()
                .scaleY(1.0f)
                .scaleX(1.0f)
                .translationY(0)
                .translationY(0)
                .setDuration(5000)
                .setInterpolator(new LinearInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        startApp();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) { }

                    @Override
                    public void onAnimationRepeat(Animator animator) { }
                });
    }

    private void startApp() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
