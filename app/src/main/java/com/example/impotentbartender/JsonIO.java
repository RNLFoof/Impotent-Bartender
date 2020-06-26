package com.example.impotentbartender;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

public class JsonIO {
    public static JSONObject load(Context context, String fileName)
    {
        fileName = filenameConvert(fileName);
        Log.d("fuck17", fileName);
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            Log.d("fuck13", "1");
            fis.close();
            return new JSONObject(sb.toString());
        } catch (FileNotFoundException fileNotFound) {
            Log.d("fuck14", "2");
            StringWriter sw = new StringWriter();
            fileNotFound.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.d("fuck", exceptionAsString);
            return new JSONObject();
        } catch (IOException ioException) {
            Log.d("fuck15", "3");
            return new JSONObject();
        } catch (JSONException e) {
            Log.d("fuck16", "4");
            return new JSONObject();
        }
    }

    public static JSONObject load(Context context, int resource)
    {
        try {
            BufferedInputStream fis = new BufferedInputStream(context.getResources().openRawResource(resource));
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            Log.d("fuck13", "1");
            return new JSONObject(sb.toString());
        } catch (FileNotFoundException fileNotFound) {
            Log.d("fuck14", "2");
            return new JSONObject();
        } catch (IOException ioException) {
            Log.d("fuck15", "3");
            return new JSONObject();
        } catch (JSONException e) {
            Log.d("fuck16", "4");
            return new JSONObject();
        }
    }

    public static JSONArray loadArray(Context context, int resource)
    {
        try {
            BufferedInputStream fis = new BufferedInputStream(context.getResources().openRawResource(resource));
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            Log.d("fuck13", "1");
            return new JSONArray(sb.toString());
        } catch (FileNotFoundException fileNotFound) {
            Log.d("fuck14", "2");
            return new JSONArray();
        } catch (IOException ioException) {
            Log.d("fuck15", "3");
            return new JSONArray();
        } catch (JSONException e) {
            Log.d("fuck16", "4");
            return new JSONArray();
        }
    }

    public static boolean save(Context context, String fileName, String jsonString)
    {
        fileName = filenameConvert(fileName);

        try {
            JSONObject o = load(context, fileName);
            JSONObject n = new JSONObject(jsonString);
            Iterator<String> keys = o.keys();
            while (keys.hasNext())
            {
                // Add to new only if it's not there already. Essentially only add new values as to not delete your own changes
                String k = keys.next();
                if (!n.has(k))
                {
                    n.put(k, o.get(k));
                }
            }

            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            fos.write(n.toString().getBytes());
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    static String filenameConvert(String fileName)
    {
        return fileName.toLowerCase().replace("?", "#").replace(".json", "") + ".json";
    }
}
