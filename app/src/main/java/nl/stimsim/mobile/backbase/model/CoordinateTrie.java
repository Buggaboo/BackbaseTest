package nl.stimsim.mobile.backbase.model;

import android.support.annotation.NonNull;

import java.text.Normalizer;
import java.util.List;

/**
 * Created by jasmsison on 26/03/2018.
 */

/**
 * Assume the names are unique (i.e. all names will become unique leaves)
 * but longer names may subsume other shorter names,
 * e.g. "aab", "aa"... the 2nd is a stepping stone for the first.
 */
public class CoordinateTrie {
    private static final int A = Character.getNumericValue('a');
    final public String countryAbbreviation;
    final char prefixLetter;
    final boolean isLeaf; // default false
    final public String normalizedName;
    final public String originalName;
    final public double coordLong;
    final public double coordLat;
    private CoordinateTrie[] trieChildren;

    // Big assumption: all names are unique! // TODO double check
    // otherwise, (coordLong, coordLat) must be put in a list

    // ctor for the root node
    public CoordinateTrie() {
        this(null, null, null, 0.0f, 0.0f, (char) 0, false, allocateEmptyTrieArray());
    }

    private CoordinateTrie(char normalizedLowercasedLetter) {
        this(null, null, null, 0.0f, 0.0f, normalizedLowercasedLetter, false, allocateEmptyTrieArray());
    }

    private CoordinateTrie(String countryAbbreviation, String normalizedName, String originalName, double coordLong, double coordLat, char letter, boolean isLeaf, CoordinateTrie[] trieChildren) {
        this.countryAbbreviation = countryAbbreviation;
        this.isLeaf = isLeaf;
        this.normalizedName = normalizedName;
        this.originalName = originalName;
        this.coordLong = coordLong;
        this.coordLat  = coordLat;
        this.prefixLetter = letter;
        this.trieChildren = trieChildren;
    }

    // This increases chance of collisions... we'll see
    public int getArrayIndex(char letter) {
        int delta = Character.getNumericValue(letter) - A;
        if (delta > 26) {
            return delta % 26;
        }else if (delta < 0) {
            return -delta;
        }
        return delta; // normalize to zero-index
    }

    // Best effort
    public String normalize(String input) {

        // TODO if there's time, precompile the regexp
        String result =  Normalizer
            .normalize(input.toLowerCase(), Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
            // .replaceAll("\\W", "") // non letter stuff, e.g. '` etc. // Warning: this makes data disappear
            .replaceAll("\\s+","") // remove spaces
            .replaceAll("‘", "")
            .replaceAll("ß", "ss")
            .replaceAll("ø", "eu");

        //System.out.println(String.format("orig: %s, res: %s", input, result));

        return result;
    }

    // entry function, guarantees normalization
    public void searchTree(String prefix, final List<CoordinateTrie> list) {
        searchTree(normalize(prefix), 0, list);
    }

    public void searchTree(String prefix, int charPosition, final List<CoordinateTrie> list) {
        if (isLeaf && charPosition == prefix.length()) {
            list.add(this);
        }

        if (charPosition < prefix.length()) {
            CoordinateTrie trieChild;
            if ((trieChild = trieChildren[getArrayIndex(prefix.charAt(charPosition))]) != null) {
                trieChild.searchTree(prefix, charPosition+1, list);
            }
        }else {
            for(CoordinateTrie trieNode : trieChildren) {
                if (trieNode != null) {
                    trieNode.filterLeaves(list);
                }
            }
        }
    }

    // add leaves indiscriminately
    public void filterLeaves(final List<CoordinateTrie> list) {
        if (isLeaf) {
            list.add(this);
        }

        for(CoordinateTrie trieNode : trieChildren) {
            if (trieNode != null) {
                trieNode.filterLeaves(list);
            }
        }
    }

    // entry function, guarantees normalization
    public void buildTrie(String country, String original, double coordLong, double coordLat) {
        // 0. normalize
        // 1. Create the root, then enter recursion
        buildTrie(country, original, normalize(original), 0, coordLong, coordLat);
    }

    public void buildTrie(String country, String original, String normalized, int charPosition, double coordLong, double coordLat) {

        char letter = normalized.charAt(charPosition);
        int arrayIndex = getArrayIndex(letter);

        /*
        if (BuildConfig.DEBUG) {
            if (arrayIndex < 0)
            Log.e("negative index", String.format("orig:%s; norm:%s; ltr:%s", original, normalized, letter));
        }
        */

        // last item
        if ((charPosition + 1) ==  normalized.length()) {
            // A leaf trie node, can still be a stepping stone, for another leaf
            trieChildren[arrayIndex] = new CoordinateTrie(country, normalized, original, coordLong, coordLat, letter, true, trieChildren[arrayIndex] == null ? allocateEmptyTrieArray() : trieChildren[arrayIndex].trieChildren);
            return;
        }

        CoordinateTrie trieChild;

        if (charPosition < normalized.length()) {
            // Big assumption, all cities are unique, but allow subsumption; only clobber values if necessary
            if (trieChildren[arrayIndex] == null) {
                trieChild = new CoordinateTrie(letter);
                trieChildren[arrayIndex] = trieChild;
            }else {
                // subsumptions shouldn't be an issue
                trieChild = trieChildren[arrayIndex];
            }
            // enter recursion
            trieChild.buildTrie(country, original, normalized, charPosition + 1, coordLong, coordLat);
        }
    }

    @NonNull
    private static CoordinateTrie[] allocateEmptyTrieArray() {
        return new CoordinateTrie[26];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoordinateTrie that = (CoordinateTrie) o;

        if (prefixLetter != that.prefixLetter) return false;
        if (isLeaf != that.isLeaf) return false;
        if (Double.compare(that.coordLong, coordLong) != 0) return false;
        if (Double.compare(that.coordLat, coordLat) != 0) return false;
        if (!normalizedName.equals(that.normalizedName)) return false;
        return originalName.equals(that.originalName);
    }

    @Override
    public int hashCode() {
        int result = (int) prefixLetter;
        result = 31 * result + (isLeaf ? 1 : 0);
        result = 31 * result + normalizedName.hashCode();
        result = 31 * result + originalName.hashCode();
        result = (int) (31 * result + (coordLong != +0.0f ? Double.doubleToLongBits(coordLong) : 0));
        result = (int) (31 * result + (coordLat != +0.0f ? Double.doubleToLongBits(coordLat) : 0));
        return result;
    }
}
