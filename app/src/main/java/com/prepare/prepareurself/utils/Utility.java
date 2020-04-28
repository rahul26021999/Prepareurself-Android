package com.prepare.prepareurself.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

}
