package com.quineloop.explr.ui;

import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
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
    private int listing_options = 0;
    private ActionBar action_bar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        action_bar = getActionBar();

        listing_options = FileSystem.SORT_BY_NAME;
        dir_listing = new DirListingAdapter(
            this,
            FileSystem.list(listing_options)
        );

        GridView grid_view = (GridView) findViewById(R.id.gridview);
        grid_view.setAdapter(dir_listing);
        grid_view.setOnItemClickListener(
            new GridView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
                    File entry = (File) dir_listing.getItem(position);
                    if ( entry.isDirectory() ) {
                        dir_listing.changeEntries(
                            entry,
                            FileSystem.list(entry, listing_options)
                        );
                        updateUpAction();
                    }
                }
            }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch( item.getItemId() ) {
            case R.id.sort_mtime:
                this.listing_options = FileSystem.SORT_BY_MTIME;
                sortListing();
                return true;
            case R.id.sort_name:
                this.listing_options = FileSystem.SORT_BY_NAME;
                sortListing();
                return true;
            case R.id.sort_size:
                this.listing_options = FileSystem.SORT_BY_SIZE;
                sortListing();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        File parent_dir = dir_listing.getParent();
        if( parent_dir != null ) {
            this.count_back_click_for_exit = 0;
            dir_listing.changeEntries(
                parent_dir, FileSystem.list(parent_dir, this.listing_options)
            );
        } else {
            if( this.count_back_click_for_exit > 0 ) {
                finish();
            } else {
                dir_listing.changeEntries(
                    null, FileSystem.list(this.listing_options)
                );
                this.count_back_click_for_exit++;
                android.widget.Toast.makeText(
                    this,
                    "Touch back once more to exit.",
                    android.widget.Toast.LENGTH_SHORT
                ).show();
            }
        }
        updateUpAction();
    }

    public boolean onNavigateUp() {
        File parent_dir = dir_listing.getParent();
        if( parent_dir != null ) {
            dir_listing.changeEntries(
                parent_dir, FileSystem.list(parent_dir, listing_options)
            );
        } else {
            dir_listing.changeEntries(
                null, FileSystem.list(listing_options)
            );
        }
        updateUpAction();
        return false;
    }

    private void updateUpAction() {
        File parent_dir = dir_listing.getParent();
        if( parent_dir != null ) {
            action_bar.setDisplayHomeAsUpEnabled(true);
        } else {
            action_bar.setDisplayHomeAsUpEnabled(false);
        }
    }

    private void sortListing() {
        File cwd = dir_listing.getCWD();
        if( cwd != null ) {
            dir_listing.changeEntries(
                cwd, FileSystem.list(cwd, this.listing_options)
            );
        } else {
            dir_listing.changeEntries(
                null, FileSystem.list(this.listing_options)
            );
        }
    }
}
