package nl.stimsim.mobile.backbase;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static nl.stimsim.mobile.backbase.TrieUnitTest.stringifyTrieSet;

/**
 * Local unit test for the Trie implementation, which will execute on the development machine (host).
 */
public class ReadJsonUnitTest {

    public static String sampleJson;

    static {
        sampleJson = "[\n" +
                "{\"country\":\"UA\",\"name\":\"Hurzuf\",\"_id\":707860,\"coord\":{\"lon\":34.283333,\"lat\":44.549999}},\n" +
                "{\"country\":\"RU\",\"name\":\"Novinki\",\"_id\":519188,\"coord\":{\"lon\":37.666668,\"lat\":55.683334}},\n" +
                "{\"country\":\"NP\",\"name\":\"Gorkhā\",\"_id\":1283378,\"coord\":{\"lon\":84.633331,\"lat\":28}},\n" +
                "{\"country\":\"IN\",\"name\":\"State of Haryāna\",\"_id\":1270260,\"coord\":{\"lon\":76,\"lat\":29}},\n" +
                "{\"country\":\"UA\",\"name\":\"Holubynka\",\"_id\":708546,\"coord\":{\"lon\":33.900002,\"lat\":44.599998}},\n" +
                "{\"country\":\"NP\",\"name\":\"Bāgmatī Zone\",\"_id\":1283710,\"coord\":{\"lon\":85.416664,\"lat\":28}},\n" +
                "{\"country\":\"RU\",\"name\":\"Mar’ina Roshcha\",\"_id\":529334,\"coord\":{\"lon\":37.611111,\"lat\":55.796391}},\n" +
                "{\"country\":\"IN\",\"name\":\"Republic of India\",\"_id\":1269750,\"coord\":{\"lon\":77,\"lat\":20}},\n" +
                "{\"country\":\"NP\",\"name\":\"Kathmandu\",\"_id\":1283240,\"coord\":{\"lon\":85.316666,\"lat\":27.716667}},\n" +
                "{\"country\":\"UA\",\"name\":\"Laspi\",\"_id\":703363,\"coord\":{\"lon\":33.733334,\"lat\":44.416668}},\n" +
                "{\"country\":\"VE\",\"name\":\"Merida\",\"_id\":3632308,\"coord\":{\"lon\":-71.144997,\"lat\":8.598333}},\n" +
                "{\"country\":\"RU\",\"name\":\"Vinogradovo\",\"_id\":473537,\"coord\":{\"lon\":38.545555,\"lat\":55.423332}},\n" +
                "{\"country\":\"IQ\",\"name\":\"Qarah Gawl al ‘Ulyā\",\"_id\":384848,\"coord\":{\"lon\":45.6325,\"lat\":35.353889}},\n" +
                "{\"country\":\"RU\",\"name\":\"Cherkizovo\",\"_id\":569143,\"coord\":{\"lon\":37.728889,\"lat\":55.800835}},\n" +
                "{\"country\":\"UA\",\"name\":\"Alupka\",\"_id\":713514,\"coord\":{\"lon\":34.049999,\"lat\":44.416668}},\n" +
                "{\"country\":\"DE\",\"name\":\"Lichtenrade\",\"_id\":2878044,\"coord\":{\"lon\":13.40637,\"lat\":52.398441}},\n" +
                "{\"country\":\"RU\",\"name\":\"Zavety Il’icha\",\"_id\":464176,\"coord\":{\"lon\":37.849998,\"lat\":56.049999}},\n" +
                "{\"country\":\"IL\",\"name\":\"‘Azriqam\",\"_id\":295582,\"coord\":{\"lon\":34.700001,\"lat\":31.75}},\n" +
                "{\"country\":\"IN\",\"name\":\"Ghūra\",\"_id\":1271231,\"coord\":{\"lon\":79.883331,\"lat\":24.766666}},\n" +
                "{\"country\":\"UA\",\"name\":\"Tyuzler\",\"_id\":690856,\"coord\":{\"lon\":34.083332,\"lat\":44.466667}},\n" +
                "{\"country\":\"RU\",\"name\":\"Zaponor’ye\",\"_id\":464737,\"coord\":{\"lon\":38.861942,\"lat\":55.639999}},\n" +
                "{\"country\":\"UA\",\"name\":\"Il’ichëvka\",\"_id\":707716,\"coord\":{\"lon\":34.383331,\"lat\":44.666668}},\n" +
                "{\"country\":\"UA\",\"name\":\"Partyzans’ke\",\"_id\":697959,\"coord\":{\"lon\":34.083332,\"lat\":44.833332}},\n" +
                "{\"country\":\"RU\",\"name\":\"Yurevichi\",\"_id\":803611,\"coord\":{\"lon\":39.934444,\"lat\":43.600555}},\n" +
                "{\"country\":\"GE\",\"name\":\"Gumist’a\",\"_id\":614371,\"coord\":{\"lon\":40.973888,\"lat\":43.026943}},\n" +
                "{\"country\":\"GE\",\"name\":\"Ptitsefabrika\",\"_id\":874560,\"coord\":{\"lon\":40.290558,\"lat\":43.183613}},\n" +
                "{\"country\":\"GE\",\"name\":\"Orekhovo\",\"_id\":874652,\"coord\":{\"lon\":40.146111,\"lat\":43.351391}},\n" +
                "{\"country\":\"NG\",\"name\":\"Birim\",\"_id\":2347078,\"coord\":{\"lon\":9.997027,\"lat\":10.062094}},\n" +
                "{\"country\":\"RU\",\"name\":\"Priiskovyy\",\"_id\":2051302,\"coord\":{\"lon\":132.822495,\"lat\":42.819168}}\n" +
                "]";
    }

    @Test
    public void readAsJsonObjects() throws Exception {
        DataReader dataReader = new DataReader();
        InputStream stream = new ByteArrayInputStream(sampleJson.getBytes(StandardCharsets.UTF_8));
        CoordinateTrie root = new CoordinateTrie();
        dataReader.fromJsonReader(root, stream);

        Set<CoordinateTrie> set = new HashSet<>();
        root.searchTree("bir", set);
        assertEquals(1, set.size());

        set.clear();

        root.searchTree("p", set);
        assertEquals(stringifyTrieSet(set),3, set.size());

        set.clear();

        root.searchTree("a", set);
        assertEquals(stringifyTrieSet(set),2, set.size());
    }

    /*
    @Test
    public void readWithRegEx() throws Exception {

    }
    */
}
