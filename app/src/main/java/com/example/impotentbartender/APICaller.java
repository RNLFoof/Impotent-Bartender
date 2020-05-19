package com.example.impotentbartender;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class APICaller
{
    static void create(final Context context, String input)
    {
        String base = "https://www.thecocktaildb.com/api/json/v1/1/";
        String url = base + input;
        JSONObject jason = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget= new HttpGet(url);

        HttpResponse response;
        try
        {
            response = httpclient.execute(httpget);

            if(response.getStatusLine().getStatusCode()==200)
            {
                String server_response = EntityUtils.toString(response.getEntity());
                jason = new JSONObject(server_response);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jason != null)
        {
            Toast.makeText(context, "YAY", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "FUCK, OW", Toast.LENGTH_SHORT).show();
        }
    }
}
