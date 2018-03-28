package nl.stimsim.mobile.backbase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.gson.stream.JsonReader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.Reader;

import nl.stimsim.mobile.backbase.model.CoordinateTrie;
import nl.stimsim.mobile.backbase.model.ViewModel;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    final static float dud = 1.0f;

    @Rule
    public ActivityTestRule<MapsActivity> activityTestRule = new ActivityTestRule(MapsActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("nl.stimsim.mobile.backbase", appContext.getPackageName());
    }

    @Test
    public void listFragmentLoadedEnterSearchTest() throws Exception {
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

        ViewModel viewModel = ViewModel.getInstance();

        // tried mockito on JsonReader, doesn't work on instrumentation
        // decided to make the 2nd param optional
        viewModel.setTrie(root, null);

        FragmentManager fragmentManager = activityTestRule.getActivity().getSupportFragmentManager();

        // 1. Enter text
        onView(withId(R.id.search)).perform(typeText("a"));
        ListFragment listFragment = (ListFragment) fragmentManager.findFragmentByTag(ListFragment.TAG);
        assertEquals(4, listFragment.recyclerView.getAdapter().getItemCount());

        // 2. Tap item
        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click())); // Alabama
        assertEquals(2, fragmentManager.getBackStackEntryCount());
    }

}
