package com.score.rahasak.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.score.rahasak.R;
import com.score.rahasak.pojo.Owl;

import java.util.ArrayList;

/**
 * Activity which responsible to display Contact list
 *
 * @author eranga herath(erangaeb@gmail.com)
 */
public class OwlListActivity extends AppCompatActivity {

    private ListView expenseListView;
    private OwlListAdapter contactListAdapter;
    private ArrayList<Owl> owlList;
    private FloatingActionButton newButton;
    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owl_list_layout);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/GeosansLight.ttf");

        initToolbar();
        initActionBar();
        initList();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.add_user_header, null));
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        // title
        TextView titleText = (TextView) findViewById(R.id.title);
        titleText.setTypeface(typeface, Typeface.BOLD);
        titleText.setText("Owls");

        // back button
        ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        toolbar.setOverScrollMode(Toolbar.OVER_SCROLL_NEVER);
        setSupportActionBar(toolbar);
    }

    private void initList() {
        expenseListView = (ListView) findViewById(R.id.list);
        expenseListView.setTextFilterEnabled(false);

        owlList = new ArrayList<>();
        owlList.add(new Owl("eranga", "Colombo", "Kandy", "12/12/2017", "Coming from singapore"));
        owlList.add(new Owl("kasun", "Colombo", "Kandy", "12/12/2017", "Can take liquere from airport"));
        owlList.add(new Owl("lakmal", "Colombo", "Kandy", "12/12/2017", "Coming from sweden"));

        contactListAdapter = new OwlListAdapter(this, owlList);
        expenseListView.setAdapter(contactListAdapter);

        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // todo[wait till learning db and encryption] go to view contact activity
            }
        });
    }

}
