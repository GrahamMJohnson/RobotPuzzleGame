package cos420.robotrally.adaptersAndItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cos420.robotrally.R;

public class AttemptAdapter extends RecyclerView.Adapter<AttemptAdapter.AttemptViewHolder> {
    private List<AttemptItem> list;
    private AttemptListener listener;
    private final Context context;

    /**
     * Constructor
     * @param list
     * @param listener
     * @param context
     */
    public AttemptAdapter(List<AttemptItem> list, AttemptListener listener, Context context) {
        this.list = list;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public AttemptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.attempt_info, parent, false);
        return new AttemptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttemptViewHolder holder, int position) {
        AttemptItem attemptItem = list.get(position);
        holder.attemptNumber.setText(String.valueOf(attemptItem.getAttemptNumber()));
        holder.numberOfMoves.setText(String.valueOf(attemptItem.getNumberOfMoves()));
        holder.success.setText(attemptItem.getSuccess());
        holder.success.setTextColor(ContextCompat.getColor(context,attemptItem.getSuccessColor()));

        holder.bind();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface AttemptListener {
        void onClick(int position);
    }

    //ViewHolder class
    public class AttemptViewHolder extends RecyclerView.ViewHolder {
        LinearLayout attemptButton;
        TextView attemptNumber;
        TextView numberOfMoves;
        TextView success;


        public AttemptViewHolder(@NonNull View itemView) {
            super(itemView);
            attemptButton = itemView.findViewById(R.id.attempt_button);
            attemptNumber = itemView.findViewById(R.id.attempt_number);
            numberOfMoves = itemView.findViewById(R.id.move_number);
            success = itemView.findViewById(R.id.success_or_failure);
        }

        public void bind() {
            attemptButton.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onClick(adapterPosition);
                }
            });
        }
    }
}
