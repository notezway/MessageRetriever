package ru.ntzw.messageretriever;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_messages_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        Random random = new Random();
        int capacity = 128;
        List<Message> messages = new ArrayList<>(capacity);
        for(int i = 0; i < capacity; i++) {
            messages.add(randomMessage(random));
        }
        MessageAdapter adapter = new MessageAdapter(this, messages);
        recyclerView.setAdapter(adapter);
    }

    private static Message randomMessage(Random random) {
        return new Message(UUID.randomUUID(), System.currentTimeMillis() - random.nextInt(1000 * 60 * 60 * 24), new BigInteger(1024, random).toString(16));
    }
}
