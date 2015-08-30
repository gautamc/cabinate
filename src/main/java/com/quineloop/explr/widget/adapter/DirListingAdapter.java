package com.quineloop.explr.widget.adapter;

import java.io.File;
import java.util.HashMap;
import android.util.Pair;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.webkit.MimeTypeMap;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import com.quineloop.explr.R;
import com.quineloop.explr.Config;
import com.quineloop.explr.utils.TNailTask;

public class DirListingAdapter extends BaseAdapter {
    private Context my_context;
    private File[] entries;
    private TextView[] text_views;
    private HashMap<Integer, BitmapDrawable> drawable_bitmaps;
    private File cwd;
    private boolean preview_images;

    public DirListingAdapter(Context ctx, File cwd, File[] entries, boolean preview_images) {
        my_context = ctx;
        this.entries = entries;
        this.text_views = new TextView[entries.length];
        this.drawable_bitmaps = new HashMap<Integer, BitmapDrawable>();
        this.cwd = cwd;
        this.preview_images = preview_images;
        if( this.preview_images ) {
            TNailTask thumbnailing_thread = new TNailTask(this, my_context);
            thumbnailing_thread.execute(entries);
        }
    }

    public DirListingAdapter(Context ctx, File cwd, File[] entries) {
        my_context = ctx;
        this.entries = entries;
        this.text_views = new TextView[entries.length];
        this.drawable_bitmaps = new HashMap<Integer, BitmapDrawable>();
        this.cwd = cwd;
        this.preview_images = false;
    }

    public void changeEntries(File cwd, File[] entries, boolean preview_images){
        this.entries = entries;
        this.text_views = new TextView[entries.length];
        this.drawable_bitmaps = new HashMap<Integer, BitmapDrawable>();
        this.cwd = cwd;
        this.preview_images = preview_images;
        this.notifyDataSetChanged();
        if( this.preview_images ){
            TNailTask thumbnailing_thread = new TNailTask(this, my_context);
            thumbnailing_thread.execute(entries);
        }
    }

    public void changeEntries(File cwd, File[] entries){
        this.entries = entries;
        this.text_views = new TextView[entries.length];
        this.drawable_bitmaps = new HashMap<Integer, BitmapDrawable>();
        this.cwd = cwd;
        this.preview_images = false;
        this.notifyDataSetChanged();
    }

    public File getCWD() {
        return this.cwd;
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
            text_views[position] = text_view;
        } else {
            text_view = (TextView) existing_view;
            text_views[Integer.valueOf(position)] = text_view;
        }

        if( drawable_bitmaps != null && drawable_bitmaps.containsKey( Integer.valueOf(position) ) ) {
            text_view.setCompoundDrawablesRelative(
                null, (BitmapDrawable) drawable_bitmaps.get(Integer.valueOf(position)), null, null
            );
            text_view.setCompoundDrawablePadding(5);
        } else {
            setDrawableBasedOnType(text_view, entry);
        }
        text_view.setText(entry.getName());
        return text_view;
    }

    public void setThumbnailFor(Pair<Integer, BitmapDrawable> pair) {
        if( text_views[pair.first] != null ) {
            text_views[pair.first].setCompoundDrawablesRelative(
                null, (BitmapDrawable) pair.second, null, null
            );
            text_views[pair.first].setCompoundDrawablePadding(5);
        }
        drawable_bitmaps.put(pair.first, pair.second);
    }

    private void setDrawableBasedOnType(TextView text_view, File entry) {
        text_view.setCompoundDrawablesWithIntrinsicBounds(
            0,
            entry.isDirectory() ? R.drawable.folder : R.drawable.file,
            0,
            0
        );
    }
}
