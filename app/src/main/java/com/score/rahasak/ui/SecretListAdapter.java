package com.score.rahasak.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.score.rahasak.R;
import com.score.rahasak.pojo.Owl;

import java.util.ArrayList;

class SecretListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Owl> owlList;
    private Typeface typeface;

    SecretListAdapter(Context context, ArrayList<Owl> owlList) {
        this.context = context;
        this.owlList = owlList;

        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/GeosansLight.ttf");
    }

    @Override
    public int getCount() {
        return owlList.size();
    }

    @Override
    public Object getItem(int position) {
        return owlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Create list row view
     *
     * @param i         index
     * @param view      current list item view
     * @param viewGroup parent
     * @return view
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        final Owl owl = (Owl) getItem(i);

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.secret_list_row_layout, viewGroup, false);

            holder = new ViewHolder();
            holder.message = (TextView) view.findViewById(R.id.message);
            holder.sender = (TextView) view.findViewById(R.id.sender);
            holder.sentTime = (TextView) view.findViewById(R.id.sent_time);
            holder.userImage = (ImageView) view.findViewById(R.id.user_image);
            holder.selected = (ImageView) view.findViewById(R.id.selected);
            holder.unreadCount = (FrameLayout) view.findViewById(R.id.unread_msg_count);
            holder.unreadText = (TextView) view.findViewById(R.id.unread_msg_text);

            holder.sender.setTypeface(typeface, Typeface.NORMAL);
            holder.message.setTypeface(typeface, Typeface.NORMAL);
            holder.sentTime.setTypeface(typeface, Typeface.NORMAL);
            holder.unreadText.setTypeface(typeface, Typeface.BOLD);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setUpRow(owl, holder);
        return view;
    }

    private void setUpRow(Owl owl, ViewHolder viewHolder) {
        // set username/name
        viewHolder.sender.setText(owl.getFrom() + " to " + owl.getTo());
        viewHolder.message.setText(owl.getDesc());
        viewHolder.sentTime.setText(owl.getDate());
        viewHolder.userImage.setImageResource(R.drawable.default_user);
        viewHolder.selected.setVisibility(View.GONE);
    }

    /**
     * Keep reference to children view to avoid unnecessary calls
     */
    private static class ViewHolder {
        TextView message;
        TextView sender;
        TextView sentTime;
        ImageView userImage;
        ImageView selected;
        FrameLayout unreadCount;
        TextView unreadText;
    }
}
