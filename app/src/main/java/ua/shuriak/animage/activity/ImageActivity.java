package ua.shuriak.animage.activity;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import ua.shuriak.animage.R;

public class ImageActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageView;
    TextView dimension,size;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.image);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView)findViewById(R.id.imageView);
        dimension = (TextView)findViewById(R.id.dimension);
        size = (TextView)findViewById(R.id.size);

        Intent i = getIntent();
        url = i.getStringExtra("url");
        imageView.setImageBitmap((Bitmap) i.getParcelableExtra("bitmap"));

        dimension.setText(i.getIntExtra("width", -1) + "x" + i.getIntExtra("height", 0));

        if (i.getIntExtra("size", 0)==0) //Safebooru doesn't provide image size information
            getSize(url);
        else size.setText(String.format("%.2f", (float) i.getIntExtra("size", 0) / 1024 / 1024) + " MB");
    }

    public void openFullScreen(View v){
        Intent i = new Intent(this,FullImageActivity.class);
        i.putExtra("url", url);
        startActivity(i);
    }

    public void downloadImage(View v) throws UnsupportedEncodingException {
        DownloadManager dm = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        url = URLDecoder.decode(url, "UTF-8");
        String file_title = url.substring(url.lastIndexOf("/"),url.length());
        Log.wtf("url = ", url);
        Log.wtf("title = ",file_title);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),file_title);
        request.setTitle(file_title);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        request.setDestinationUri(Uri.fromFile(file));
        dm.enqueue(request);
    }

    void getSize(String url) {
        StringRequest request = new StringRequest(Request.Method.HEAD,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {}
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                }
        ){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String,String> map = response.headers;
                String data = map.get("Content-Length");
                Log.wtf("","size = "+data);
                size.setText(String.format("%.2f", Float.parseFloat(data) / 1024 / 1024) + " MB");
                return super.parseNetworkResponse(response);
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return(super.onOptionsItemSelected(item));
    }
}
