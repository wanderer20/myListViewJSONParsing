package learn2crack.listview;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



import learn2crack.listview.library.JSONParser;



public class MainActivity extends Activity {
	ListView list;
	TextView ver;
	TextView name;
	TextView api;
	private static String IMG_URL = "http://openweathermap.org/img/w/";
	Button Btngetdata;
	ArrayList<HashMap<String, Object>> oslist = new ArrayList<HashMap<String, Object>>();
	
	//URL to get JSON Array
	private static String url = "http://api.openweathermap.org/data/2.5/find?lat=55.5&lon=37.5&cnt=20";
	
	//JSON Node Names 
	private static final String TAG_OS = "android";
	private static final String TAG_NAME = "name";
	private static final String TAG_MAIN = "main";
	private static final String TAG_TEMP = "temp";
	private static final String TAG_ICON = "icon";

	
	JSONArray android = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.activity_main);
        oslist = new ArrayList<HashMap<String, Object>>();

        
        
        Btngetdata = (Button)findViewById(R.id.getdata);
        Btngetdata.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
		         new JSONParse().execute();

				
			}
		});
        
        
    }

    
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    
    Bitmap drawable_from_url(String url) throws java.net.MalformedURLException, java.io.IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection)new URL(url) .openConnection();
        connection.setRequestProperty("User-agent","Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return x;
    }
    
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
    	 private ProgressDialog pDialog;
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
             ver = (TextView)findViewById(R.id.vers);
			 name = (TextView)findViewById(R.id.name);
			 api = (TextView)findViewById(R.id.api);
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            
            
            
    	}
    	public byte[] getImage(String code) {
            HttpURLConnection con = null ;
            InputStream is = null;
            try {
                con = (HttpURLConnection) ( new URL(IMG_URL + code)).openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();
                 
                // Let's read the response
                is = con.getInputStream();
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 
                while ( is.read(buffer) != -1)
                    baos.write(buffer);
                 
                return baos.toByteArray();
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            finally {
                try { is.close(); } catch(Throwable t) {}
                try { con.disconnect(); } catch(Throwable t) {}
            }
             
            return null;
             
        }
    	
    	
    	@Override
        protected JSONObject doInBackground(String... args) {
    		
    		JSONParser jParser = new JSONParser();

    		// Getting JSON from URL
    		JSONObject json = jParser.getJSONFromUrl(url);
    		return json;
    	}
    	 @Override
         protected void onPostExecute(JSONObject json) {
    		 pDialog.dismiss();
    		 try {
    				// Getting JSON Array from URL
    				android = json.getJSONArray("list");
    				for(int i = 0; i < android.length(); i++){
    				JSONObject c = android.getJSONObject(i);
    				JSONObject m = c.getJSONObject("main");
    				JSONArray w = c.getJSONArray("weather");
    				JSONObject wi = w.getJSONObject(0);
    				
    				// Storing  JSON item in a Variable
    				
    				String name = c.getString("name");
    				
    				String temp = m.getString("temp");
    				
    			
    				String main = wi.getString("main");
    				String icon = wi.getString("icon");
    		
    				
    				

    				// Adding value HashMap key => value
    				


    				HashMap<String, Object> map = new HashMap<String, Object>();

    				
    				map.put(TAG_NAME, name);
    				map.put(TAG_TEMP, temp);
    				map.put(TAG_MAIN, main);

    				
    				oslist.add(map);
    				list=(ListView)findViewById(R.id.list);
    				
    				
    				
    		        
    				//ListAdapter
    				ListAdapter adapter = new SimpleAdapter(MainActivity.this, oslist,
    						R.layout.list_v,
    						new String[] { TAG_NAME,TAG_TEMP, TAG_MAIN }, new int[] {
    								R.id.vers,R.id.name, R.id.api});

    				list.setAdapter(adapter);
    				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

    		            @Override
    		            public void onItemClick(AdapterView<?> parent, View view,
    		                                    int position, long id) {
    		                Toast.makeText(MainActivity.this, "You Clicked at "+oslist.get(+position).get("name"), Toast.LENGTH_SHORT).show();

    		            }
    		        });

    				}
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}

    		 
    	 }
    }
    
}
