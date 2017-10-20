package com.cosmic.personalcapitalchallenge.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cosmic.personalcapitalchallenge.R;

/**
 * Created by anushree on 10/17/2017.
 * Util class for getting relative layout, card views, text views, image views
 */

public class LayoutsfromCode {

    public static View getRelativeLayout(Context context){
        RelativeLayout layout = new RelativeLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        layout.setPadding(10,10,10,10);
        return layout;
    }

    public static View getCardView(Context context){
        CardView card = new CardView(context);
        card.setId(R.id.cardView);
        card.setCardElevation(10);
        card.setMaxCardElevation(15);
        card.setForeground(getSelectedItemDrawable(context));
        card.setRadius(9);
        card.setClickable(true);
        card.setPreventCornerOverlap(false);
        card.setBackgroundResource(R.drawable.cardview_border);
        card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        card.setContentPadding(15,15,15,15);
        return card;
    }


    public static View getTitleTextView(Context mCtx){
        TextView tv = new TextView(mCtx);
        tv.setId(R.id.Viewholdertv);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW,R.id.image);
        tv.setLayoutParams(params);
        Typeface title_font = Typeface.createFromAsset(mCtx.getResources().getAssets(),"font/OpenSans-Bold.ttf");
        tv.setTypeface(title_font);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setTextSize(15);
        return tv;
    }


    public static View getDescTextView(Context mCtx){
        TextView tv = new TextView(mCtx);
        tv.setId(R.id.descTv);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW,R.id.Viewholdertv);
        tv.setLayoutParams(params);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        Typeface desc_font = Typeface.createFromAsset(mCtx.getResources().getAssets(),"font/OpenSans-Regular.ttf");
        tv.setTypeface(desc_font);
        tv.setMaxLines(2);
        tv.setTextSize(10);
        return tv;
    }


    public static View getImageView(Context mCtx){
        ImageView image = new ImageView(mCtx);
        image.setId(R.id.image);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        image.setLayoutParams(params);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        return image;
    }

    /* To give the ripple effect on card view click
     */
    private static Drawable getSelectedItemDrawable(Context ctx) {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray ta = ctx.obtainStyledAttributes(attrs);
        Drawable selectedItemDrawable = ta.getDrawable(0);
        ta.recycle();
        return selectedItemDrawable;
    }


}
