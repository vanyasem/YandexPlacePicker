package ru.semkin.yandexplacepicker.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yandex.mapkit.GeoObject;
import com.yandex.mapkit.GeoObjectCollection;

import java.util.List;

import ru.semkin.yandexplacepicker.R;

public class PlacePickerAdapter  extends RecyclerView.Adapter<PlacePickerAdapter.PlaceViewHolder> {

    public PlacePickerAdapter(List<GeoObjectCollection.Item> places, OnPlaceSelected listener) {
        mPlaceList = places;
        mClickListener = listener;
    }

    private List<GeoObjectCollection.Item> mPlaceList;
    private OnPlaceSelected mClickListener;

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
        ImageView ivPlaceType;
        TextView tvPlaceName;
        TextView tvPlaceAddress;

        private PlaceViewHolder(View itemView) {
            super(itemView);
            ivPlaceType = itemView.findViewById(R.id.ivPlaceType);
            tvPlaceName = itemView.findViewById(R.id.tvPlaceName);
            tvPlaceAddress = itemView.findViewById(R.id.tvPlaceAddress);
        }

        public void bind(GeoObject place, OnPlaceSelected listener) {
            itemView.setOnClickListener(v -> listener.onPlaceSelected(place));
            ivPlaceType.setImageResource(UiUtils.getPlaceDrawableRes(itemView.getContext(), place));
            tvPlaceName.setText(place.getName());
            tvPlaceAddress.setText(place.getDescriptionText());
        }
    }

    interface OnPlaceSelected {
        void onPlaceSelected(GeoObject place);
    }
}
