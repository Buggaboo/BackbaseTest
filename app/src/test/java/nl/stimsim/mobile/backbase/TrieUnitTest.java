package nl.stimsim.mobile.backbase;

import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Local unit test for the Trie implementation, which will execute on the development machine (host).
 */
public class TrieUnitTest {
    final static float dud = 1.0f;

    public static String stringifyTrieSet(Set<CoordinateTrie> set) {
        String content = "";
        for (CoordinateTrie s : set) {
            content += s.originalName + " "; // O(n^2) // so what
        }
        return content.trim();
    }

    @Test
    public void buildTrieWithOverlappingValuesTest() throws Exception {
        // locations
        String[] locations = new String[]{
                "aab", "aa", "azabcd", "a"
        };

        CoordinateTrie root = new CoordinateTrie();
        for (String locus : locations) {
            root.buildTrie(locus, dud, dud);
        }

        HashSet<CoordinateTrie> set = new HashSet<>();
        root.filterLeaves(set);

        assertEquals(locations.length, set.size());

        Set<String> result = new HashSet<>();

        for (String locus : locations) {
            result.add(locus);
        }

        for (CoordinateTrie node : set) {
            assertTrue(node.normalizedName + " in " + stringifyTrieSet(set), result.contains(node.normalizedName));
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
            root.buildTrie(locus, dud, dud);
        }

        HashSet<CoordinateTrie> set = new HashSet<>();
        root.filterLeaves(set);

        assertEquals(locations.length, set.size());

        set.clear();

        root.searchTree("a", set);
        assertTrue(stringifyTrieSet(set), 4 == set.size());

        set.clear();

        root.searchTree("z", set);
        assertTrue(stringifyTrieSet(set), 4 == set.size());

        set.clear();

        root.searchTree("aa", set);
        assertTrue(stringifyTrieSet(set), 3 == set.size());

        set.clear();

        root.searchTree("aaa", set);
        assertTrue(stringifyTrieSet(set), 2 == set.size());

        set.clear();

        root.searchTree("aaaa", set);
        assertTrue(stringifyTrieSet(set), 1 == set.size());

        set.clear();

        root.searchTree("az", set);
        assertTrue(stringifyTrieSet(set), 0 == set.size());

        set.clear();

        // TODO in the UI filter on non-alphabetical values
        root.searchTree("doesn't exist ''''", set);
        assertTrue(stringifyTrieSet(set), 0 == set.size());

        set.clear();

        root.searchTree(" a ", set);
        assertTrue(stringifyTrieSet(set), 4 == set.size());
    }

    @Test
    public void normalizationTest() throws Exception {
        String[] locations = new String[]{
                "aàáâäãåā",
                "aàáâäãå",
                "aàáâäã",
                "aàáâä",
                "aàáâ",
                "aàá",
                "aà",
                "a"
                // "æÆ", // breaks horribly, TODO fix this from the UI side, write a test for this
        };

        CoordinateTrie root = new CoordinateTrie();
        for (String locus : locations) {
            root.buildTrie(locus, dud, dud);
        }

        HashSet<CoordinateTrie> set = new HashSet<>();

        // basic test
        root.filterLeaves(set);
        assertEquals(locations.length, set.size());

        // a lotta character 'a's
        String alot = "aaaaaaaa";

        for (int i = 1; i <= set.size(); i++) {
            set.clear();
            String substring = alot.substring(0, i);
            root.searchTree(substring, set);
            assertEquals(substring + " in " + stringifyTrieSet(set), (locations.length + 1) - i, set.size());
        }

    }
}
