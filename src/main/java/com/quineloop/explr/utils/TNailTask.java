package com.quineloop.explr.utils;

import java.io.File;
import android.util.Pair;
import android.content.Context;
import android.webkit.MimeTypeMap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.net.Uri;
import com.quineloop.explr.widget.adapter.DirListingAdapter;

public class TNailTask extends AsyncTask<File, Pair<Integer, BitmapDrawable>, Void> {

    private DirListingAdapter dir_listing;
    private Context context;
    private BitmapFactory.Options bitmap_opts;

    public TNailTask(DirListingAdapter dir_listing, Context context) {
        super();
        this.dir_listing = dir_listing;
        this.context = context;
        bitmap_opts = new BitmapFactory.Options();
        bitmap_opts.inJustDecodeBounds = true;
    }

    protected Void doInBackground(File... files) {
        for( int i = 0; i < files.length; i++ ){
            if( files[i].isDirectory() || !files[i].canRead() ) {
                continue;
            }
            String extn = MimeTypeMap.getFileExtensionFromUrl( files[i].getAbsolutePath() );
            String mime_type = null;
            if( extn.length() > 0 ) {
                mime_type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extn);
            }
            if( mime_type == null ) {
                BitmapFactory.decodeFile(files[i].getAbsolutePath(), bitmap_opts);
                mime_type = bitmap_opts.outMimeType;
            }
            if( mime_type != null && mime_type.equals("image/jpeg") ) {
                Bitmap source_bitmap = BitmapFactory.decodeFile( files[i].getAbsolutePath() );
                Bitmap small_bitmap = ThumbnailUtils.extractThumbnail(source_bitmap, 56, 56);
                BitmapDrawable drawable_bitmap = new BitmapDrawable(context.getResources(), small_bitmap);
                drawable_bitmap.setBounds(
                    0,5,
                    drawable_bitmap.getIntrinsicWidth(),
                    drawable_bitmap.getIntrinsicHeight()
                );
                publishProgress(Pair.create(Integer.valueOf(i), drawable_bitmap));
            }
            if( isCancelled() ) {
                break;
            }
        }
        return null;
    }

    protected void onProgressUpdate(Pair<Integer, BitmapDrawable>... progress) {
        dir_listing.setThumbnailFor(progress[0]);
    }
}
