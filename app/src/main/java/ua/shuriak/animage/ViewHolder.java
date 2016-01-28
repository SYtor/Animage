package ua.shuriak.animage;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView textView;
    public ImageView imageView;
    public HolderClick holderClick;

    public ViewHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.textView);
        imageView = (ImageView)itemView.findViewById(R.id.imageView);
    }

    public void setOnClick(HolderClick listener){
        holderClick = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        holderClick.startActivity();
    }

    public static interface HolderClick{
        public void startActivity();
    }
}