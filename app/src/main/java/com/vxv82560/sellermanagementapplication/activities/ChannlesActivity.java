package com.vxv82560.sellermanagementapplication.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vxv82560.sellermanagementapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author veenapaniv
 */
public class ChannlesActivity extends AppCompatActivity {

    ListView channelsList;
    ArrayList<String> userChannels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channles);

        //Get the custom toolbar and display
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get the list defined in the layout file
        channelsList = findViewById(R.id.channelsList);
        //define an arraylist as dataSource
        userChannels = new ArrayList<String>();
        try{
            //Create a json object and load the json file in the assets folder
            JSONObject assetJSON = new JSONObject(loadFromAssets());
            //get the array "channels_per_user" from the json file
            JSONArray channelsArray = assetJSON.getJSONArray("channels_per_user");
            //get the size of the array
            int size = channelsArray.length();
            //iterate through the array
            for(int i=0;i<size;i++){
                //the json file has another array which has channel names for each user
                JSONObject channel = channelsArray.getJSONObject(i);
                int userId = getIntent().getIntExtra("User_Id",0);
                int jsonUserID = Integer.parseInt(channel.getString("user_id"));
                //match json's userId with the logged in userId and load the channels for each user
                if(jsonUserID == userId){
                    JSONArray channelsInfoArray = channel.getJSONArray("channels");

                    for(int j=0;j<channelsInfoArray.length();j++){
                        JSONObject channelName = channelsInfoArray.getJSONObject(j);
                        userChannels.add(channelName.getString("name"));
                    }

                }
                break;

            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        //Ingest the ArrayList in a ArrayAdapter and map it to the layout
        ArrayAdapter ad = new ArrayAdapter(ChannlesActivity.this, android.R.layout.simple_list_item_1, userChannels);
        //set the adapter to the list element in the layout file.
        channelsList.setAdapter(ad);

    }

    private String loadFromAssets() {
        String jsonString = null;
        try{
            //Read the json file from assets folder
            InputStream io = getAssets().open("myJSON.json");
            //Returns an estimate of the number of bytes that can be read (or skipped over) from this input stream without blocking by the next invocation of a method for this input stream.
            int size = io.available();
            //set the size of the buffer
            byte[] buffer = new byte[size];
            //read the json file
            io.read(buffer);
            io.close();
            jsonString = new String(buffer,"UTF-8");
        }catch (IOException e){
            e.printStackTrace();
        }
        //return the json string
        return jsonString;
    }
}
