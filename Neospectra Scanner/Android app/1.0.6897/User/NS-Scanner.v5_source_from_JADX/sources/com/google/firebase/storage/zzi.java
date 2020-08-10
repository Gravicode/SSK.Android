package com.google.firebase.storage;

import android.util.Log;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.StreamDownloadTask.StreamProcessor;
import com.google.firebase.storage.StreamDownloadTask.TaskSnapshot;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

final class zzi implements StreamProcessor {
    private /* synthetic */ TaskCompletionSource zzojh;
    private /* synthetic */ long zzoji;

    zzi(StorageReference storageReference, long j, TaskCompletionSource taskCompletionSource) {
        this.zzoji = j;
        this.zzojh = taskCompletionSource;
    }

    public final void doInBackground(TaskSnapshot taskSnapshot, InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[16384];
            int i = 0;
            while (true) {
                int read = inputStream.read(bArr, 0, 16384);
                if (read != -1) {
                    i += read;
                    if (((long) i) > this.zzoji) {
                        Log.e("StorageReference", "the maximum allowed buffer size was exceeded.");
                        throw new IndexOutOfBoundsException("the maximum allowed buffer size was exceeded.");
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                } else {
                    byteArrayOutputStream.flush();
                    this.zzojh.setResult(byteArrayOutputStream.toByteArray());
                    return;
                }
            }
        } finally {
            inputStream.close();
        }
    }
}
