package nl.stimsim.mobile.backbase;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a list of Items.
 */
public class ListFragment extends Fragment {

    public final static String TAG = "LIST_TAG";

    private RecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListFragment() {}

    @SuppressWarnings("unused")
    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO do cool stuff
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list, container, false);
        setupAdapter(view);
        return view;
    }

    RecyclerViewEmptySupport recyclerView;
    TextView emptyText;
    Drawable divider;

    private void setupAdapter(View view) {
        recyclerView = (RecyclerViewEmptySupport) view.findViewById(R.id.list);
        emptyText = view.findViewById(R.id.empty_list);
        divider = getResources().getDrawable(R.drawable.divider);

        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setEmptyView(emptyText);
        setDividerItemDecoration(recyclerView, divider);
        adapter = new RecyclerViewAdapter();
        /*
        adapter.setExerciseList(viewModel.selectedProgramExercises.getValue());
        */
        recyclerView.setAdapter(adapter);
    }

    public void setDividerItemDecoration(RecyclerView recyclerView, Drawable divider) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) layoutManager).getOrientation());
            dividerItemDecoration.setDrawable(divider);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }else {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException();
            }
        }
    }
}
