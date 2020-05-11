package com.prepare.prepareurself.utils;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYouListener;
import com.onesignal.OneSignal;
import com.prepare.prepareurself.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.ACTIVITY_SERVICE;

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

    public static String getVideoPlaylistId(String youtubeUrl){
        String temp = youtubeUrl.split("list=")[1];

        if (temp.contains("&")){
            temp = temp.split("&")[0];
        }

        return temp;
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

    public static int getNextPageNumber(String next_page_url) {
        return Integer.parseInt(next_page_url.split("=")[1]);
    }

    public static Bitmap getBitmapFromView(View view){
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable!=null){
            bgDrawable.draw(canvas);
        }else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public static Uri getUriOfBitmap(Bitmap bitmap, Context context) throws IOException {

        File file = new File(context.getExternalCacheDir(),"share.png");
        FileOutputStream fout = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
        fout.flush();
        fout.close();
        file.setReadable(true, false);

        return Uri.fromFile(file);
    }

    public static Uri getUriOfBitmapJPG(Bitmap bitmap, Context context) throws IOException {

        File file = new File(context.getExternalCacheDir(),"share.png");
        FileOutputStream fout = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
        fout.flush();
        fout.close();
        file.setReadable(true, false);

        return Uri.fromFile(file);
    }

    public static Uri getUriOfBitmapCompress(Bitmap bitmap, Context context, int compress) throws IOException {

        File file = new File(context.getExternalCacheDir(),"share.png");
        FileOutputStream fout = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, compress, fout);
        fout.flush();
        fout.close();
        file.setReadable(true, false);

        return Uri.fromFile(file);
    }

    public static void shareContent(Context context,Uri bitmapUri, String text){
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        intent.setType("image/png");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent,"Share Via"));
    }

    public static String base64EncodeForString(String id) throws UnsupportedEncodingException {
        byte[] data = String.valueOf(id).getBytes("UTF-8");
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static String base64DecodeForString(String tempData) throws UnsupportedEncodingException {
        String encoded_id = tempData.substring(1);
        byte[] data1 = Base64.decode(encoded_id, Base64.DEFAULT);
        return new String(data1, "UTF-8");
    }

    public static String base64EncodeForInt(int id) throws UnsupportedEncodingException {
        byte[] data = String.valueOf(id).getBytes("UTF-8");
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static int base64DecodeForInt(String tempData) throws UnsupportedEncodingException {
        String encoded_id = tempData.substring(1);
        byte[] data1 = Base64.decode(encoded_id, Base64.DEFAULT);
        String text = new String(data1, "UTF-8");
        return Integer.parseInt(text);
    }

    public static Date stringToDate(String date) throws ParseException {

        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(date);
    }

    public static String getDurationBetweenTwoDays(String endDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String output = "";
        try {
            String startDate = dateFormat.format(new Date());
            Date mStartDate = dateFormat.parse(startDate);
            Calendar start = Calendar.getInstance();
            start.setTime(mStartDate);
            Date mEndDate = dateFormat.parse(endDate);
            Calendar end = Calendar.getInstance();
            end.setTime(mEndDate);
            long diff = mStartDate.getTime() - mEndDate.getTime();

            long days = (diff / (1000 * 60 * 60 * 24)) % 365;
            long months = (diff / (1000l * 60 * 60 * 24 * 30 )) % 12;
            long weeks = (diff / (1000l * 60 * 60 * 24 * 7 )) % 7;
            long years =  (diff / (1000l * 60 * 60 * 24 * 365));

            Log.d("Date_debug","days: "+ days+"| weeks : "+weeks+" months : "+months +" years : "+years);

            if (years == 1){
                output = years + " year ago";
            }else if (years>1){
                output = years + " years ago";
            }else if (years == 0){
                if (months == 1){
                    output = months + " month ago";
                }else if (months>1){
                    output = months + " months ago";
                }else if (months == 0){
                    if (weeks == 1){
                        output = weeks + " week ago";
                    }else if (weeks>1){
                        output = weeks + " weeks ago";
                    }else if (weeks == 0){
                        if (days == 1){
                            output = days + " day ago";
                        }else if (days>1){
                            output = days + " days ago";
                        }else if (days == 0){
                            output = "";
                        }
                    }
                }
            }

//            if (days<7){
//                output = days+" days ago";
//            }else if (weeks<=5){
//                output = weeks + " weeks ago";
//            } else if (months < 12){
//                output = months+" months ago";
//            }else if (years == 1){
//                output = years + "year ago";
//            }else if (years >1){
//
//            }else{
//                output = "";
//            }

//            Log.d("Date_debug", "getDurationBetweenTwoDays: "+(calendarDiff.get(Calendar.YEAR) - 1970 )+" year, "+calendarDiff.get(Calendar.WEEK_OF_MONTH)+" weeks, "+calendarDiff.get(Calendar.MONTH)+" months, "+calendarDiff.get(Calendar.DAY_OF_MONTH)+" days");

        } catch (ParseException e) {
            e.printStackTrace();
            output = "";
        }


        return output;
    }


    public static String getOneSignalId(){
        return OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
    }

    public static void loadImage(final Context context, String ImageUrl, ImageView imageView){
        if (ImageUrl.endsWith(".svg")){
            loadSVGImage(context, ImageUrl, imageView);
        }else{
            loadNormalImageUsingGlide(context, ImageUrl,imageView);
        }
    }

    public static void loadNormalImageUsingGlide(final Context context, String ImageUrl, ImageView imageView){
        Glide.with(context).load(
                ImageUrl)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                .error(R.drawable.placeholder)
                .into(imageView);
    }

    public static void loadSVGImage(final Context context, String ImageUrl, ImageView imageView){
        GlideToVectorYou
                .init()
                .with(context)
                .withListener(new GlideToVectorYouListener() {
                    @Override
                    public void onLoadFailed() {
                       // Toast.makeText(context, "Load failed", Toast.LENGTH_SHORT).show()
                    }

                    @Override
                    public void onResourceReady() {
                       // Toast.makeText(context, "Image ready", Toast.LENGTH_SHORT).show()
                    }
                })
                .setPlaceHolder(R.drawable.placeholder,R.drawable.placeholder)
                .load(Uri.parse(ImageUrl), imageView);
    }

    public static void clearAppData(Context context){
        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
            ((ActivityManager) context.getSystemService(ACTIVITY_SERVICE))
                    .clearApplicationUserData();
        }
    }

}
