package ua.shuriak.animage.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import ua.shuriak.animage.activity.ImageActivity;
import ua.shuriak.animage.R;
import ua.shuriak.animage.ViewHolder;
import ua.shuriak.animage.object.DanbooruObject;

public class DanbooruAdapter extends RecyclerView.Adapter<ViewHolder> {

    Context context;
    List<DanbooruObject> list;

    public DanbooruAdapter(Context context, List<DanbooruObject> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DanbooruObject image = list.get(position);

        holder.textView.setText(image.width + "x" + image.height);
        Log.wtf("", position + " http://danbooru.donmai.us" + image.preview_url);
        Picasso.with(context).load("http://danbooru.donmai.us" + image.preview_url)
                .into(holder.imageView);
        holder.setOnClick(new ViewHolder.HolderClick() {
            @Override
            public void startActivity() {
                Intent i = new Intent(context, ImageActivity.class);
                i.putExtra("width", image.width);
                i.putExtra("height", image.height);
                i.putExtra("size", image.file_size);
                i.putExtra("url", "http://danbooru.donmai.us" + image.file_url);
                i.putExtra("bitmap", ((BitmapDrawable) holder.imageView.getDrawable()).getBitmap());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
