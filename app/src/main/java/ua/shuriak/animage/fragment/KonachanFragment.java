package ua.shuriak.animage.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ua.shuriak.animage.activity.MainActivity;
import ua.shuriak.animage.R;
import ua.shuriak.animage.adapter.KonachanAdapter;
import ua.shuriak.animage.object.KonachanObject;

public class KonachanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View view;

    Context context;

    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;

    GridLayoutManager manager;
    KonachanAdapter adapter;
    List<KonachanObject> list;

    int page;
    boolean isLoading;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        view = inflater.inflate(R.layout.fragment_grid_list, null);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler);

        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(this);

        list = new ArrayList<>();
        adapter = new KonachanAdapter(context,list);

        request(false);

        manager = new GridLayoutManager(context,
                getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE ? 4 : 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new ScrollListener());
        return view;
    }

    void request(final boolean loadMore){
        if(!loadMore) page = 1;
        else page++;

        String url = "http://konachan.com/post.json?page="+page+
                "&limit=50&tags=rating%3As";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!loadMore) list.clear();
                        list.addAll(getList(response));
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                        if(refreshLayout.isRefreshing())
                            refreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(((MainActivity) getActivity()).coordinatorLayout, "Error", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }
        );

        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public void onRefresh() {
        request(false);
    }

    class ScrollListener extends RecyclerView.OnScrollListener{

        int visibleItemCount,totalItemCount,pastVisiblesItems;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            visibleItemCount = manager.getChildCount();
            totalItemCount = manager.getItemCount();
            pastVisiblesItems = manager.findFirstVisibleItemPosition();

            if (!isLoading && (visibleItemCount + pastVisiblesItems + 12) >= totalItemCount){
                isLoading=true;
                request(true);
            }
        }
    }

    List<KonachanObject> getList(String json){
        Gson gson = new Gson();
        Type listType = new TypeToken<List<KonachanObject>>(){}.getType();
        return  gson.fromJson(json, listType);
    }

}
