package ru.ntzw.messageretriever;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private final LayoutInflater inflater;
    private final List<Message> messages;
    private final DateFormat dateFormat = DateFormat.getDateTimeInstance();

    MessageAdapter(Context context, List<Message> messages) {
        this.inflater = LayoutInflater.from(context);
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.getTimeView().setText(dateFormat.format(new Date(message.getTime())));
        holder.getTextView().setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    void removeItem(int position) {
        messages.remove(position);
        notifyItemRemoved(position);
    }
}
