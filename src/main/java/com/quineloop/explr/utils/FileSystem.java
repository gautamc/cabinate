package com.quineloop.explr.utils;

import java.io.File;

public class FileSystem {

    private static final File[] NO_FILES = {};

    public static File[] list() {
        File[] all_roots = File.listRoots();
        if( all_roots.length > 0 ) {
            return (new File(all_roots[0].getAbsolutePath()).listFiles());
        }
        return NO_FILES;
    }

    public static File[] list(File dir) {
        return dir.listFiles();
    }

}
