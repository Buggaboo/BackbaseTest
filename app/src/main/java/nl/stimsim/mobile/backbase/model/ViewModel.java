package nl.stimsim.mobile.backbase.model;

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

    /**
     *
     * Convenience method to build the tree
     *
     * @param trie
     * @param reader
     * @throws IOException
     */
    public void setTrie(final CoordinateTrie trie, final JsonReader reader) throws IOException {
        this.root = trie;
        dataReader.fromJsonReader(root, reader);
    }

    public void onSelectedCoordinates(CoordinateTrie node) {
        notifyObservers(node);
    }

    private ViewModel() {
    }
}
