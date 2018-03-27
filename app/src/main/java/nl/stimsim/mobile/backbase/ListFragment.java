package nl.stimsim.mobile.backbase;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import nl.stimsim.mobile.backbase.model.CoordinateTrie;
import nl.stimsim.mobile.backbase.model.ViewModel;

/**
 * A fragment representing a list of Items.
 */
public class ListFragment extends Fragment implements Observer, TextWatcher {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list, container, false);
        ViewModel.getInstance().addObserver(this);
        setupAdapter(view);
        setupSearch(view);
        return view;
    }

    RecyclerViewEmptySupport recyclerView;
    TextView emptyText;
    Drawable divider;
    EditText search;

    private void setupSearch(View view) {
        search = view.findViewById(R.id.search);
        search.addTextChangedListener(this);
    }

    private void setupAdapter(View view) {
        recyclerView = view.findViewById(R.id.list);
        emptyText = view.findViewById(R.id.empty_list);

        divider = getResources().getDrawable(R.drawable.divider);

        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setEmptyView(emptyText);
        setDividerItemDecoration(recyclerView, divider);
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        ViewModel.getInstance().deleteObserver(this);
        search.removeTextChangedListener(this);
        super.onDestroy();
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

    @Override
    public void update(Observable observable, Object o) {
        // java type erasure
        if (adapter != null && o instanceof ArrayList<?>) {
            adapter.setCoordinates((List<CoordinateTrie>) o);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        ViewModel.getInstance().onFilter(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
