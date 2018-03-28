package nl.stimsim.mobile.backbase;

import com.google.gson.stream.JsonReader;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static org.mockito.Mockito.*;

import nl.stimsim.mobile.backbase.model.CoordinateTrie;
import nl.stimsim.mobile.backbase.model.ViewModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Local unit test for the Observer/Observable implementation
 */
public class ObservableTest {
    final static float dud = 1.0f;

    class SelectionObserver implements Observer {

        public static final String SYDNEY = "Sydney";

        @Override
        public void update(Observable observable, Object o) {
            if (!(o instanceof CoordinateTrie)) return;
            CoordinateTrie node = (CoordinateTrie) o;
            assertEquals(node.originalName, SYDNEY);
        }
    }

    class ListObserver implements Observer {
        @Override
        public void update(Observable observable, Object o) {
            if (!(o instanceof List<?>)) return;
            List<CoordinateTrie> list = (List<CoordinateTrie>) o;
            assertEquals(4, list.size());
        }
    }

    @Test
    public void plumbingTest() throws Exception {
        String[] locationNonAlphabetical = new String[] {
            "Sydney",
            "Alabama",
            "Arizona",
            "Albuquerque",
            "Anaheim"
        };

        CoordinateTrie root = new CoordinateTrie();
        for (String locus : locationNonAlphabetical) {
            root.buildTrie(null, locus, dud, dud);
        }

        ArrayList<CoordinateTrie> list = new ArrayList<>();

        ViewModel viewModel = ViewModel.getInstance();
        JsonReader jsonReader = mock(JsonReader.class);
        SelectionObserver listObserver = new SelectionObserver();
        ListObserver nodeObserver = new ListObserver();
        viewModel.setTrie(root, jsonReader);
        viewModel.addObserver(listObserver);
        viewModel.addObserver(nodeObserver);

        viewModel.onFilter("a");
        root.searchTree(SelectionObserver.SYDNEY, list);
        viewModel.onSelected(list.get(0));

        viewModel.deleteObservers();
    }
}
