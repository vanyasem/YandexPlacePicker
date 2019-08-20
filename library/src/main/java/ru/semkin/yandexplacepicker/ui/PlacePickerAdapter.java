package ru.semkin.yandexplacepicker.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.yandex.mapkit.GeoObject;
import com.yandex.mapkit.GeoObjectCollection;

import java.util.List;

import ru.semkin.yandexplacepicker.R;

public class PlacePickerAdapter  extends RecyclerView.Adapter<PlacePickerAdapter.PlaceViewHolder> {

    public PlacePickerAdapter(List<GeoObjectCollection.Item> places, boolean search, OnPlaceListener listener) {
        mPlaceList = places;
        mClickListener = listener;
        mSearch = search;
    }

    private List<GeoObjectCollection.Item> mPlaceList;
    private final OnPlaceListener mClickListener;
    private final boolean mSearch;

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final @NonNull PlaceViewHolder holder, int position) {
        holder.bind(mPlaceList.get(position).getObj(), mClickListener);
    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }

    public void swapData(List<GeoObjectCollection.Item> newPlaceList) {
        mPlaceList = newPlaceList;
        notifyDataSetChanged();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivPlaceType;
        final TextView tvPlaceName;
        final TextView tvPlaceAddress;
        final MaterialButton btnSelect;

        private PlaceViewHolder(View itemView) {
            super(itemView);
            ivPlaceType = itemView.findViewById(R.id.ivPlaceType);
            tvPlaceName = itemView.findViewById(R.id.tvPlaceName);
            tvPlaceAddress = itemView.findViewById(R.id.tvPlaceAddress);
            btnSelect = itemView.findViewById(R.id.btnSelect);

        }

        void bind(GeoObject place, OnPlaceListener listener) {
            // Hide or show place icons according to the config
            if (itemView.getContext().getResources().getBoolean(R.bool.show_place_icons)) {
                ivPlaceType.setImageResource(UiUtils.getPlaceDrawableRes(itemView.getContext(), place));
            } else {
                ivPlaceType.setVisibility(View.GONE);
            }

            // Hide or show the select button according to the config
            if (itemView.getContext().getResources().getBoolean(R.bool.show_confirmation_buttons) &&
                    !mSearch) {
                btnSelect.setVisibility(View.VISIBLE);
                btnSelect.setOnClickListener(v -> listener.onPlaceSelected(place));
                itemView.setOnClickListener(v -> listener.onPlacePreviewed(place));
            } else {
                btnSelect.setVisibility(View.GONE);
                itemView.setOnClickListener(v -> listener.onPlaceSelected(place));
            }

            tvPlaceName.setText(place.getName());
            tvPlaceAddress.setText(place.getDescriptionText());
        }
    }

    interface OnPlaceListener {
        void onPlaceSelected(GeoObject place);
        void onPlacePreviewed(GeoObject place);
    }
}
