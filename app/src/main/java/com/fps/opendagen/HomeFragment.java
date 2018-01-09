package com.fps.opendagen;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
   }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean registered = !((MainActivity) getActivity()).userId.isEmpty();
        ConstraintLayout registerLayout = (ConstraintLayout) getView().findViewById(R.id.clRegister);
        ConstraintLayout registerDoneLayout = (ConstraintLayout) getView().findViewById(R.id.clRegisterDone);

        registerLayout.setVisibility(registered ? View.GONE : View.VISIBLE);
        registerDoneLayout.setVisibility(registered ? View.VISIBLE : View.GONE);

        TextView intro = (TextView) getView().findViewById(R.id.tvIntro);
        intro.setText("Welkom bij de Open dagen van\nROC Friese Poort Sneek!\n\nLaat je gegevens achter zodat\nwe je op de hoogte kunnen houden.");


        TextView registerDone = (TextView) getView().findViewById(R.id.tvRegisterDone);
        registerDone.setText("Bedankt, je bent aangemeld.");

        Button btnSend = (Button) getView().findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendClick(v);
            }
        });
    }


    // Verstuur de gegevens naar onze database
    public void onSendClick(View view) {
        String email = ((EditText) getView().findViewById(R.id.etEmail)).getText().toString();
        String name = ((EditText) getView().findViewById(R.id.etName)).getText().toString();

        if (email.isEmpty() || name.isEmpty())
        {
            Toast.makeText(getActivity(), "Vul je E-mail en Naam in alsjeblieft.", Toast.LENGTH_SHORT).show();
            return;
        }

        // testen
        if (false) {
            ((MainActivity) getActivity()).userId = "1231456789";

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.PREF_USER_ID), "123456789");
            editor.commit();

            boolean registered = !((MainActivity) getActivity()).userId.isEmpty();
            ConstraintLayout registerLayout = (ConstraintLayout) getView().findViewById(R.id.clRegister);
            ConstraintLayout registerDoneLayout = (ConstraintLayout) getView().findViewById(R.id.clRegisterDone);

            registerLayout.setVisibility(registered ? View.GONE : View.VISIBLE);
            registerDoneLayout.setVisibility(registered ? View.VISIBLE : View.GONE);

            return;
        }

        try
        {
            String url = "https://opendagen.frisovdpoort.nl/api/openday";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", name);
            jsonBody.put("email", email);
            jsonBody.put("education", "vmbo4");
            jsonBody.put("phone_number", "0653298109");

            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response)
                        {
                            try
                            {
                                String userId = response.getString("id");
                                ((MainActivity) getActivity()).userId = userId;

                                // Bewaar de userId
                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(getString(R.string.PREF_USER_ID), userId);
                                editor.commit();

                                ConstraintLayout registerLayout = (ConstraintLayout) getView().findViewById(R.id.clRegister);
                                ConstraintLayout registerDoneLayout = (ConstraintLayout) getView().findViewById(R.id.clRegisterDone);

                                registerLayout.setVisibility(View.GONE);
                                registerDoneLayout.setVisibility(View.VISIBLE);
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Toast.makeText(getActivity(), "Er is helaas iets fout gegegaan. Probeer het later nog een keer.", Toast.LENGTH_SHORT).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsObjRequest);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

}
