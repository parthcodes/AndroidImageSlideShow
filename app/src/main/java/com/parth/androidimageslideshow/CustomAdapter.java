package com.parth.androidimageslideshow;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


/**
 * Created by Parth on 12/13/14.
 */
public class CustomAdapter extends PagerAdapter {

    Context context;
    int imageCount = Integer.MAX_VALUE;

    public static final String DATATAG = "Data :"; // For logging purpose

    public CustomAdapter(Context context){
        this.context = context;

    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        View viewItem = inflater.inflate(R.layout.image_item, container, false);
        ImageView imageView = (ImageView) viewItem.findViewById(R.id.imageView);

        Log.v("ImageURL Check", AndroidImageSlideShow.imagesUrl.get(position));

        if(position==(AndroidImageSlideShow.imagesUrl.size()-5)){ // if last 4 images remaining then Get more images in the arrayList.
            loadExtraData();
        }

        //Download and show images using Picasso Image library.
            Picasso.with(context)
                    .load(AndroidImageSlideShow.imagesUrl.get(position))
                    .noFade()
                    .into(imageView);


        //imageView.setImageResource(imageId[position]);

        TextView textView1 = (TextView) viewItem.findViewById(R.id.imageName);
        textView1.setText(AndroidImageSlideShow.imageUserNames.get(position));
        ((ViewPager)container).addView(viewItem);

        return viewItem;
    }

    private void loadExtraData() {
        AsyncHttpClient client = new AsyncHttpClient();
        int idx = new Random().nextInt(AndroidImageSlideShow.categories.length);
        client.get(AndroidImageSlideShow.imageFetchUrl+"&q="+AndroidImageSlideShow.categories[idx], new TextHttpResponseHandler() {

            @Override
            public void onStart(){
            }

            @Override
            public void onFinish(){

            }

            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {

                try {
                    JSONObject json = new JSONObject(s);
                    JSONArray imageArray = json.getJSONArray("hits");

                    //int counter=AndroidImageSlideShow.imagesUrl.size()-1;
                    for(int j=0;j<imageArray.length();j++){
                        AndroidImageSlideShow.imagesUrl.add(imageArray.getJSONObject(j).getString("webformatURL"));
                        AndroidImageSlideShow.imageUserNames.add(imageArray.getJSONObject(i).getString("user"));
                      //  Log.v("ImageURL Check", AndroidImageSlideShow.imagesUrl.get(counter));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getCount() {
        return imageCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == ((View)o);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ((ViewPager) container).removeView((View) object);
    }


}
