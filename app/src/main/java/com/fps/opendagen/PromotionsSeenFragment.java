package com.fps.opendagen;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fps.opendagen.PromotionSeenContent.PromotionSeenItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PromotionsSeenFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PromotionsSeenFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*
        View view = inflater.inflate(R.layout.fragment_promotionsseen_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            Map<String, String> promotionsSeen = ((MainActivity) getActivity()).promotionsSeen;
            List<PromotionSeenItem> seenList = new ArrayList<>();
            int count = 0;
            for (String seen : promotionsSeen.values()) {
                try {
                    count++;
                    JSONObject seenJson = new JSONObject(seen);
                    seenList.add(new PromotionSeenItem(
                            seenJson.getString("name"),
                            seenJson.getString("title"),
                            seenJson.getString("message"),
                            seenJson.getString("uri")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            recyclerView.setAdapter(new PromotionsSeenRecyclerViewAdapter(seenList, mListener));
        }
        return view;
        */

        RecyclerView recycler = (RecyclerView)inflater.inflate(
                R.layout.fragment_promotionsseen_list, container, false);

        Map<String, String> promotionsSeen = ((MainActivity) getActivity()).promotionsSeen;
        List<PromotionSeenItem> seenList = new ArrayList<>();
        int count = 0;
        for (String seen : promotionsSeen.values()) {
            try {
                count++;
                JSONObject seenJson = new JSONObject(seen);
                seenList.add(new PromotionSeenItem(
                        seenJson.getString("name"),
                        seenJson.getString("title"),
                        seenJson.getString("message"),
                        seenJson.getString("uri")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        PromotionsSeenRecyclerViewAdapter adapter = new PromotionsSeenRecyclerViewAdapter(seenList, mListener);

        recycler.setAdapter(adapter);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager1);
        recycler.setItemAnimator(new DefaultItemAnimator());

       // GridLayoutManager layoutManager2 = new GridLayoutManager(getActivity(), 2);
       // recycler.setLayoutManager(layoutManager2);

        adapter.setItemClickListener(new PromotionsSeenRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onClick(String uri) {
                // Stuur de uri door naar de FragmentInteraction listener
                mListener.onListFragmentInteraction(uri);
            }
        });

        return recycler;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Stuur de url van een promotion na een click op een card door naar de MainActivity
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(String uri);
    }
}
