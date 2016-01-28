package ua.shuriak.animage.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import ua.shuriak.animage.R;

public class FullImageActivity extends AppCompatActivity {

    SubsamplingScaleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
        Intent i = getIntent();
        ImageRequest imgRequest = new ImageRequest(i.getStringExtra("url"),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        findViewById(R.id.progress).setVisibility(View.GONE);
                        imageView.setImage(ImageSource.bitmap(response));
                    }
                }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        findViewById(R.id.progress).setVisibility(View.GONE);
                    }
        });

        Volley.newRequestQueue(this).add(imgRequest);
    }
}
