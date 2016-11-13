package org.secuso.privacyfriendlypasswordgenerator;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.secuso.privacyfriendlypasswordgenerator.database.MetaData;

import java.util.ArrayList;
import java.util.List;

/**
 * Code according to the tutorial from https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
 */


public class MetaDataAdapter extends RecyclerView.Adapter<MetaDataAdapter.MetaDataViewHolder> {

    List<MetaData> metaDataList;

    public MetaDataAdapter(List<MetaData> metaData) {

        this.metaDataList = metaData;
        initialMetaData();
    }


    @Override
    public MetaDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_metadata, parent, false);
        MetaDataViewHolder metaDataViewHolder = new MetaDataViewHolder(v);
        return metaDataViewHolder;
    }

    @Override
    public void onBindViewHolder(MetaDataViewHolder holder, int position) {
        holder.domain.setText(metaDataList.get(position).getDOMAIN());
        //holder.length.setText(metaDataList.get(position).getLENGTH());

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return metaDataList.size();
    }

    private void initialMetaData() {

        metaDataList = new ArrayList<>();

        MetaData a = new MetaData(1, "google.de", 13, 1, 1, 1, 1);
        metaDataList.add(a);

        MetaData b = new MetaData(1, "amazon.de", 14, 1, 1, 1, 1);
        metaDataList.add(b);

        MetaData c = new MetaData(1, "gmx.de", 15, 1, 1, 1, 1);
        metaDataList.add(c);

        MetaData d = new MetaData(1, "web.de", 16, 1, 1, 1, 1);
        metaDataList.add(d);
    }


    public class MetaDataViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView domain;
        TextView length;

        public MetaDataViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            domain = (TextView) itemView.findViewById(R.id.domainTextView);
            length = (TextView) itemView.findViewById(R.id.length);
        }
    }
}
