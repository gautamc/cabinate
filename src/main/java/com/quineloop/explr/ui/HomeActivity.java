package com.quineloop.explr.ui;

import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.AdapterView;

import java.io.File;

import com.quineloop.explr.R;
import com.quineloop.explr.Config;
import com.quineloop.explr.utils.FileSystem;
import com.quineloop.explr.widget.adapter.DirListingAdapter;

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
}
