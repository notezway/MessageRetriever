package ru.ntzw.messageretriever;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

class MessageBlockProvider implements DataBlockProvider<Message> {

    private final static String uriTemplate = "http://messageretriver.ntzw.ru/endpoint/%d.json";

    @Override
    public List<Message> get(int index) {
        return loadBlock(index);
    }

    private static List<Message> loadBlock(int blockIndex) {
        URL url;
        try {
            url = getURL(blockIndex);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        JSONArray json;
        try {
            json = readAsJsonArray(getConnection(url));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return parseJson(json);
    }

    private static URL getURL(int blockIndex) throws MalformedURLException {
        String uriString = String.format(Locale.getDefault(), uriTemplate, blockIndex);
        Uri uri = Uri.parse(uriString);
        return new URL(uri.toString());
    }

    private static HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Accept", "application/octet-stream");
        return connection;
    }

    private static JSONArray readAsJsonArray(HttpURLConnection connection) throws IOException, JSONException {
        InputStream inputStream = connection.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(inputStream.available());
        int b;
        while ((b = inputStream.read()) != -1) {
            buffer.write(b);
        }
        return new JSONArray(buffer.toString("utf-8"));
    }

    private static List<Message> parseJson(JSONArray json) {
        List<Message> block = new ArrayList<>();
        JSONObject object;
        int index = 0;
        while ((object = json.optJSONObject(index++)) != null) {
            try {
                UUID id = UUID.fromString(object.getString("id"));
                long time = object.getLong("time");
                String text = object.getString("text");
                block.add(new Message(id, time, text));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return block;
    }
}
