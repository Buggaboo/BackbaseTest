package nl.stimsim.mobile.backbase.model;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

/**
 * Created by jasmsison on 27/03/2018.
 */

public class DataReader {

    public void fromJsonReader(final CoordinateTrie root, final JsonReader reader) throws IOException {
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
                        String coordName = reader.nextName();
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
            root.buildTrie(country, locationName, lon, lat);
        }
        reader.endArray();
    }
}
