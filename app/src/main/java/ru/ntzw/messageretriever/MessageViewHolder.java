package ru.ntzw.messageretriever;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MessageViewHolder extends RecyclerView.ViewHolder {

    private final TextView idView;
    private final TextView timeView;
    private final TextView textView;

    MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        this.idView = itemView.findViewById(R.id.tv_id);
        this.timeView = itemView.findViewById(R.id.tv_time);
        this.textView = itemView.findViewById(R.id.tv_text);
    }

    TextView getIdView() {
        return idView;
    }

    TextView getTimeView() {
        return timeView;
    }

    TextView getTextView() {
        return textView;
    }
}
