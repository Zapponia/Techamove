package com.techamove.Utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressedRequestBody extends RequestBody {
    private static final String TAG = "ProgressedRequestBody";

    private File mFile;
    private String mPath;
    private UploadCallbacks mListener;
    private String content_type;

    private int fileCount = 1;

    private static final int DEFAULT_BUFFER_SIZE = 2048;

    public ProgressedRequestBody(final File file, String content_type, final UploadCallbacks listener) {
        this.content_type = content_type;
        mFile = file;
        mListener = listener;
    }


    @Override
    public MediaType contentType() {
        return MediaType.parse(content_type);
    }

    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = mFile.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {

                // update progress on UI thread
                handler.post(new ProgressUpdater(uploaded, fileLength));

                uploaded += read;
                sink.write(buffer, 0, read);
            }
        } finally {
            in.close();
            /*if (fileCount != fileCount) {

            }*/
            fileCount++;
            if (mListener != null) {
                mListener.fileCount(fileCount);
            }

        }
        Log.d(TAG, "writeTo: " + fileCount);
    }

    public interface UploadCallbacks {
        void onProgressUpdate(int percentage);

        void onError();

        void onFinish();

        void fileCount(int count);
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;
        private int fileCount;

        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            mListener.onProgressUpdate((int) (100 * mUploaded / mTotal));
        }
    }

}
