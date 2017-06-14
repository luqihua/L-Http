package lu.okhttpdemo.util;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Author: luqihua
 * Time: 2017/6/6
 * Description: ApkDownloadUtil
 */

public class ApkDownloadUtil {

    private DownloadManager downloadManager;
    private long downloadId;
    private Disposable disposable;

    public void down(Context context, String url) {
        if (downloadManager == null) {
            downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        //设置Notification的标题和描述
//        request.setTitle("标题");
//        request.setDescription("描述");
        //设置Notification的显示，和隐藏。
//        request.setNotificationVisibility(visibility);
//        request.setMimeType("application/vnd.android.package-archive");

        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "dingding.apk");

        request.setDestinationUri(Uri.fromFile(file));

        downloadId = downloadManager.enqueue(request);

        disposable = Flowable.interval(100, 200, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        int progress = getProgress();
                        Log.d("ApkDownActivity", "getProgress():" + progress);
                        if (progress == 100) {
                            disposable.dispose();
                            Log.d("ApkDownActivity", "下载完成");
                        }
                    }
                });
    }

    public int getProgress() {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        int progress = 0;
        try {
            cursor = downloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //下载的文件到本地的目录
                int downloadFar = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                long total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                progress = (int) (downloadFar * 100d / total);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return progress;
    }

    public void destroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
