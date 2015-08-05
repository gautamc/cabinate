package com.quineloop.explr.widget.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;

import com.quineloop.explr.R;
import com.quineloop.explr.Config;

public class DirListingAdapter extends BaseAdapter {
    private Context my_context;
    private File[] entries;
    private File cwd;

    public DirListingAdapter(Context ctx, File[] entries) {
        my_context = ctx;
        this.entries = entries;
    }

    public void changeEntries(File cwd, File[] entries){
        this.entries = entries;
        this.cwd = cwd;
        this.notifyDataSetChanged();
    }

    public File getParent() {
        if( this.cwd != null ) {
            File parent_wd = this.cwd.getParentFile();
            return (parent_wd != null ? parent_wd : null);
        } else {
            return null;
        }
    }

    public int getCount() {
        return entries.length;
    }

    public Object getItem(int position) {
        return entries[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View existing_view, ViewGroup parent) {
        TextView text_view;
        File entry = entries[position];
        if( existing_view == null ) {
            text_view = new TextView(my_context);
            text_view.setLayoutParams(new GridView.LayoutParams(58, 92));
            text_view.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
            text_view.setMaxLines(2);
            text_view.setEllipsize(android.text.TextUtils.TruncateAt.END);
        } else {
            text_view = (TextView) existing_view;
        }
        text_view.setCompoundDrawablesWithIntrinsicBounds(
            0,
            entry.isDirectory() ? R.drawable.folder : R.drawable.file,
            0,
            0
        );
        text_view.setText(entry.getName());
        return text_view;
    }
}
