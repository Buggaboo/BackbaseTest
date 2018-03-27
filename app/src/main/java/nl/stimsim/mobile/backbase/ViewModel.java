package nl.stimsim.mobile.backbase;

import java.util.Map;
import java.util.Observable;

/**
 * Created by jasmsison on 26/03/2018.
 */

public class ViewModel extends Observable {
    private static final ViewModel ourInstance = new ViewModel();
    private CoordinateTrie trie;

    public static ViewModel getInstance() {
        return ourInstance;
    }

    public void setTrie(CoordinateTrie trie) {
        this.trie = trie;
    }

    public void searchPattern(String s) {
        // TODO activate later
        // Map<String, Map.Entry> coordinates = trie.searchTree(s);
        // notifyObservers(coordinates);
    }

    private ViewModel() {
    }
}
