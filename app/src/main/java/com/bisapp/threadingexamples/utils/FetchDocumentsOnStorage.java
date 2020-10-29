package com.bisapp.threadingexamples.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FetchDocumentsOnStorage {
    private WeakReference<Context> mContext;

    public FetchDocumentsOnStorage(WeakReference<Context> mContext) {
        this.mContext = mContext;
    }

    public List<FileData> listFiles() {
        Context context = mContext.get();
        //final String selection = MediaStore.Audio.Media.IS_MUSIC +" != 0";
        final String[] projection = {MediaStore.Images.Media.DATA};

        List<FileData> fileDataList = new ArrayList<>();
        try (Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , projection, null, null, null)) {

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                    File file = new File(path);
                    if (!file.isDirectory()) {
                        FileData fileData = new FileData(file.getName(), "" + file.length());
                        fileDataList.add(fileData);
                    }
                    cursor.moveToNext();
                }
            }

        }

        return fileDataList;
    }
}
