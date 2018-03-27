package nl.stimsim.mobile.backbase.model;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import android.os.Handler;

/**
 * Created by jasmsison on 26/03/2018.
 */

public class ViewModel extends Observable {
    private static final ViewModel ourInstance = new ViewModel();
    final private DataReader dataReader = new DataReader();
    final private List<CoordinateTrie> list = new ArrayList<>();
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

    public void onFilter(final String input) {
        list.clear();
        root.searchTree(input, list); // heavy operation?
        setChanged();
        notifyObservers(list);
        clearChanged();
    }

    public void onSelectedCoordinates(CoordinateTrie node) {
        setChanged();
        notifyObservers(node);
        clearChanged();
    }

    private ViewModel() {
    }
}
