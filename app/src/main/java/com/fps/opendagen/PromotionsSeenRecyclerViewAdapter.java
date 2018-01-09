package com.fps.opendagen;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fps.opendagen.PromotionsSeenFragment.OnListFragmentInteractionListener;
import com.fps.opendagen.PromotionSeenContent.PromotionSeenItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PromotionSeenItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PromotionsSeenRecyclerViewAdapter extends RecyclerView.Adapter<PromotionsSeenRecyclerViewAdapter.ViewHolder> {

    private final List<PromotionSeenItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public static interface ItemClickListener {
        public void onClick(String uri);
    }

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public PromotionsSeenRecyclerViewAdapter(List<PromotionSeenItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext())
        //        .inflate(R.layout.fragment_promotionsseen, parent, false);
        //return new ViewHolder(view);

        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promotion_card, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        /*
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        */

        CardView cardView = holder.cardView;
        TextView tvPromotionTitle = (TextView)cardView.findViewById(R.id.tvPromotionTitle);
        tvPromotionTitle.setText(mValues.get(position).title);

        TextView tvPromotionMessage = (TextView)cardView.findViewById(R.id.tvPromotionTitle);
        tvPromotionMessage.setText(mValues.get(position).message);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onClick(mValues.get(position).uri);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /*
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public PromotionSeenItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
    */

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }
}
