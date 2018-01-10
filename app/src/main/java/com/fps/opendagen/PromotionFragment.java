package com.fps.opendagen;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PromotionFragment extends Fragment {

    private String promotionUri;

    public PromotionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        promotionUri = getArguments().getString(getString(R.string.CURPROMOTIONURI));

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promotion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvInfo = (TextView) getView().findViewById(R.id.tvPromotionId);

        WebView wvPromotion = (WebView)getView().findViewById(R.id.wvPromotion);
        WebSettings webSettings = wvPromotion.getSettings();
        webSettings.setJavaScriptEnabled(true);



        if (!promotionUri.isEmpty()) {
            wvPromotion.setVisibility(View.VISIBLE);
            tvInfo.setVisibility(View.GONE);

            wvPromotion.loadUrl(promotionUri);
        } else {
            wvPromotion.setVisibility(View.GONE);
            tvInfo.setVisibility(View.VISIBLE);

            ((TextView) getView().findViewById(R.id.tvPromotionId)).setText("Nog geen Live informatie gevonden.");
        }
    }
}
