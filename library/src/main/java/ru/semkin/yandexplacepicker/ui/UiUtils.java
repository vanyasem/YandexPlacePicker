package ru.semkin.yandexplacepicker.ui;

import android.content.Context;

import com.yandex.mapkit.GeoObject;
import com.yandex.mapkit.search.BusinessObjectMetadata;
import com.yandex.mapkit.search.Category;

import java.util.List;

import ru.semkin.yandexplacepicker.R;

public class UiUtils {

    /**
     * Gets the place drawable resource according to its type
     */
    static public int getPlaceDrawableRes(Context context, GeoObject place) {
        String defType = "drawable";
        String defPackage = context.getPackageName();

        BusinessObjectMetadata metadata = place.getMetadataContainer().getItem(BusinessObjectMetadata.class);
        if(metadata != null) {
            List<Category> categories =  metadata.getCategories();

            for (Category category: categories) {
                if(category != null && category.getCategoryClass() != null && !category.getCategoryClass().equals("null")) {
                    String catClass = category.getCategoryClass().replace(' ', '_');
                    int id = context.getResources()
                            .getIdentifier("ic_places_" + catClass, defType, defPackage);
                    if (id > 0) return id; //todo place icons
                }
            }
        }

        // Default resource
        return R.drawable.ic_map_marker_black_24dp;
    }
}
