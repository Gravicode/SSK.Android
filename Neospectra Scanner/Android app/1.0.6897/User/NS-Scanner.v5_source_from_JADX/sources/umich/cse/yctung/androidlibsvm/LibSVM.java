package umich.cse.yctung.androidlibsvm;

import android.util.Log;

public class LibSVM {
    private static LibSVM svm;
    String LOG_TAG = "LibSVM";

    private native void jniSvmPredict(String str);

    private native void jniSvmScale(String str, String str2);

    private native void jniSvmTrain(String str);

    private native void testLog(String str);

    static {
        System.loadLibrary("jnilibsvm");
    }

    public void train(String cmd) {
        jniSvmTrain(cmd);
    }

    public void predict(String cmd) {
        jniSvmPredict(cmd);
    }

    public void scale(String cmd, String fileOutPath) {
        jniSvmScale(cmd, fileOutPath);
    }

    public static LibSVM getInstance() {
        if (svm == null) {
            svm = new LibSVM();
        }
        return svm;
    }

    public LibSVM() {
        Log.d(this.LOG_TAG, "LibSVM init");
    }
}
