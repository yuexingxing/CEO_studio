package com.tajiang.ceo.common.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by ciko on 16/4/25.
 */
public class ImageUtils {

    public static void loadImage(Context context , ImageView imageView, String url){
        loadImage(context, imageView, url, null);
    }

    public static void loadImage(Context context , ImageView imageView, String url, Integer placeholderImageResId){

        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) imageView;

        if(placeholderImageResId != null){
            GenericDraweeHierarchy genericDraweeHierarchy = simpleDraweeView.getHierarchy();
            genericDraweeHierarchy.setPlaceholderImage(placeholderImageResId);
        }

        Uri uri = Uri.parse(url + "");
        simpleDraweeView.setImageURI(uri);

    }

}
