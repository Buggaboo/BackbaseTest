package nl.stimsim.mobile.backbase;

import java.util.Map;
import java.util.Observable;

/**
 * Created by jasmsison on 26/03/2018.
 */

public class ViewModel extends Observable {
    private static final ViewModel ourInstance = new ViewModel();
    private CoordinateTrie root;

    public static ViewModel getInstance() {
        return ourInstance;
    }

    public void setTrie(CoordinateTrie trie) {
        this.root = trie;
    }

    private ViewModel() {
    }
}
