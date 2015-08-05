package com.quineloop.explr.utils;

import java.io.File;
import java.util.Comparator;
import java.util.Arrays;

public class FileSystem {

    public static int SHOW_HIDDEN = 1;
    public static int SORT_BY_MTIME = 2;
    public static int SORT_BY_NAME = 4;
    public static int SORT_BY_SIZE = 8;
    public static int SORT_BY_TYPE = 16;
    private static final File[] NO_FILES = {};

    public static File[] list(int... optional_args) {
        int options = optional_args.length > 0 ? optional_args[0] : 0;
        File[] all_roots = File.listRoots();
        if( all_roots.length > 0 ) {
            // (options & SHOW_HIDDEN) == 0
            // (options & SORT_BY_MTIME) == 0
            // (options & SORT_BY_NAME) == 0
            File[] listing = new File(all_roots[0].getAbsolutePath()).listFiles();
            if( (options & SORT_BY_MTIME) == SORT_BY_MTIME ) {
                Arrays.sort(
                    listing, new Comparator<File>(){
                        public int compare(File f1, File f2){
                            return Long.compare(f2.lastModified(), f1.lastModified());
                        }
                    }
                );
            }
            return listing;
        }
        return NO_FILES;
    }

    public static File[] list(File dir, int... optional_args) {
        int options = optional_args.length > 0 ? optional_args[0] : 0;
        // (options & SHOW_HIDDEN) == 0
        // (options & SORT_BY_MTIME) == 0
        // (options & SORT_BY_NAME) == 0
        File[] listing = dir.listFiles();
        if( (options & SORT_BY_MTIME) == SORT_BY_MTIME ) {
            Arrays.sort(
                listing, new Comparator<File>(){
                    public int compare(File f1, File f2){
                        return Long.compare(f2.lastModified(), f1.lastModified());
                    }
                }
            );
        }
        return listing;
    }

}
