package com.score.rahasak.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.score.rahasak.R;
import com.score.rahasak.pojo.Owl;
import com.score.rahasak.utils.ActivityUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewOwlFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = NewOwlFragment.class.getName();

    protected Typeface typeface;

    // ui controls
    private EditText userEditText;
    private EditText amountEditText;
    private EditText dateEditText;
    private EditText offerEditText;
    private EditText descEditText;
    private Button sendButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_owl_activity_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initPrefs();
        initUi();
        initActionBar();
    }

    private void initPrefs() {
    }

    private void initUi() {
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GeosansLight.ttf");

        userEditText = (EditText) getActivity().findViewById(R.id.new_cheque_username);
        amountEditText = (EditText) getActivity().findViewById(R.id.new_cheque_amount);
        dateEditText = (EditText) getActivity().findViewById(R.id.new_cheque_date);
        offerEditText = (EditText) getActivity().findViewById(R.id.new_cheque_offer);
        descEditText = (EditText) getActivity().findViewById(R.id.desc);

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
    }

    private void initActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        // done button
        ImageView doneBtn = (ImageView) actionBar.getCustomView().findViewById(R.id.done);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.hideSoftKeyboard(getActivity());
                onClickPreview();
            }
        });
    }

    private void onFocusDate() {
        final Calendar cal = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(false);
        datePicker.setTitle("Select the date");
        datePicker.show();
    }

    private void onClickPreview() {
        String from = userEditText.getText().toString().trim();
        String to = amountEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String amount = amountEditText.getText().toString().trim();
        String desc = descEditText.getText().toString().trim();
        if (from.isEmpty() || to.isEmpty() || date.isEmpty()) {
            Toast.makeText(getActivity(), "Empty fields", Toast.LENGTH_LONG).show();
        } else {
            //ActivityUtils.showProgressDialog(this, "Generating cheque...");
            Intent intent = new Intent(getActivity(), OwlListActivity.class);
            intent.putExtra("OWL", new Owl("senz", from, to, date, desc));
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
