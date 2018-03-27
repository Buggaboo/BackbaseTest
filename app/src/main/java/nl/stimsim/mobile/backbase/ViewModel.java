package nl.stimsim.mobile.backbase;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.Observable;

/**
 * Created by jasmsison on 26/03/2018.
 */

public class ViewModel extends Observable {
    private static final ViewModel ourInstance = new ViewModel();
    final private DataReader dataReader = new DataReader();
    private CoordinateTrie root;

    public static ViewModel getInstance() {
        return ourInstance;
    }

    public void setTrie(final CoordinateTrie trie, final JsonReader reader) throws IOException {
        this.root = trie;
        dataReader.fromJsonReader(root, reader);
    }

    private ViewModel() {
    }
}
