package nl.stimsim.mobile.backbase;

import android.app.Application;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jasmsison on 27/03/2018.
 */

public class App extends Application {
    private CoordinateTrie root;

    @Override
    public void onCreate() {
        super.onCreate();

        // KISS, no AsyncTask, if the app is killed before this completes, too bad
        // the resources will be cleaned up in the 'finally' block
        root = new CoordinateTrie();
        new Thread(new Runnable(){
            public void run() {
                Resources resources = App.this.getApplicationContext().getResources();
                InputStream is = null;
                InputStreamReader inputStreamReader = null;
                JsonReader reader = null;
                try {
                    is = resources.openRawResource(R.raw.cities);
                    inputStreamReader = new InputStreamReader(is, "UTF-8");
                    reader = new JsonReader(inputStreamReader);
                    ViewModel.getInstance().setTrie(root, reader);
                } catch (final IOException e) {
                    // for lack of runOnUiThread
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Log.getStackTraceString(e);
                        }
                    });
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (inputStreamReader != null)
                            inputStreamReader.close();
                        if (reader != null)
                            reader.close();
                    } catch (IOException e) {
                        // ...
                    }
                }
            }
        }).start();
    }
}
