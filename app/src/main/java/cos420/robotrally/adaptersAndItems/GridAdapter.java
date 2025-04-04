package cos420.robotrally.adaptersAndItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import cos420.robotrally.R;

// TODO javadoc
public class GridAdapter extends ArrayAdapter<GridItem> {

    // TODO javadoc
    public GridAdapter(Context context, ArrayList<GridItem> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.grid_tile,
                            parent, false);
        }

        GridItem gridItem = getItem(position);

        TextView textView = itemView.findViewById(R.id.tile_view);

        if (gridItem != null) {
            textView.setText(gridItem.getText());
            textView.setBackground(gridItem.getColor());
        }

        return itemView;
    }
}
