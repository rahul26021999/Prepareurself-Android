package com.prepare.prepareurself.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.prepare.prepareurself.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static boolean isValidEmail(CharSequence str_email){
        return(Patterns.EMAIL_ADDRESS.matcher(str_email).matches());
    }

    public static void showToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static String[] splitName(Context context, String name){
        return name.split(" ");
    }

    public static void redirectUsingCustomTab(Context context,String url) {
        Uri uri = Uri.parse(url);

        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

        CustomTabsIntent customTabsIntent = intentBuilder.build();

        customTabsIntent.launchUrl(context, uri);
    }

    public static String getVideoCode(String youtubeUrl){
        String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";

        Pattern compiledPattern = Pattern.compile(pattern,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(youtubeUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }/*from w  w  w.  j a  va  2 s .c om*/
        return null;
    }

    public static ViewPropertyTransition.Animator getAnimationObject(){

        return new ViewPropertyTransition.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha(0f);

                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeAnim.setDuration(2500);
                fadeAnim.start();
            }
        };
    }

}
