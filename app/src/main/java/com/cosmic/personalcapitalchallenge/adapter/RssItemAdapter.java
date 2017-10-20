package com.cosmic.personalcapitalchallenge.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cosmic.personalcapitalchallenge.R;
import com.cosmic.personalcapitalchallenge.activity.MainActivity;
import com.cosmic.personalcapitalchallenge.activity.WebViewActivity;
import com.cosmic.personalcapitalchallenge.models.RssPCBean;
import com.cosmic.personalcapitalchallenge.utils.DownloadImageTask;
import com.cosmic.personalcapitalchallenge.utils.LayoutsfromCode;

import java.util.ArrayList;

/**
 * Created by anushree on 10/16/2017.
 * Adapter to Rss feed recycler view : rvRssFeed
 * Loads every item - which has a title, description and image
 */

public class RssItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LINK = "link";
    private static final String TITLE = "title";

    Context mCtx;
    ArrayList<RssPCBean> list;

    public RssItemAdapter(Context context, ArrayList<RssPCBean> rssList) {
        mCtx = context;
        list = rssList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh = null;
        vh = new ViewHolderFirst(getLayoutViews());
                return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

           ViewHolderFirst vh1 = (ViewHolderFirst) holder;
           vh1.bind(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    /* View holder for the adapter
        On click of every item in recycler view - linked is opened in an embedded webView
     */
    public class ViewHolderFirst extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv;
        TextView desc;
        ImageView image;
        CardView card;

        public ViewHolderFirst(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.Viewholdertv);
            desc = (TextView) itemView.findViewById(R.id.descTv);
            image = (ImageView) itemView.findViewById(R.id.image);
            card = (CardView) itemView.findViewById(R.id.cardView);

            card.setOnClickListener(this);

        }

        public void bind(RssPCBean bean){



            if(getAdapterPosition() == 0){
                desc.setVisibility(View.VISIBLE);
                tv.setMaxLines(1);
                desc.setMaxLines(2);
            }
            else{
                desc.setVisibility(View.GONE);
                tv.setMaxLines(2);
            }

            tv.setText(Html.fromHtml(bean.getTitle(),Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));
            desc.setText(bean.getPubDate()+" - "+Html.fromHtml(bean.getDescription(),Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));

            /*  Async task to download the image of the RSsfeed

             */
            DownloadImageTask task = new DownloadImageTask(image,mCtx);
            task.execute(bean.getImageURL());
        }

        /*
            OnClick listener for item of recycler view
         */

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Intent intent = new Intent();
            intent.setClass(mCtx,WebViewActivity.class);
            intent.putExtra(LINK,list.get(pos).getLink());
            intent.putExtra(TITLE,list.get(pos).getTitle());
            Toast.makeText(mCtx, "Loading the page, Please wait ...", Toast.LENGTH_SHORT).show();
            mCtx.startActivity(intent);
        }
    }

    /* Prepare all the child Views
     * Adds to the parent view
     * returns the view group which is the parent
     */

    public View getLayoutViews(){
        RelativeLayout parent = (RelativeLayout) LayoutsfromCode.getRelativeLayout(mCtx);
        parent.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        RelativeLayout layout = (RelativeLayout) LayoutsfromCode.getRelativeLayout(mCtx);
        CardView card = (CardView) LayoutsfromCode.getCardView(mCtx);
        TextView title = (TextView) LayoutsfromCode.getTitleTextView(mCtx);
        TextView desc = (TextView) LayoutsfromCode.getDescTextView(mCtx);
        ImageView image = (ImageView) LayoutsfromCode.getImageView(mCtx);
        ProgressDialog dialog = (ProgressDialog) LayoutsfromCode.getProgressDialog(mCtx);


        // Add the child views to parent
        layout.addView(image);
        layout.addView(title);
        layout.addView(desc);
        card.addView(layout);
        parent.addView(card);
        return parent;
    }


    /* Clear the adapter

     */
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }



}
