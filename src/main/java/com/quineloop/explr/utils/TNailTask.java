package com.quineloop.explr.utils;

import java.io.File;
import android.webkit.MimeTypeMap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import com.quineloop.explr.widget.adapter.DirListingAdapter;

public class TNailTask extends AsyncTask<File, Integer, Integer> {

    private DirListingAdapter dir_listing;

    public TNailTask(DirListingAdapter dir_listing) {
        this.dir_listing = dir_listing;
    }

    protected Integer doInBackground(File... image_files) {
        for( int i = 0; i < image_files.length; i++ ){
            System.err.println( " ****************** " + image_files[i].getAbsolutePath() + " ************ " );
            publishProgress(i+1);
            if( isCancelled() ) {
                break;
            }
        }
        return image_files.length;
    }

    protected void onProgessUpdate(Integer... progress) {
        dir_listing.setThumbnailFor(progress[0]);
    }

    protected void onPostExecute(Integer result) {
        System.err.println(" ************ THUMBNAIL PROCESS COMPLETE ************* " );
    }
}
