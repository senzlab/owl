package com.score.rahasak.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.score.rahasak.R;
import com.score.rahasak.pojo.Owl;

import java.util.ArrayList;

/**
 * Display expense list
 *
 * @author eranga herath(erangaeb@gmail.com)
 */
public class OwlListAdapter extends BaseAdapter {

    private OwlListActivity activity;
    private ArrayList<Owl> owlList;

    private Typeface typeface;

    /**
     * Initialize context variables
     *
     * @param activity friend list activity
     */
    public OwlListAdapter(OwlListActivity activity, ArrayList<Owl> owlList) {
        this.activity = activity;
        this.owlList = owlList;
        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/GeosansLight.ttf");
    }

    /**
     * Get size of expense list
     *
     * @return expenseList size
     */
    @Override
    public int getCount() {
        return owlList.size();
    }

    /**
     * Get specific item from expense list
     *
     * @param i item index
     * @return list item
     */
    @Override
    public Object getItem(int i) {
        return owlList.get(i);
    }

    /**
     * Get expense list item id
     *
     * @param i item index
     * @return current item id
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Create list row view
     *
     * @param position index
     * @param view     current list item view
     * @param parent   parent
     * @return view
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        final ViewHolder holder;
        final Owl owl = (Owl) getItem(position);

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.owl_list_row_layout, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.contact_list_row_layout_name);
            holder.date = (TextView) view.findViewById(R.id.contact_list_row_layout_date);
            holder.phone = (TextView) view.findViewById(R.id.contact_list_row_layout_phone);

            holder.name.setTypeface(typeface, Typeface.NORMAL);
            holder.date.setTypeface(typeface, Typeface.NORMAL);
            holder.phone.setTypeface(typeface, Typeface.NORMAL);
            // todo set custom font for phone

            view.setTag(holder);
        } else {
            // get view holder back
            holder = (ViewHolder) view.getTag();
        }

        // bind text with view holder content view for efficient use
        holder.name.setText(owl.getUsername());
        holder.date.setText(owl.getDate());
        holder.phone.setText(owl.getDesc());

        return view;
    }


    /**
     * Keep reference to children view to avoid unnecessary calls
     */
    private static class ViewHolder {
        TextView name;
        TextView date;
        TextView phone;
    }

}
