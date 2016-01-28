package ua.shuriak.animage.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.ArrayList;
import java.util.List;

import ua.shuriak.animage.activity.MainActivity;
import ua.shuriak.animage.R;
import ua.shuriak.animage.adapter.SafebooruAdapter;
import ua.shuriak.animage.object.SafebooruObject;

public class SafebooruFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View view;

    Context context;

    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;

    GridLayoutManager manager;
    SafebooruAdapter adapter;
    List<SafebooruObject.Post> list;

    Serializer serializer;
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
        adapter = new SafebooruAdapter(context,list);

        serializer = new Persister();
        request(false);

        manager = new GridLayoutManager(context,
                getActivity().getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE ? 4 : 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new ScrollListener());

        return view;
    }

    void request(final boolean loadMore){
        if(!loadMore) page = 1;
        else page++;
        
        String url = "http://safebooru.org/index.php?pid="+page+
                "&limit=50&tags=rating%3Asafe&page=dapi&s=post&q=index";



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(!loadMore) list.clear();
                            SafebooruObject object = serializer.read(SafebooruObject.class, response);
                            list.addAll(object.list);
                            adapter.notifyDataSetChanged();
                            isLoading = false;
                            if(refreshLayout.isRefreshing())
                                refreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(((MainActivity) getActivity()).coordinatorLayout, "Error", Snackbar.LENGTH_SHORT)
                                .show();
                    }
        });

        Volley.newRequestQueue(context).add(stringRequest);
    }

    class ScrollListener extends RecyclerView.OnScrollListener{

        int visibleItemCount, totalItemCount, pastVisiblesItems;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            visibleItemCount = manager.getChildCount();
            totalItemCount = manager.getItemCount();
            pastVisiblesItems = manager.findFirstVisibleItemPosition();

            if (!isLoading && (visibleItemCount + pastVisiblesItems + 12) >= totalItemCount){
                Log.wtf("","LoadMore");
                isLoading=true;
                request(true);
            }
        }
    }

    @Override
    public void onRefresh() {
        request(false);
    }
}
