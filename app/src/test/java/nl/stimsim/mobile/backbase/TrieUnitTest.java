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

    static String toString(Set<CoordinateTrie> set) {
        String content = "";
        for (CoordinateTrie s : set) {
            content += s.originalName + " "; // O(n^2) // so what
        }
        return content.trim();
    }

    @Test
    public void buildTrieWithOverlappingValuesTest() throws Exception {
        // locations
        String[] locations = new String[] {
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
            assertTrue(node.normalizedName + " in " + toString(set), result.contains(node.normalizedName));
        }
    }

    @Test
    public void buildTrieThenSearchTreeTest() throws Exception {
        // locations
        String[] locations = new String[] {
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
        assertTrue(toString(set), 4 == set.size());

        set.clear();

        root.searchTree("z", set);
        assertTrue(toString(set), 4 == set.size());

        set.clear();

        root.searchTree("aa", set);
        assertTrue(toString(set), 3 == set.size());

        set.clear();

        root.searchTree("aaa", set);
        assertTrue(toString(set), 2 == set.size());

        set.clear();

        root.searchTree("aaaa", set);
        assertTrue(toString(set), 1 == set.size());

        set.clear();

        root.searchTree("az", set);
        assertTrue(toString(set), 0 == set.size());

        set.clear();

        // TODO in the UI filter on non-alphabetical values
        root.searchTree("doesn't exist ''''", set);
        assertTrue(toString(set), 0 == set.size());

        set.clear();

        root.searchTree(" a ", set);
        assertTrue(toString(set), 4 == set.size());
    }
}