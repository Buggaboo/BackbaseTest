package nl.stimsim.mobile.backbase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Local unit test for the Trie implementation, which will execute on the development machine (host).
 */
public class TrieUnitTest {
    final static float dud = 1.0f;

    public static String stringifyTrieList(List<CoordinateTrie> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (CoordinateTrie s : list) {
            stringBuilder.append(s.originalName).append(" ");
        }
        return stringBuilder.toString().trim();
    }

    @Test
    public void buildTrieWithOverlappingValuesTest() throws Exception {
        // locations
        String[] locations = new String[]{
                "aab", "aa", "azabcd", "a"
        };

        CoordinateTrie root = new CoordinateTrie();
        for (String locus : locations) {
            root.buildTrie(null, locus, dud, dud);
        }

        ArrayList<CoordinateTrie> list = new ArrayList<>();
        root.filterLeaves(list);

        assertEquals(locations.length, list.size());

        List<String> result = new ArrayList<>();

        for (String locus : locations) {
            result.add(locus);
        }

        for (CoordinateTrie node : list) {
            assertTrue(node.normalizedName + " in " + stringifyTrieList(list), result.contains(node.normalizedName));
        }
    }

    @Test
    public void buildTrieThenSearchTreeTest() throws Exception {
        // locations
        String[] locations = new String[]{
                "aaaa", "aaa", "aa", "a",
                "zzzz", "zzz", "zz", "z"
        };

        CoordinateTrie root = new CoordinateTrie();
        for (String locus : locations) {
            root.buildTrie(null, locus, dud, dud);
        }

        ArrayList<CoordinateTrie> list = new ArrayList<>();
        root.filterLeaves(list);

        assertEquals(locations.length, list.size());

        list.clear();

        root.searchTree("a", list);
        assertTrue(stringifyTrieList(list), 4 == list.size());

        list.clear();

        root.searchTree("z", list);
        assertTrue(stringifyTrieList(list), 4 == list.size());

        list.clear();

        root.searchTree("aa", list);
        assertTrue(stringifyTrieList(list), 3 == list.size());

        list.clear();

        root.searchTree("aaa", list);
        assertTrue(stringifyTrieList(list), 2 == list.size());

        list.clear();

        root.searchTree("aaaa", list);
        assertTrue(stringifyTrieList(list), 1 == list.size());

        list.clear();

        root.searchTree("az", list);
        assertTrue(stringifyTrieList(list), 0 == list.size());

        list.clear();

        // TODO in the UI filter on non-alphabetical values
        root.searchTree("doesn't exist ''''", list);
        assertTrue(stringifyTrieList(list), 0 == list.size());

        list.clear();

        root.searchTree(" a ", list);
        assertTrue(stringifyTrieList(list), 4 == list.size());
    }



    @Test
    public void normalizeHangulEtcTest() throws Exception {
        String[] locations = new String[]{
                "괴내",
                "æÆ",
                "ß",
                "ø",
                "ł"
        };

        CoordinateTrie root = new CoordinateTrie();

        ArrayList<CoordinateTrie> list = new ArrayList<>();

        for (String locus : locations) {
            root.normalize(locus); // see if nothing breaks
            root.buildTrie(null, locus, dud, dud);
        }

        root.filterLeaves(list);

        assertEquals(stringifyTrieList(list), 5, list.size());
    }

    @Test
    public void trieNormalizationDiacriticTest() throws Exception {
        String[] locations = new String[]{
                "aàáâäãåā",
                "aàáâäãå",
                "aàáâäã",
                "aàáâä",
                "aàáâ",
                "aàá",
                "aà",
                "a"
        };

        CoordinateTrie root = new CoordinateTrie();
        for (String locus : locations) {
            root.buildTrie(null, locus, dud, dud);
        }

        ArrayList<CoordinateTrie> list = new ArrayList<>();

        // basic test
        root.filterLeaves(list);
        assertEquals(locations.length, list.size());

        // a lotta character 'a's
        String alot = "aaaaaaaa";

        for (int i = 1; i <= list.size(); i++) {
            list.clear();
            String substring = alot.substring(0, i);
            root.searchTree(substring, list);
            assertEquals(substring + " in " + stringifyTrieList(list), (locations.length + 1) - i, list.size());
        }

    }
}
