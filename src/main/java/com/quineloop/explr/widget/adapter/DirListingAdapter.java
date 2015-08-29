package com.quineloop.explr.widget.adapter;

import java.io.File;
import java.util.HashMap;
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
    private File cwd;
    private boolean preview_images;
    private HashMap<Integer, BitmapDrawable> drawable_bitmaps;

    public DirListingAdapter(Context ctx, File cwd, File[] entries, boolean preview_images) {
        my_context = ctx;
        this.entries = entries;
        this.cwd = cwd;
        this.drawable_bitmaps = null;
        this.preview_images = preview_images;
        if( this.preview_images ) {
            TNailTask thumbnailing_thread = new TNailTask(this, my_context);
            thumbnailing_thread.execute(entries);
        }
    }

    public DirListingAdapter(Context ctx, File cwd, File[] entries) {
        my_context = ctx;
        this.entries = entries;
        this.cwd = cwd;
        this.drawable_bitmaps = null;
        this.preview_images = false;
    }

    public void changeEntries(File cwd, File[] entries, boolean preview_images){
        this.entries = entries;
        this.cwd = cwd;
        this.drawable_bitmaps = null;
        this.preview_images = preview_images;
        this.notifyDataSetChanged();
        if( this.preview_images ){
            TNailTask thumbnailing_thread = new TNailTask(this, my_context);
            thumbnailing_thread.execute(entries);
        }
    }

    public void changeEntries(File cwd, File[] entries){
        this.entries = entries;
        this.cwd = cwd;
        this.drawable_bitmaps = null;
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
        } else {
            text_view = (TextView) existing_view;
        }
        setDrawableBasedOnType(text_view, entry);
        text_view.setText(entry.getName());
        return text_view;
    }

    public void setThumbnailFor(HashMap<Integer, BitmapDrawable> drawable_bitmaps){
        System.err.println( " **** IN setThumbnailFor() ********** " );
        this.drawable_bitmaps = drawable_bitmaps;
        System.err.println( drawable_bitmaps.size() );
        System.err.println( " **** AFTER setThumbnailFor() ******* " );
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
