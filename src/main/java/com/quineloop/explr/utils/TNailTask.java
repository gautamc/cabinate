package com.quineloop.explr.utils;

import java.io.File;
import java.util.HashMap;
import android.content.Context;
import android.webkit.MimeTypeMap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import com.quineloop.explr.widget.adapter.DirListingAdapter;

public class TNailTask extends AsyncTask<File, Void, HashMap<Integer, BitmapDrawable>> {

    private DirListingAdapter dir_listing;
    private Context context;

    public TNailTask(DirListingAdapter dir_listing, Context context) {
        this.dir_listing = dir_listing;
        this.context = context;
    }

    protected HashMap doInBackground(File... image_files) {
        HashMap<Integer, BitmapDrawable> bitmaps = new HashMap<Integer, BitmapDrawable>();
        for( int i = 0; i < image_files.length; i++ ){
            System.err.println( " ******IN ASYNC TASK ************ " + image_files[i].getAbsolutePath() + " ************ " );
            String extn = MimeTypeMap.getFileExtensionFromUrl( image_files[i].getAbsolutePath() );
            String mime_type = null;
            if( extn.length() > 0 ) {
                mime_type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extn);
            }
            System.err.println( " ----------------- " + image_files[i].getAbsolutePath() + " : " + mime_type + " ----------------- " );
            if( mime_type != null && mime_type.equals("image/jpeg") ) {
                Bitmap source_bitmap = BitmapFactory.decodeFile( image_files[i].getAbsolutePath() );
                Bitmap small_bitmap = ThumbnailUtils.extractThumbnail(source_bitmap, 58, 58);
                BitmapDrawable drawable_bitmap = new BitmapDrawable(context.getResources(), small_bitmap);
                drawable_bitmap.setBounds(
                    0,0,
                    drawable_bitmap.getIntrinsicWidth(),
                    drawable_bitmap.getIntrinsicHeight()
                );
                bitmaps.put(Integer.valueOf(i), drawable_bitmap);
            }
            if( isCancelled() ) {
                break;
            }
        }
        return bitmaps;
    }

    protected void onPostExecute(HashMap<Integer, BitmapDrawable> bitmaps) {
        System.err.println(" ************ THUMBNAIL PROCESS COMPLETE ************* " );
        dir_listing.setThumbnailFor(bitmaps);
    }
}
