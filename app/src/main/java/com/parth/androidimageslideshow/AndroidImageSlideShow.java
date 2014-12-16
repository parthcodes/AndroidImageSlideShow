package com.parth.androidimageslideshow;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AndroidImageSlideShow extends Activity {

    ViewPager viewPager;
    public static String imageFetchUrl = "http://pixabay.com/api/?username=parth1991&key=4a4de2723b92a3f0f221&image_type=photo&per_page=200";
    public static String[] categories = {"animals","sports", "cars", "bikes", "love", "music", "travel", "religion", "health", "beauty", "emotions",
            "machines", "computers", "buildings", "education", "nature", "people", "kids", "school", "family"};
    public static List<String> imagesUrl = new ArrayList<String>();
    public static List<String> imageUserNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_image_slide_show);

        //imagesUrlUrl[0]="http://pixabay.com/get/2614a7720b29557db552/1418521721/51a9466434f2e9be072fb7dc_640.jpg";
        loadData();


    }
    public void loadData(){
        AsyncHttpClient client = new AsyncHttpClient();
        int idx = new Random().nextInt(categories.length);
        client.get(imageFetchUrl+"&q="+categories[idx], new TextHttpResponseHandler() {

            @Override
            public void onStart(){
            }

            @Override
            public void onFinish(){

            }

            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

                //If there is an error retrieving data.
                Toast.makeText(AndroidImageSlideShow.this, "Cannot get the data. Please check your network connection", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {

                // Log.v(DATATAG, s);
                loadDataToArray(s);
                viewPager = (ViewPager) findViewById(R.id.viewPager);


                //Inner class of Pagetransformer for givinf Zoom like Animation effect
                //Reference:  https://developer.android.com/training/animation/

                viewPager.setPageTransformer(false,new ViewPager.PageTransformer(){

                    private static final float MIN_SCALE = 0.85f;
                    private static final float MIN_ALPHA = 0.5f;

                    @Override
                    public void transformPage(View view, float position) {
                        int pageWidth = view.getWidth();
                        int pageHeight = view.getHeight();

                        if (position < -1) { // [-Infinity,-1)
                            // This page is way off-screen to the left.
                            view.setAlpha(0);

                        } else if (position <= 1) { // [-1,1]
                            // Modify the default slide transition to shrink the page as well
                            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                            if (position < 0) {
                                view.setTranslationX(horzMargin - vertMargin / 2);
                            } else {
                                view.setTranslationX(-horzMargin + vertMargin / 2);
                            }

                            // Scale the page down (between MIN_SCALE and 1)
                            view.setScaleX(scaleFactor);
                            view.setScaleY(scaleFactor);

                            // Fade the page relative to its size.
                            view.setAlpha(MIN_ALPHA +
                                    (scaleFactor - MIN_SCALE) /
                                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                        } else { // (1,+Infinity]
                            // This page is way off-screen to the right.
                            view.setAlpha(0);
                        }
                    }
                });
                PagerAdapter adapter = new CustomAdapter(AndroidImageSlideShow.this);
                viewPager.setAdapter(adapter);


            }
        });
    }

    public void loadDataToArray(String s){

        //Load data to ArrayList once it is fetched.
        try {
            JSONObject json = new JSONObject(s);
            JSONArray imageArray = json.getJSONArray("hits");

            for(int i=0;i<imageArray.length();i++){
                imagesUrl.add(imageArray.getJSONObject(i).getString("webformatURL"));
                imageUserNames.add(imageArray.getJSONObject(i).getString("user"));
                //Log.v("ImageURL Check", imagesUrl[i]);
            }
        } catch (JSONException e) {

            Toast.makeText(AndroidImageSlideShow.this, "Error in parsing data", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.android_image_slide_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
