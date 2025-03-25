package cos420.robotrally.adaptersAndItems;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cos420.robotrally.R;

public class MoveAdapter extends RecyclerView.Adapter<MoveAdapter.MyViewHolder> {

    private List<MoveItem> list;

    //Constructor
    public MoveAdapter(List<MoveItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.moves, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MoveItem moveItem = list.get(position);
        holder.move_view.setText(moveItem.getText());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //ViewHolder class
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView move_view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            move_view = itemView.findViewById(R.id.move_view);
        }
    }
}
