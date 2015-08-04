package com.quineloop.explr.ui;

import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.quineloop.explr.R;
import com.quineloop.explr.Config;
import com.quineloop.explr.utils.FileSystem;

import java.io.File;

public class HomeActivity extends Activity {
    
    private DirListingAdapter dir_listing;
    private int count_back_click_for_exit = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActionBar action_bar = getActionBar();
        action_bar.setDisplayHomeAsUpEnabled(true);

        dir_listing = new DirListingAdapter(this, FileSystem.list() );

        GridView grid_view = (GridView) findViewById(R.id.gridview);
        grid_view.setAdapter(dir_listing);
        grid_view.setOnItemClickListener(
            new GridView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
                    File entry = (File) dir_listing.getItem(position);
                    if ( entry.isDirectory() ) {
                        dir_listing.changeEntries(entry, FileSystem.list(entry));
                    }
                }
            }
        );
    }

    public void onBackPressed() {
        File parent_dir = dir_listing.getParent();
        if( parent_dir != null ) {
            this.count_back_click_for_exit = 0;
            dir_listing.changeEntries(
                parent_dir, FileSystem.list(parent_dir)
            );
        } else {
            if( this.count_back_click_for_exit > 0 ) {
                finish();
            } else {
                dir_listing.changeEntries(
                    null, FileSystem.list()
                );
                this.count_back_click_for_exit++;
                android.widget.Toast.makeText(
                    this,
                    "Touch back once more to exit.",
                    android.widget.Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    public boolean onNavigateUp() {
        File parent_dir = dir_listing.getParent();
        if( parent_dir != null ) {
            dir_listing.changeEntries(
                parent_dir, FileSystem.list(parent_dir)
            );
        } else {
            dir_listing.changeEntries(
                null, FileSystem.list()
            );
        }
        return false;
    }

    private class DirListingAdapter extends BaseAdapter {
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

}
