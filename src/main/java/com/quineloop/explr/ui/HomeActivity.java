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
    private ActionBar action_bar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        action_bar = getActionBar();

        dir_listing = new DirListingAdapter(
            this,
            FileSystem.list(FileSystem.SORT_BY_NAME)
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
                            FileSystem.list(entry, FileSystem.SORT_BY_NAME)
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
        File cwd = dir_listing.getCWD();
        switch( item.getItemId() ) {
            case R.id.sort_mtime:
                if( cwd != null ) {
                    dir_listing.changeEntries(
                        cwd, FileSystem.list(cwd, FileSystem.SORT_BY_MTIME)
                    );
                } else {
                    dir_listing.changeEntries(
                        null, FileSystem.list(FileSystem.SORT_BY_MTIME)
                    );
                }
                return true;
            case R.id.sort_name:
                if( cwd != null ) {
                    dir_listing.changeEntries(
                        cwd, FileSystem.list(cwd, FileSystem.SORT_BY_NAME)
                    );
                } else {
                    dir_listing.changeEntries(
                        null, FileSystem.list(FileSystem.SORT_BY_NAME)
                    );
                }
                return true;
            case R.id.sort_size:
                if( cwd != null ) {
                    dir_listing.changeEntries(
                        cwd, FileSystem.list(cwd, FileSystem.SORT_BY_SIZE)
                    );
                } else {
                    dir_listing.changeEntries(
                        null, FileSystem.list(FileSystem.SORT_BY_SIZE)
                    );
                }
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
        updateUpAction();
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
}
