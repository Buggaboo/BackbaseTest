package nl.stimsim.mobile.backbase;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.RawRes;
import android.util.Log;

import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// import android.util.JsonReader; // This one isn't mocked of course, gotta use GSON

/**
 * Created by jasmsison on 27/03/2018.
 */

public class DataReader {

    // probably the worst performing implementation, it should work though, albeit slowly
    public void fromJSONArray(final CoordinateTrie root, final JSONArray array) {
        for(int i=0; i<array.length(); i++) {
            try {
                JSONObject o = (JSONObject) array.get(i);
                String country = o.getString("country"); // TODO group as Map<String, Set<CoordTrie>>
                String name = o.getString("name");
                JSONObject coord = o.getJSONObject("coord");
                double lon = coord.getDouble("lon");
                double lat = coord.getDouble("lat");

                root.buildTrie(name, lon, lat);
            } catch (JSONException e) {
                Log.getStackTraceString(e);
            }
        }
    }

    public void fromRawJsonReader(final CoordinateTrie root, Context context, @RawRes int rawResId) throws IOException {
        Resources resources = context.getResources();
        InputStream is = resources.openRawResource(rawResId);
        fromJsonReader(root, is);
    }

    public void fromJsonReader(final CoordinateTrie root, final InputStream is) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
        reader.beginArray();
        while (reader.hasNext()) {
            String country = null;
            String locationName = null;
            double lon = 0.0f;
            double lat = 0.0f;

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("country")) {
                    country = reader.nextString();
                } else if (name.equals("name")) {
                    locationName = reader.nextString();
                } else if (name.equals("coord")) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        String coordName = reader.nextName(); // TODO add this to the trie
                        if (coordName.equals("lon")) {
                            lon = reader.nextDouble();
                        } else if (coordName.equals("lat")) {
                            lat = reader.nextDouble();
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            root.buildTrie(locationName, lon, lat);
        }
        reader.endArray();
    }

    // TODO RegExp implementation... later, if time allows
}
