package com.score.rahasak.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.score.rahasak.R;
import com.score.rahasak.utils.ActivityUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewOwlActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = NewOwlActivity.class.getName();

    // ui controls
    private EditText userEditText;
    private EditText amountEditText;
    private EditText dateEditText;
    private EditText offerEditText;
    private EditText descEditText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_owl_activity_layout);

        initPrefs();
        initUi();
        initToolbar();
        initActionBar();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "Bind to senz service");
        bindToService();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // unbind from service
        if (isServiceBound) {
            Log.d(TAG, "Unbind to senz service");
            unbindService(senzServiceConnection);

            isServiceBound = false;
        }
    }

    private void initPrefs() {
    }

    private void initUi() {
        userEditText = (EditText) findViewById(R.id.new_cheque_username);
        amountEditText = (EditText) findViewById(R.id.new_cheque_amount);
        dateEditText = (EditText) findViewById(R.id.new_cheque_date);
        offerEditText = (EditText) findViewById(R.id.new_cheque_offer);
        descEditText = (EditText) findViewById(R.id.desc);

        userEditText.setTypeface(typeface, Typeface.BOLD);
        amountEditText.setTypeface(typeface, Typeface.BOLD);
        dateEditText.setTypeface(typeface, Typeface.BOLD);
        offerEditText.setTypeface(typeface, Typeface.BOLD);
        descEditText.setTypeface(typeface, Typeface.BOLD);

        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // show date picker
                    onFocusDate();
                }
            }
        });
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFocusDate();
            }
        });

        sendButton = (Button) findViewById(R.id.new_cheque_send);
        sendButton.setTypeface(typeface, Typeface.BOLD);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.hideSoftKeyboard(NewOwlActivity.this);
                onClickPreview();
            }
        });
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.new_cheque_header, null));
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        // title
        TextView titleText = (TextView) findViewById(R.id.title);
        titleText.setTypeface(typeface, Typeface.BOLD);

        // back button
        ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // done button
        ImageView doneBtn = (ImageView) findViewById(R.id.done);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.hideSoftKeyboard(NewOwlActivity.this);
                onClickPreview();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        toolbar.setOverScrollMode(Toolbar.OVER_SCROLL_NEVER);
        setSupportActionBar(toolbar);
    }

    private void onFocusDate() {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    private void onClickPreview() {
        String amount = amountEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        if (amount.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Empty fields", Toast.LENGTH_LONG).show();
        } else {
            //ActivityUtils.showProgressDialog(this, "Generating cheque...");
            Intent intent = new Intent(this, OwlListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String selectedDate = sdf.format(cal.getTime());
        dateEditText.setText(selectedDate);
    }
}
