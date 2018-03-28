package nl.stimsim.mobile.backbase.model;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

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
     * Since searching in the tree, is a series
     * of 'reads'. Unless a new thread is granted
     * access to the list, there are no deadlocks.
     * And the app can immediately get values.
     *
     * @param trie
     * @param reader
     * @throws IOException
     */
    public void setTrie(final CoordinateTrie trie, final JsonReader reader) throws IOException {
        this.root = trie;
        dataReader.fromJsonReader(root, reader);
    }

    /**
     *
     * The EditText accesses this,
     * we use the ViewModel for loose coupling, less messy code.
     *
     * @param input
     */
    public void onFilter(final String input) {
        if (input.length() == 0) {
            list.clear();
            setChanged();
            notifyObservers(list);
            clearChanged();
        }else {
            list.clear();
            root.searchTree(input, list); // Heavy operation? Dedicated thread? It seems overkill.
            setChanged();
            notifyObservers(list);
            clearChanged();
        }
    }

    /**
     *
     * When a selection is made from the list,
     * this passes the selected trie leaf,
     * to anyone who's listening.
     * Also, loosely coupled.
     *
     * Normally, I'd use Rx or LiveData.
     * An Observable suffices.
     *
     * @param leaf
     */
    public void onSelected(CoordinateTrie leaf) {
        setChanged();
        notifyObservers(leaf);
        clearChanged();
    }

    private ViewModel() {
    }
}
