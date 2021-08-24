package com.techamove.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.techamove.R;
import com.techamove.Utils.AppPreferences;
import com.techamove.Utils.Constants;
import com.techamove.Utils.Utility;
import com.techamove.view.BusinessCardShare.ShareCardActivity;
import com.techamove.view.Home.HomeActivity;
import com.techamove.view.Login.NewLoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    Animation anim;
    @BindView(R.id.imgLogo)
    ImageView imgLogo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Utility.getPref(getApplicationContext(), Constants.PREF_LANGUAGE, "").equalsIgnoreCase(Constants.ENGLISH)) {
                            Utility.localization(Constants.ENGLISH_CODE, SplashActivity.this);
                        } else if (Utility.getPref(getApplicationContext(), Constants.PREF_LANGUAGE, "").equalsIgnoreCase(Constants.GERMAN)) {
                            Utility.localization(Constants.GERMAN_CODE, SplashActivity.this);
                        } else {
                            Utility.localization(Constants.DANISH_CODE, SplashActivity.this);
                        }
                        if (AppPreferences.getInstance(getApplicationContext()).getID() == 0) {
                            Intent mainIntent = new Intent(SplashActivity.this, NewLoginActivity.class);
                            SplashActivity.this.startActivity(mainIntent);
                            SplashActivity.this.finish();
                        } else {
                            if (AppPreferences.getInstance(getApplicationContext()).getAccountType().equals("2")) {
                                Intent i = new Intent(SplashActivity.this, ShareCardActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();

                            }

                        }

                    }
                }, 3000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imgLogo.startAnimation(anim);
    }
}
