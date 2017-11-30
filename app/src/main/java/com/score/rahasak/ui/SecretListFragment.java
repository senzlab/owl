package com.score.rahasak.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.score.rahasak.R;
import com.score.rahasak.application.IntentProvider;
import com.score.rahasak.db.SenzorsDbSource;
import com.score.rahasak.enums.IntentType;
import com.score.rahasak.interfaces.IFragmentTransitionListener;
import com.score.rahasak.pojo.Owl;
import com.score.senzc.enums.SenzTypeEnum;
import com.score.senzc.pojos.Senz;

import java.util.ArrayList;


public class SecretListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = SecretListFragment.class.getName();

    private IFragmentTransitionListener listener;

    private Typeface typeface;
    private ActionBar actionBar;
    private ImageView actionBarDelete;
    private FloatingActionButton newButton;

    private ArrayList<Owl> owlList;
    private SecretListAdapter adapter;
    private SenzorsDbSource dbSource;

    private BroadcastReceiver senzReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Got new user from Senz service");

            if (intent.hasExtra("SENZ")) {
                Senz senz = intent.getExtras().getParcelable("SENZ");
                if (senz.getSenzType() == SenzTypeEnum.DATA) {
                    refreshList();
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.secret_list_fragment_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dbSource = new SenzorsDbSource(getContext());
        initUi();
        initActionBar();
        displayList();
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IFragmentTransitionListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();

        getActivity().registerReceiver(senzReceiver, IntentProvider.getIntentFilter(IntentType.SENZ));
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(senzReceiver);
    }

    private void initUi() {
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GeosansLight.ttf");
        ((TextView) getActivity().findViewById(R.id.empty_view_chat)).setTypeface(typeface);

        // new
        newButton = (FloatingActionButton) getActivity().findViewById(R.id.new_done);
        if (new SenzorsDbSource(getActivity()).isAvailableUsers())
            newButton.setVisibility(View.GONE);
        else
            newButton.setVisibility(View.VISIBLE);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!new SenzorsDbSource(getActivity()).isAvailableUsers()) {
                    // move to invite
                    listener.onTransition("invite");
                }
            }
        });
    }

    private void initActionBar() {
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBarDelete = (ImageView) actionBar.getCustomView().findViewById(R.id.done);
        actionBarDelete.setVisibility(View.GONE);
    }

    private void displayList() {
        owlList = dbSource.getOwlList();
        adapter = new SecretListAdapter(getContext(), owlList);
        getListView().setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void refreshList() {
        owlList.clear();
        owlList.addAll(dbSource.getOwlList());
        adapter.notifyDataSetChanged();
    }

    private void moveToFriends() {

    }

    private void moveToInvite() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Owl owl = owlList.get(position);

        Intent intent = new Intent(this.getActivity(), OwlListActivity.class);
        intent.putExtra("OWL", owl);
        startActivity(intent);
    }

}
