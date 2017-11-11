package com.score.rahasak.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.score.rahasak.R;


public class HomeActivity extends BaseActivity {

    private RelativeLayout newDeliveryLayout;
    private RelativeLayout newTourLayout;
    private RelativeLayout deliveryOwlLayout;

    private TextView newDelivery;
    private TextView newTour;
    private TextView deliveryOwls;
    private TextView tourOwls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owl_home_layout);

        initUi();
    }

    private void initUi() {
        newDelivery = (TextView) findViewById(R.id.new_delivery);
        newTour = (TextView) findViewById(R.id.new_tour);
        deliveryOwls = (TextView) findViewById(R.id.deliver_owls);
        tourOwls = (TextView) findViewById(R.id.tour_owls);

        newDelivery.setTypeface(typeface, Typeface.BOLD);
        newTour.setTypeface(typeface, Typeface.BOLD);
        deliveryOwls.setTypeface(typeface, Typeface.BOLD);
        tourOwls.setTypeface(typeface, Typeface.BOLD);

        newDeliveryLayout = (RelativeLayout) findViewById(R.id.new_delivery_layout);
        newTourLayout = (RelativeLayout) findViewById(R.id.new_tour_layout);
        deliveryOwlLayout = (RelativeLayout) findViewById(R.id.deliver_owls_layout);

        newDeliveryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NewOwlActivity.class);
                startActivity(intent);
            }
        });

        newTourLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NewOwlActivity.class);
                startActivity(intent);
            }
        });

        deliveryOwlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, OwlListActivity.class);
                startActivity(intent);
            }
        });
    }
}

