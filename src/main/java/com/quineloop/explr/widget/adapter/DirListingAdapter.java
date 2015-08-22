package com.quineloop.explr.widget.adapter;

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

import java.io.File;

import com.quineloop.explr.R;
import com.quineloop.explr.Config;

public class DirListingAdapter extends BaseAdapter {
    private Context my_context;
    private File[] entries;
    private File cwd;
    private boolean preview_images;

    public DirListingAdapter(Context ctx, File[] entries, boolean preview_images) {
        my_context = ctx;
        this.entries = entries;
        this.preview_images = preview_images;
    }

    public DirListingAdapter(Context ctx, File[] entries) {
        my_context = ctx;
        this.entries = entries;
        this.preview_images = false;
    }

    public void changeEntries(File cwd, File[] entries, boolean preview_images){
        this.entries = entries;
        this.cwd = cwd;
        this.preview_images = preview_images;
        this.notifyDataSetChanged();
    }

    public void changeEntries(File cwd, File[] entries){
        this.entries = entries;
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
        } else {
            text_view = (TextView) existing_view;
        }

        if ( preview_images ) {
            // TODO: Move this to AsynTask
            String extn = MimeTypeMap.getFileExtensionFromUrl( entry.getAbsolutePath() );
            String mime_type = null;
            if( extn.length() > 0 ) {
                mime_type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extn);
            }
            System.err.println( " ----------------- " + entry.getAbsolutePath() + " : " + mime_type + " ----------------- " );
            if( mime_type != null && mime_type.equals("image/jpeg") ) {
                Bitmap source_bitmap = BitmapFactory.decodeFile( entry.getAbsolutePath() );
                Bitmap small_bitmap = ThumbnailUtils.extractThumbnail(source_bitmap, 58, 58);
                Drawable drawable_bitmap = new BitmapDrawable(my_context.getResources(), small_bitmap);
                drawable_bitmap.setBounds(
                    0,0,
                    drawable_bitmap.getIntrinsicWidth(),
                    drawable_bitmap.getIntrinsicHeight()
                );
                text_view.setCompoundDrawablesRelative(
                    null, drawable_bitmap, null, null
                );
            } else {
                setDrawableBasedOnType(text_view, entry);
            }
        } else {
            setDrawableBasedOnType(text_view, entry);
        }
        text_view.setText(entry.getName());
        return text_view;
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
