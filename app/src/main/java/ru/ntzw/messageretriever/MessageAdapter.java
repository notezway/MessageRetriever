package ru.ntzw.messageretriever;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;

class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private static final DateFormat dateFormat = DateFormat.getDateTimeInstance();
    private final LayoutInflater inflater;
    private final FilteredDataProvider<Message> messageProvider;

    MessageAdapter(Context context, DataProvider<Message> messageProvider) {
        this.inflater = LayoutInflater.from(context);
        this.messageProvider = new FilteredDataProvider<>(messageProvider);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageRetrieveTask task = new MessageRetrieveTask(messageProvider, this, holder);
        task.execute(position);
    }

    @Override
    public int getItemCount() {
        return messageProvider.size();
    }

    void removeItem(int position) {
        MessageRemoveTask task = new MessageRemoveTask(messageProvider, this);
        task.execute(position);
    }

    static class MessageRetrieveTask extends AsyncTask<Integer, Boolean, Message> {

        private final DataProvider<Message> messageProvider;
        private final MessageAdapter messageAdapter;
        private final MessageViewHolder holder;
        private int dataSetSizeBefore;

        private MessageRetrieveTask(DataProvider<Message> messageProvider, MessageAdapter messageAdapter, MessageViewHolder holder) {
            this.messageProvider = messageProvider;
            this.messageAdapter = messageAdapter;
            this.holder = holder;
        }

        @Override
        protected Message doInBackground(Integer... integers) {
            synchronized (messageProvider) {
                this.dataSetSizeBefore = messageProvider.size();
                int index = integers[0];
                return messageProvider.get(index);
            }
        }

        @Override
        protected void onPostExecute(Message message) {
            super.onPostExecute(message);
            synchronized (messageAdapter) {
                holder.getIdView().setText(message.getId().toString());
                holder.getTimeView().setText(dateFormat.format(new Date(message.getTime())));
                holder.getTextView().setText(message.getText());
                synchronized (messageProvider) {
                    int dataSetSizeAfter = messageProvider.size();
                    if (dataSetSizeAfter != dataSetSizeBefore) {
                        int size = dataSetSizeAfter - dataSetSizeBefore;
                        messageAdapter.notifyItemRangeChanged(dataSetSizeBefore, size);
                    }
                }
            }
        }
    }

    static class MessageRemoveTask extends AsyncTask<Integer, Void, Void> {

        private final FilteredDataProvider<Message> messageProvider;
        private final MessageAdapter messageAdapter;
        private volatile int index;

        MessageRemoveTask(FilteredDataProvider<Message> messageProvider, MessageAdapter messageAdapter) {
            this.messageProvider = messageProvider;
            this.messageAdapter = messageAdapter;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            index = integers[0];
            synchronized (messageProvider) {
                messageProvider.removeIndex(index);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            synchronized (messageAdapter) {
                messageAdapter.notifyItemRemoved(index);
            }
        }
    }
}
