package cos420.robotrally.adaptersAndItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cos420.robotrally.MainActivity;
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
        ImageView imageView = itemView.findViewById(R.id.tile_view);

        if (gridItem != null) {
            //imageView.setImageResource(gridItem.getImage());
            if (gridItem.isGif()) { //is a gif
                Glide.with(this.getContext())
                        .asGif()
                        .load(gridItem.getImage())
                        .into(imageView);
            }else {//not a gif
                Glide.with(this.getContext())
                        .load(gridItem.getImage())
                        .into(imageView);
            }
        }

        return itemView;
    }
}
