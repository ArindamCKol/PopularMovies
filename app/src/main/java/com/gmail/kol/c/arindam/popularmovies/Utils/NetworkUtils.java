package com.gmail.kol.c.arindam.popularmovies.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public final class NetworkUtils {

    //Error massage tag
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    //time out value in milliseconds
    private static final int READ_TIMEOUT_VALUE = 10000;
    private static final int CONNECT_TIMEOUT_VALUE = 15000;

    //blank constructor for final class
    public NetworkUtils() { }

    //convert string to url
    public static URL createUrl(String urlText) {
        URL url = null;
        try {
            url = new URL(urlText);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "URL not correct ", e);
        }
        return url;
    }

    //get json string from url
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // if url is null return blank json string
        if (url == null) {
            return jsonResponse;
        }

        //make network connection
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT_VALUE);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT_VALUE);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If response code 200, success. create json string.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Unable to access json result.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //convert url response to string
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
