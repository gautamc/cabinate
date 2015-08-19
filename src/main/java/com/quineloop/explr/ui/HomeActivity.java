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
import android.widget.Toast;
import android.util.DisplayMetrics;
import android.content.res.Configuration;

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
                    if( entry.canRead() ) {
                        if ( entry.isDirectory() ) {
                            dir_listing.changeEntries(
                                entry,
                                FileSystem.list(entry, listing_options)
                            );
                            updateUpAction();
                        }
                    } else {
                        Toast.makeText(
                            parent.getContext(),
                            "Selection is not readable.",
                            Toast.LENGTH_SHORT
                        ).show();
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
            case R.id.show_hidden:
                if ( (this.listing_options & FileSystem.SHOW_HIDDEN) == FileSystem.SHOW_HIDDEN ) {
                    this.listing_options &= (~FileSystem.SHOW_HIDDEN);
                } else {
                    this.listing_options |= FileSystem.SHOW_HIDDEN;
                }
                sortListing();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            System.err.println( "landscape - " + metrics.toString() );
            Toast.makeText(this, "landscape - " + metrics.toString(), Toast.LENGTH_LONG).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            System.err.println( "landscape - " + metrics.toString() );
            Toast.makeText(this, "portrait - " + metrics.toString(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(
                    this,
                    "Touch back once more to exit.",
                    Toast.LENGTH_SHORT
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
