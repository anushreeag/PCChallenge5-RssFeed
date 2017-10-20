package com.cosmic.personalcapitalchallenge.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.cosmic.personalcapitalchallenge.R;

public class WebViewActivity extends AppCompatActivity {

    private static final String LINK = "link";
    private static final String TITLE = "title";
    Toolbar toolbar;
    RelativeLayout relLayout;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpParentRelativeLayout();
        setUpToolbar();
        setUpWebView();
        relLayout.addView(toolbar);
        relLayout.addView(webView);
        setContentView(relLayout);
    }

    /* Sets up a relative layout instance which acts as
        a parent that holds toolbar, Web View in it
     */
    private void setUpParentRelativeLayout(){
        relLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        relLayout.setLayoutParams(relParams);

    }

    /* Sets up a Toolbar with color of title, background
     */
    private void setUpToolbar(){
        toolbar = new Toolbar(this);
        RelativeLayout.LayoutParams toolbarParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toolbar.setLayoutParams(toolbarParams);
        toolbar.setId(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra(TITLE));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
    }

    /* Sets up the webView and loads the URL in it using webView Client

     */

    private void setUpWebView(){
        webView = new WebView(this);
        RelativeLayout.LayoutParams webParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        webParams.addRule(RelativeLayout.BELOW,R.id.toolbar);
        webView.setLayoutParams(webParams);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(getIntent().getStringExtra(LINK));
    }



}
