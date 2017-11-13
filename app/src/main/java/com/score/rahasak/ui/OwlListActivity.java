package com.score.rahasak.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.score.rahasak.R;
import com.score.rahasak.db.SenzorsDbSource;
import com.score.rahasak.enums.BlobType;
import com.score.rahasak.enums.DeliveryState;
import com.score.rahasak.pojo.Owl;
import com.score.rahasak.pojo.Secret;
import com.score.rahasak.pojo.SecretUser;
import com.score.rahasak.utils.SenzUtils;

import java.util.ArrayList;

/**
 * Activity which responsible to display Contact list
 *
 * @author eranga herath(erangaeb@gmail.com)
 */
public class OwlListActivity extends AppCompatActivity {

    private ListView expenseListView;
    private OwlListAdapter contactListAdapter;
    private Typeface typeface;

    private Owl owl;
    private ArrayList<Owl> owlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owl_list_layout);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/GeosansLight.ttf");

        initToolbar();
        initActionBar();
        initPrefs();
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

    private void initPrefs() {
        owl = getIntent().getParcelableExtra("OWL");
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
                onOwlClick(owlList.get(position));
            }
        });
    }

    private void onOwlClick(Owl clickedOwl) {
        // create user
        SecretUser secretUser = new SecretUser("id", clickedOwl.getUsername());
        SenzorsDbSource dbSource = new SenzorsDbSource(this);
        if (!dbSource.isExistingUser(secretUser.getUsername())) {
            dbSource.createSecretUser(secretUser);

            // my secrets
            Long t1 = System.currentTimeMillis();
            String myBlob = owl.getDesc() + "\n" +
                    "From: " + owl.getFrom() + "\n" +
                    "To: " + owl.getTo() + "\n" +
                    "Date: " + owl.getDate();
            Secret mySecret = new Secret(myBlob, BlobType.TEXT, secretUser, false);
            mySecret.setDeliveryState(DeliveryState.NONE);
            mySecret.setTimeStamp(t1);
            mySecret.setId(SenzUtils.getUid(this, t1.toString()));
            dbSource.createSecret(mySecret);

            // friend secrets
            Long t2 = System.currentTimeMillis();
            String friendBlob = clickedOwl.getDesc() + "\n" +
                    "From: " + clickedOwl.getFrom() + "\n" +
                    "To: " + clickedOwl.getTo() + "\n" +
                    "Date: " + clickedOwl.getDate();
            Secret friendSecret = new Secret(friendBlob, BlobType.TEXT, secretUser, true);
            friendSecret.setDeliveryState(DeliveryState.NONE);
            friendSecret.setTimeStamp(t2);
            friendSecret.setId(SenzUtils.getUid(this, t2.toString()));
            dbSource.createSecret(friendSecret);
        }

        Intent intent = new Intent(OwlListActivity.this, ChatActivity.class);
        intent.putExtra("SENDER", clickedOwl.getUsername());
        startActivity(intent);
    }

}
