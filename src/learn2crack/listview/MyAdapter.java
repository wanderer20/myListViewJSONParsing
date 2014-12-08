package learn2crack.listview;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

// here's our beautiful adapter
public class MyAdapter extends SimpleAdapter {

	Context mContext;
    int layoutResourceId;
    String[] mfrom;
    int[] mto;
    private static String IMG_URL = "http://openweathermap.org/img/w/";
    List<? extends Map<String, ?>> ddata = null;
    
    public MyAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		
		 mContext = context;
		 ddata = data;
		 layoutResourceId = resource;
		 mfrom = from;
		 mto = to;
		// TODO Auto-generated constructor stub
	}



	

   

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /*
         * The convertView argument is essentially a "ScrapView" as described is Lucas post 
         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
         * It will have a non-null value when ListView is asking you recycle the row layout. 
         * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
         */
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        if(position != 3)
        {
        	TextView tx = (TextView) convertView.findViewById(mto[position]);
        	tx.setText(ddata.get(position).toString());
        }
        if(position == 3)
        {
        	ImageView im = (ImageView) convertView.findViewById(mto[position]);
        	new DownloadImageTask((ImageView) im).execute(IMG_URL + ddata.get(position).toString() + ".png");
			
        	
        }
        // object item based on the position
        //Item objectItem = data[position];

        // get the TextView and then set the text (item name) and tag (item ID) values
        //TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
        //textViewItem.setText(objectItem.itemName);
        //textViewItem.setTag(objectItem.itemId);

        return convertView;

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

}