package com.fps.opendagen;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.fps.opendagen.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaticPromotionFragment extends Fragment {

    private String promotionUri;

    public StaticPromotionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        promotionUri = getArguments().getString(getString(R.string.CURPROMOTIONURI));

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_static_promotion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebView wvStaticPromotion = (WebView) getView().findViewById(R.id.wvStaticPromotion);
        wvStaticPromotion.loadUrl(promotionUri);
    }

}
