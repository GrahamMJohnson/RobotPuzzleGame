package cos420.robotrally.adaptersAndItems;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cos420.robotrally.R;


// TODO javadoc
public class MoveAdapter extends RecyclerView.Adapter<MoveAdapter.MyViewHolder> {

    // TODO javadoc
    private List<MoveItem> list;
    MoveListener listener;

    // TODO javadoc
    //Constructor
    public MoveAdapter(List<MoveItem> list, MoveListener listener) {
        this.list = list;
        this.listener = listener;
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
        holder.blink.setBackground(moveItem.getColor());

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // TODO javadoc
    /**
     * Interface to interact with listener in main class
     */
    public interface MoveListener {
        void onMoveClick(int position);
    }


    //ViewHolder class
    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout move;
        Button move_view;
        TextView blink;

        // TODO javadoc
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            move = itemView.findViewById(R.id.move);
            move_view = itemView.findViewById(R.id.move_view);
            blink = itemView.findViewById(R.id.blink);
        }

        /**
         * Set listener
         * @param position
         */
        public void bind(int position) {
            move_view.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onMoveClick(adapterPosition);
                }
            });
        }
    }
}
