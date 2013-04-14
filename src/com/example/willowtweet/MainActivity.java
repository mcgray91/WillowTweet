package com.example.willowtweet;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ListActivity{
    
    private ProgressDialog m_ProgressDialog = null; 
    private ArrayList<Order> m_orders = null;
    private OrderAdapter m_adapter;
    private Runnable viewOrders;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_orders = new ArrayList<Order>();
        this.m_adapter = new OrderAdapter(this, R.layout.row, m_orders);
        setListAdapter(this.m_adapter);
        
      
        new AsyncTest().execute();
      
        m_ProgressDialog = ProgressDialog.show(MainActivity.this,    
              "Please wait...", "Retrieving data ...", true);
    }


       
    private class AsyncTest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
        	try {
                // gets Twitter instance with default credentials
            	ConfigurationBuilder cb = new ConfigurationBuilder();
            	cb.setDebugEnabled(true)
            	  .setOAuthConsumerKey("5g0ohOlHQulDQSiqGGEaiQ")
            	  .setOAuthConsumerSecret("GSJW7lybDBCr0IaHzAFLWrbaQjIEacN4vho58aPns")
            	  .setOAuthAccessToken("391075607-T1HUenmic29Bx9gCT8pfsbo41ve104fNfQBEQErM")
            	  .setOAuthAccessTokenSecret("xPVu43tujVnqMJN53r2BsFdAYowTDmyMAujt76eln0k");
            	TwitterFactory tf = new TwitterFactory(cb.build());
            	Twitter twitter = tf.getInstance();
                String user = "MattGray9";
                List<twitter4j.Status> statuses = twitter.getUserTimeline(user);
                Order o1;
                for (twitter4j.Status status : statuses) {
                   	if(status.isRetweet()){
                		System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText() + " -- " + status.getUser().getProfileImageURL());
                    	o1 = new Order(status.getCurrentUserRetweetId(), status.getUser().getName(), status.getText(), status.getCreatedAt(), status.getUser().getProfileImageURL());
                	}else{
                		System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText()); 
                		o1 = new Order(status.getId(), status.getUser().getName(), status.getText(), status.getCreatedAt(), status.getUser().getProfileImageURL());
                	}
                    m_orders.add(o1);
                }
            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println("Failed to get timeline: " + te.getMessage());
                System.exit(-1);
            }
        	return null;
        }      

        @Override
        protected void onPostExecute(String result) {
            if(m_orders != null && m_orders.size() > 0){
            	m_adapter.setItems(m_orders);
                m_adapter.notifyDataSetChanged();
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }   
    
    private class OrderAdapter extends ArrayAdapter<Order> {

        private ArrayList<Order> items;

        public OrderAdapter(Context context, int textViewResourceId, ArrayList<Order> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        
        public void setItems(ArrayList<Order> items) {
        	this.items = items;
        }
        
        public int getCount(){
        	return this.items == null ? 0 : this.items.size();
        }
        
        public long getItemId(final int position){
        	return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);
                }
                Order o = items.get(position);
                if (o != null) {
                        TextView tt = (TextView) v.findViewById(R.id.toptext);
                        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                        TextView ts = (TextView) v.findViewById(R.id.timetext);
                        ImageView up = (ImageView) v.findViewById(R.id.icon);
                        if (tt != null) {
                              tt.setText(o.getOrderName());                            }
                        if(bt != null){
                              bt.setText(o.getOrderStatus());
                        }
                        if(ts != null){
                        	  ts.setText(o.getOrderTime().toString());
                        }
                        if(up != null){
                           	try
                            {
                                InputStream is = (InputStream) new URL(o.getOrderPic()).getContent();
                                Drawable d = Drawable.createFromStream(is, "src name");
                                up.setImageDrawable(d);
                            }catch (Exception e) {
                                System.out.println("Exc="+e);
                                return null;
                            }
                        }
                }
                return v;
        }
    }
}
