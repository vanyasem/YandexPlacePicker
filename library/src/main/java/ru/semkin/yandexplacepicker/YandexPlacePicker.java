package ru.semkin.yandexplacepicker;

import android.app.Activity;
import android.content.Intent;

import ru.semkin.yandexplacepicker.ui.YandexPickerActivity;

public class YandexPlacePicker {

    public static final String EXTRA_PLACE = "extra_place";
    public static String yandexMapsKey;

    public static class IntentBuilder {

        private Intent intent = new Intent();

        public IntentBuilder setYandexMapsKey(String mapsKey) {
            yandexMapsKey = mapsKey;
            return this;
        }

        public Intent build(Activity activity) {
            intent.setClass(activity, YandexPickerActivity.class);
            return intent;
        }
    }

    public static PlaceParcelable getPlace(Intent intent) {
        return intent.getParcelableExtra(EXTRA_PLACE);
    }
}
