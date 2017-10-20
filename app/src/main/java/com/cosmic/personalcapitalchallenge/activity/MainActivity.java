package com.cosmic.personalcapitalchallenge.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cosmic.personalcapitalchallenge.R;
import com.cosmic.personalcapitalchallenge.adapter.RssItemAdapter;
import com.cosmic.personalcapitalchallenge.models.RssPCBean;
import com.cosmic.personalcapitalchallenge.utils.CheckOnline;
import com.cosmic.personalcapitalchallenge.utils.MyPullParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String LINK = "link";
    private static final String TITLE = "title";

    RelativeLayout relLayout;
    RecyclerView rvRssFeed;
    GridLayoutManager grid;
    MyPullParser parser;
    RssItemAdapter rssAdp;
    boolean firstTime;

    Toolbar toolbar;
    SwipeRefreshLayout swipe;

    ArrayList<RssPCBean> rssList;
    ProgressDialog dialog;
    DrawerLayout drawerLayout;
    NavigationView navView;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpParentRelativeLayout();
        setUpRecyclerViewLayout();
        setUpToolBar();
        setUpdrawerLayout();

        //Xml PullParser Instance
        parser = new MyPullParser();
        dialog = new ProgressDialog(this);
        firstTime = true;
        setUpNavigationDrawer();
        addViews();
        setContentView(drawerLayout);
        if(!CheckOnline.isOnline()){
            setNotOnlineLayout();
            return;
        }
        parseandLoadFromRssFeed();
    }

    private void parseandLoadFromRssFeed(){
        ParserTask task = new ParserTask();
        task.execute();
    }

    // Adds all the child views to drawer layout

    private void addViews(){
        swipe.addView(rvRssFeed);
        relLayout.addView(toolbar);
        relLayout.addView(swipe);
        drawerLayout.addView(relLayout);
        drawerLayout.addView(navView);
    }

    /* Sets up a relative layout instance which acts as
        a parent that holds toolbar, recycler View in it
     */
    private void setUpParentRelativeLayout(){
        relLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        relLayout.setLayoutParams(relParams);

    }

    /* Sets up a recycler View
     */
    private void setUpRecyclerViewLayout(){
        RelativeLayout.LayoutParams recParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        rvRssFeed = new RecyclerView(this);
        rvRssFeed.setId(R.id.rssView);
        rvRssFeed.setLayoutParams(recParams);
        RelativeLayout.LayoutParams swipeParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        swipe = new SwipeRefreshLayout(this);
        swipe.setId(R.id.swipe);
        swipe.setLayoutParams(swipeParams);

        swipeParams.addRule(RelativeLayout.BELOW,R.id.toolbar);
        setLayoutManager();
        rssList = new ArrayList<>();
        rssAdp = new RssItemAdapter(MainActivity.this,rssList);
        rvRssFeed.setAdapter(rssAdp);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshClicked();
            }
        });


    }

    /* Sets up a layout manager for recycler View
     * 1. Sets a grid layout of 2 columns in case of Phone
     * 2. Sets a grid layout of 3 columns in case of Tablet
     */

    private void setLayoutManager(){
        if(getResources().getBoolean(R.bool.isTablet)) {

            grid = new GridLayoutManager(this, 3);
            grid.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                // If position is 0 -> 1st row,
                // we need span size to be 3 - That means all 3 columns will be merged to make 1
                public int getSpanSize(int position) {
                    return (position == 0 ? 3 : 1);
                }
            });
        }
        else{
            grid = new GridLayoutManager(this, 2);
            grid.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // If position is 0 -> 1st row,
                    // we need span size to be 2 - That means all 2 columns will be merged to make 1
                    return (position == 0 ? 2 : 1);
                }
            });
        }
        rvRssFeed.setLayoutManager(grid);
    }



    /* Sets up a Toolbar with color of title, background
     */
    private void setUpToolBar(){
        toolbar = new Toolbar(this);
        RelativeLayout.LayoutParams toolbarParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toolbar.setLayoutParams(toolbarParams);
        toolbar.setId(R.id.toolbar);
        toolbar.setTitle("         Research & Insights");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationIcon(R.drawable.actionbar_icon);
        toolbar.inflateMenu(R.menu.pcmenu);

        //Sets the toolbar as action bar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"setNavigationOnClickListener");
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    /* Sets up a Drawer Layout
     */
    private void setUpdrawerLayout(){
        drawerLayout = new DrawerLayout(this);
        DrawerLayout.LayoutParams drawerLayout_params=new DrawerLayout.LayoutParams(
                DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT);
        drawerLayout.setLayoutParams(drawerLayout_params);
        drawerLayout.setFitsSystemWindows(true);
    }

    /* Sets up a Layout when wifi or internet connection is not there
     */

    private void setNotOnlineLayout(){
        RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout notOnlineLayout = new RelativeLayout(this);
        Toolbar NotOnlinetoolbar = new Toolbar(this);
        RelativeLayout.LayoutParams toolbarParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        NotOnlinetoolbar.setLayoutParams(toolbarParams);
        NotOnlinetoolbar.setTitle("Personal Capital");
        NotOnlinetoolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        NotOnlinetoolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        notOnlineLayout.setLayoutParams(relParams);
        drawerLayout.setVisibility(View.GONE);
        tv = new TextView(this);
        RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tv.setTextSize(20);
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv.setPadding(10,10,10,10);
        tv.setLayoutParams(tvParams);
        tv.setText("Internet/Wifi is off. Please switch on Internet and try again");
        notOnlineLayout.addView(tv);
        notOnlineLayout.addView(NotOnlinetoolbar);
        setSupportActionBar(NotOnlinetoolbar);
        setContentView(notOnlineLayout);
    }

    /* Sets up a Navigation drawer with Navigation icon set
     * Clicking on the navigation icon opens the navigation drawer
     */

    private void setUpNavigationDrawer(){
        navView = new NavigationView(this);
        DrawerLayout.LayoutParams navParams = new DrawerLayout.LayoutParams(
                NavigationView.LayoutParams.WRAP_CONTENT,NavigationView.LayoutParams.MATCH_PARENT,(int)(Gravity.START));
        navParams.gravity = Gravity.START;
        navView.setBackgroundColor(getResources().getColor(android.R.color.white));
        navView.getMenu().clear();
        navView.inflateMenu(R.menu.drawer_menu);
        navView.setLayoutParams(navParams);
        navView.setNavigationItemSelectedListener(navSelectListener);
    }

    /* Listener for navigation icon that opens the navigation drawer

     */
    private NavigationView.OnNavigationItemSelectedListener navSelectListener = new NavigationView.OnNavigationItemSelectedListener(){
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.i(TAG,"onNavigationItemSelected");
            switch(item.getItemId()){
                case R.id.help:
                    prepareAlertDialog().show();
                    break;
                case R.id.about :
                    openAboutUs();
                    break;
                default : Log.i(TAG,"Invalid menu item");
            }

            drawerLayout.closeDrawers();
            return false;
        }
    };

    /* Prepares alert dialog for help menu item in navigation drawer
     */

    private AlertDialog prepareAlertDialog(){

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("Help").
                setMessage("1. This App shows research and insights of personal capital.\n" +
                        "2. Clicking on any news takes you to personal capital webpage for complete news\n"+
                        "3. Click on Refresh button to refresh the news\n"+
                        "4. Swipe the List from Up to Down to refresh.").create();

        return dialog;
    }

    /* Prepares intent for About Us menu item in navigation drawer
     */
    private void openAboutUs(){
        Intent intent = new Intent();
        intent.setClass(this,WebViewActivity.class);
        intent.putExtra(LINK,"https://www.personalcapital.com/company");
        intent.putExtra(TITLE,"About Us");
        Toast.makeText(this, "Loading the page, Please wait ...", Toast.LENGTH_SHORT).show();
        this.startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.pcmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!CheckOnline.isOnline()){
            item.setEnabled(false);
            return true;
        }

        if(item.getItemId()==R.id.refresh){
            dialog.setTitle("Refreshing.. Please wait");
            dialog.show();
            RefreshClicked();
        }

        return true;
    }

    /* Create an object of Asynctask and run it

     */

    private void RefreshClicked(){
        ParserTask task = new ParserTask();
        task.execute();
    }

    /* Asynctask ParserTask
     * Params : Null
     * Progress : Null
     * Result : Arraylist of the RSSFeedItems
     * Once the Result (Arraylist is ready) , the recycler view's Adapater is notified
     * and all the items are added to the list and adapter.
     */

    class ParserTask extends AsyncTask<Void,Void,ArrayList<RssPCBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(firstTime) {
                //Show the loading Dialog
                dialog.setTitle("Loading.. Please wait");
                dialog.show();
                firstTime = false;
            }
        }

        @Override
        protected ArrayList<RssPCBean> doInBackground(Void...params) {


            ArrayList<RssPCBean> feedlist = new ArrayList<>();
            try {


                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser myparse = factory.newPullParser();

                URL personalCapitalURL = new URL("https://blog.personalcapital.com/feed/?cat=3,891,890,68,284");
                HttpURLConnection conn = (HttpURLConnection) personalCapitalURL.openConnection();

                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream stream = conn.getInputStream();
                myparse.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                myparse.setInput(stream, "UTF-8");

                //parses the RSS feed mentioned in the URL
                feedlist.addAll(parser.parseXml(myparse));
                stream.close();
            } catch (Exception e) {
                Log.i(TAG,e.toString());
            }
            return feedlist;
        }

        @Override
        protected void onPostExecute(ArrayList<RssPCBean> rssBean) {
            super.onPostExecute(rssBean);
            //If dialog is showing dismiss the dialog
            if(dialog.isShowing())
                dialog.dismiss();

            //If swipe refresh is showing dismiss the swipe refresh progress icon
            if(swipe.isRefreshing())
                swipe.setRefreshing(false);
            rssList.clear();
            rssAdp.clear();
            rssList.addAll(rssBean);
            //notify the adapter after loading the list
            rssAdp.notifyDataSetChanged();

            //After refresh or loading for first, scroll to beginning of recycler view
            rvRssFeed.scrollToPosition(0);
        }
    }



}


