package nl.stimsim.mobile.backbase;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nl.stimsim.mobile.backbase.model.CoordinateTrie;
import nl.stimsim.mobile.backbase.model.ViewModel;

public class RecyclerViewAdapter extends RecyclerViewEmptySupport.Adapter<RecyclerViewAdapter.ViewHolder> {

    public void setCoordinates(List<CoordinateTrie> coordinates) {
        this.coordinates = coordinates;
    }

    // Use this as the main data source
    private List<CoordinateTrie> coordinates;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CoordinateTrie trie = coordinates.get(position);
        holder.trie = trie;
        holder.nameText.setText(trie.originalName);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("long.: ").append(String.valueOf(trie.coordLong))
                .append(" lat.: ").append(String.valueOf(trie.coordLat));
        holder.descText.setText(stringBuilder.toString());
        holder.descText2.setText(trie.countryAbbreviation);
    }

    @Override
    public int getItemCount() {
        if (coordinates == null) {
            return 0;
        }

        return coordinates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @Nullable
        public TextView nameText, descText, descText2;

        @Nullable
        public CoordinateTrie trie;

        public ViewHolder(View view) {
            super(view);
            nameText = view.findViewById(R.id.name);
            descText = view.findViewById(R.id.description);
            descText2 = view.findViewById(R.id.description2);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ViewModel.getInstance().onSelected(trie);
        }
    }
}
