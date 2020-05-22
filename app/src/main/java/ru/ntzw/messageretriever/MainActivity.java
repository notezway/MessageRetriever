package ru.ntzw.messageretriever;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SimpleItemSwipeRemover(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
}
