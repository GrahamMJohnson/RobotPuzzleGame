package cos420.robotrally.adaptersAndItems;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;

import cos420.robotrally.R;

/**
 * Helps create dynamic GUI for the level select menu
 * @author Bright Holst
 * @version 01/04/2025
 */
public class LevelAdapter extends ArrayAdapter<LevelItem> {
    /** Listener to allow the buttons to work */
    LevelSelectListener listener;

    /**
     * Constructor
     * @param context   the current context
     * @param list      of LevelItems
     * @param listener  to handle button clicks
     */
    public LevelAdapter(Context context, ArrayList<LevelItem> list, LevelSelectListener listener) {
        super(context, 0, list);
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.level_select_level_tile,
                            parent, false);
        }

        LevelItem levelItem = getItem(position);

        if (levelItem != null) {
            AppCompatButton button = itemView.findViewById(R.id.levelDynamic);
            button.setText(levelItem.getDisplayName());
            // Make button clickable
            button.setOnClickListener(v -> {
                listener.onLevelSelectClick(levelItem.getID());
            });
            // TODO Set percentage bar
        }

        return itemView;
    }

    /**
     * Interface to allow the main activity to tell when these buttons are clicked.
     */
    public interface LevelSelectListener {
        /**
         * Verifies valid level number and opens level
         * @param levelID to try to open
         */
        public void onLevelSelectClick(int levelID);
    }
}
