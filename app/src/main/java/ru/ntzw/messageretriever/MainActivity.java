package ru.ntzw.messageretriever;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rv_messages_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AdapterCreateTask task = new AdapterCreateTask();
        task.execute(this);
        MessageAdapter adapter = null;
        try {
            adapter = task.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
            finish();
        }
        if(adapter == null) {
            return; //should never happen :)
        }

        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SimpleItemSwipeRemover(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.registerAdapterDataObserver(new AdapterOnChangedEventListener(this, adapter));
    }

    static class AdapterCreateTask extends AsyncTask<Context, Void, MessageAdapter> {
        @Override
        protected MessageAdapter doInBackground(Context... contexts) {
            DataBlockProvider<Message> messageProvider = new MessageBlockProvider();
            DataProvider<Message> perElementProvider = new CachedPerElementBlockDataProvider<>(messageProvider);
            DataProvider<Message> ringBufferedProvider = new RingBufferedDataProvider<>(perElementProvider, 4000);
            ringBufferedProvider.get(0);
            return new MessageAdapter(contexts[0], perElementProvider);
        }
    }

    static class AdapterOnChangedEventListener extends RecyclerView.AdapterDataObserver {

        private final AppCompatActivity activity;
        private final MessageAdapter adapter;

        private AdapterOnChangedEventListener(AppCompatActivity activity, MessageAdapter adapter) {
            this.activity = activity;
            this.adapter = adapter;
            updateTitle();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            updateTitle();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            updateTitle();
        }

        @SuppressLint("DefaultLocale")
        private void updateTitle() {
            String appName = activity.getResources().getString(R.string.app_name);
            activity.setTitle(String.format("%s [%d]", appName, adapter.getItemCount()));
        }
    }
}
