/**
 * This file is part of Privacy Friendly Password Generator.
 * <p>
 * Privacy Friendly Password Generator is free software:
 * you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or any later version.
 * <p>
 * Privacy Friendly Password Generator is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Privacy Friendly Password Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlypasswordgenerator.helpers;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.secuso.privacyfriendlypasswordgenerator.R;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaData;

import java.util.List;

/**
 * Code according to the tutorial from https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
 * accessed 17th June 2016
 */


public class MetaDataAdapter extends RecyclerView.Adapter<MetaDataAdapter.MetaDataViewHolder> {

    private List<MetaData> metaDataList;

    public MetaDataAdapter(List<MetaData> metaData) {
        this.metaDataList = metaData;
    }

    @Override
    public MetaDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_metadata, parent, false);

        return new MetaDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MetaDataViewHolder holder, int position) {

        ColorGenerator generator = ColorGenerator.MATERIAL;

        holder.domain.setText(metaDataList.get(position).getDOMAIN());
        holder.length.setText(String.valueOf(metaDataList.get(position).getLENGTH()));
        holder.iteration.setText(String.valueOf(metaDataList.get(position).getITERATION()));

        if (metaDataList.get(position).getUSERNAME().length() == 0) {
            holder.username.setText("-");
        } else {
            holder.username.setText(metaDataList.get(position).getUSERNAME());
        }

        String characterset = "";

        if (metaDataList.get(position).getHAS_LETTERS_LOW() == 1) {
            characterset += "abc";
        }

        if (metaDataList.get(position).getHAS_LETTERS_UP() == 1) {
            characterset += " ABC";
        }

        if (metaDataList.get(position).getHAS_NUMBERS() == 1) {
            characterset += " 123";
        }

        if (metaDataList.get(position).getHAS_SYMBOLS() == 1) {
            characterset += " +!#";
        }

        holder.characterset.setText(characterset);

        int color = generator.getColor(metaDataList.get(position).getDOMAIN());
        TextDrawable textDrawable = TextDrawable.builder()
                .buildRound(String.valueOf(metaDataList.get(position).getDOMAIN().toUpperCase().charAt(0)), color);
        holder.imageView.setImageDrawable(textDrawable);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return metaDataList.size();
    }

    public class MetaDataViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView domain;
        TextView username;
        TextView length;
        TextView iteration;
        TextView characterset;
        ImageView imageView;

        public MetaDataViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            domain = (TextView) itemView.findViewById(R.id.domainTextView);
            username = (TextView) itemView.findViewById(R.id.username);
            length = (TextView) itemView.findViewById(R.id.length);
            iteration = (TextView) itemView.findViewById(R.id.iteration);
            characterset = (TextView) itemView.findViewById(R.id.characterSet);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);

        }
    }

    public void setMetaDataList(List<MetaData> metaDataList) {
        this.metaDataList = metaDataList;
    }

    public MetaData getItem(int position) {
        return metaDataList.get(position);
    }

    public MetaData removeItem(int position) {
        final MetaData metaData = metaDataList.remove(position);
        notifyItemRemoved(position);
        return metaData;
    }

    public void addItem(int position, MetaData metaData) {
        metaDataList.add(position, metaData);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final MetaData metaData = metaDataList.remove(fromPosition);
        metaDataList.add(toPosition, metaData);
        notifyItemMoved(fromPosition, toPosition);
    }

    private void applyAndAnimateRemovals(List<MetaData> newMetaData) {
        for (int i = metaDataList.size() - 1; i >= 0; i--) {
            final MetaData metaData = metaDataList.get(i);
            if (!newMetaData.contains(metaData)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<MetaData> newMetaData) {
        for (int i = 0, count = newMetaData.size(); i < count; i++) {
            final MetaData metaData = newMetaData.get(i);
            if (!metaDataList.contains(metaData)) {
                addItem(i, metaData);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<MetaData> newMetaData) {
        for (int toPosition = newMetaData.size() - 1; toPosition >= 0; toPosition--) {
            final MetaData metaData = newMetaData.get(toPosition);
            final int fromPosition = metaDataList.indexOf(metaData);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public void animateTo(List<MetaData> metaDataList) {
        applyAndAnimateRemovals(metaDataList);
        applyAndAnimateAdditions(metaDataList);
        applyAndAnimateMovedItems(metaDataList);
    }

}
