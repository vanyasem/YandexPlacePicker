package ru.semkin.yandexplacepicker.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.yandex.mapkit.GeoObject;

import java.util.Locale;

import ru.semkin.yandexplacepicker.PlaceParcelable;
import ru.semkin.yandexplacepicker.R;

public class PlaceConfirmDialogFragment extends AppCompatDialogFragment {

    private final static String ARG_PLACE = "arg_place";

    static public PlaceConfirmDialogFragment newInstance(GeoObject place, OnPlaceConfirmedListener listener) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_PLACE, new PlaceParcelable(place));

        PlaceConfirmDialogFragment fragment = new PlaceConfirmDialogFragment();
        fragment.setArguments(args);
        fragment.confirmListener = listener;
        return fragment;
    }

    public OnPlaceConfirmedListener confirmListener;

    private PlaceParcelable mPlace;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check mandatory parameters for this fragment
        if ((getArguments() == null) || (getArguments().getParcelable(ARG_PLACE) == null)) {
            throw new IllegalArgumentException("You must pass a Place as argument to this fragment");
        }

        mPlace = getArguments().getParcelable(ARG_PLACE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.picker_place_confirm)
                .setView(getContentView(getActivity()))
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    confirmListener.onPlaceConfirmed(mPlace);
                    dismiss();
                })
                .setNegativeButton(R.string.picker_place_confirm_cancel, (dialog, which) ->
                        dismiss());

        return builder.create();
    }

    @SuppressLint("InflateParams")
    private View getContentView(Context context) {
        View content = LayoutInflater.from(context)
                .inflate(R.layout.fragment_dialog_place_confirm, null);

        ((TextView)content.findViewById(R.id.tvPlaceName)).setText(mPlace.getName());
        ((TextView)content.findViewById(R.id.tvPlaceAddress)).setText(mPlace.getAddress());

        fetchPlaceMap(context, content);
        //fetchPlacePhoto(content); todo place photo

        return content;
    }

    private void fetchPlaceMap(Context context, View content) {
        String lang = "en_US";
        if(Locale.getDefault().getLanguage().equalsIgnoreCase(new Locale("ru").getLanguage()))
            lang = "ru_RU";

        float width = context.getResources().getDisplayMetrics().widthPixels - 84 * context.getResources().getDisplayMetrics().density;
        float height = 110 * context.getResources().getDisplayMetrics().density;
        if(width > 650) {
            float scaleFactor = 650f / width;
            width  *= scaleFactor;
            height *= scaleFactor;
        }
        if(height > 450) {
            float scaleFactor = 450f / height;
            width  *= scaleFactor;
            height *= scaleFactor;
        }

        String url = "https://static-maps.yandex.ru/1.x/?"
                + "ll=" + mPlace.getPoint().getLongitude() + "," + mPlace.getPoint().getLatitude()
                + "&"
                + "size=" + Math.round(width) + "," + Math.round(height)
                + "&"
                + "z=18"
                + "&"
                + "l=map"
                + "&"
                + "lang=" + lang;
        //+ "key=\(Storage.ymkKey)";

        ImageView image = content.findViewById(R.id.ivPlaceMap);
        loadImage(image, url);
    }

    public static void loadImage(final ImageView view, String urlImage) {
        try {
            RequestCreator request = Picasso.get().load(urlImage);
            request.into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface OnPlaceConfirmedListener {
        void onPlaceConfirmed(PlaceParcelable place);
    }
}
