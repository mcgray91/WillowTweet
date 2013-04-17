package com.example.willowtweet;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.TimeSpanConverter;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class MainActivity extends ListActivity{
    
    private ProgressDialog m_ProgressDialog = null; 
    private ArrayList<Order> m_orders = null;
    private ArrayList<twitUser> m_Users = null;
    private OrderAdapter m_adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_orders = new ArrayList<Order>();
        m_Users = new ArrayList<twitUser>();
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
                Bitmap bitmap = null;
                String user = "bsirach";
                List<twitter4j.Status> statuses = twitter.getUserTimeline(user);
                User twitUser = twitter.showUser(user);
        		try {
            		  bitmap = BitmapFactory.decodeStream((InputStream)new URL(twitUser.getBiggerProfileImageURL()).getContent());
            		} catch (MalformedURLException e) {
            		  e.printStackTrace();
            		} catch (IOException e) {
            		  e.printStackTrace();
            		}
        		twitUser curUser = new twitUser(twitUser.getScreenName(), twitUser.getFriendsCount(), twitUser.getFollowersCount(), twitUser.getStatusesCount(), bitmap);
                m_Users.add(curUser);
        		Order o1;
                for (twitter4j.Status status : statuses) {
                   	if(status.isRetweet()){
                		System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText() + " -- " + status.getUser().getProfileImageURL());
                		try {
                  		  bitmap = BitmapFactory.decodeStream((InputStream)new URL(status.getUser().getBiggerProfileImageURL()).getContent());
                  		} catch (MalformedURLException e) {
                  		  e.printStackTrace();
                  		} catch (IOException e) {
                  		  e.printStackTrace();
                  		}
                    	o1 = new Order(status.getCurrentUserRetweetId(), status.getUser().getName(), status.getText(), status.getCreatedAt(), bitmap);
                	}else{
                		System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText()); 	
                		try {
                			bitmap = BitmapFactory.decodeStream((InputStream)new URL(status.getUser().getBiggerProfileImageURL()).getContent());
                  		} catch (MalformedURLException e) {
                  		  e.printStackTrace();
                  		} catch (IOException e) {
                  		  e.printStackTrace();
                  		}
                		o1 = new Order(status.getId(), status.getUser().getName(), status.getText(), status.getCreatedAt(), bitmap);
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
        	setUserData(m_Users);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }   
    
    private void setUserData(ArrayList<twitUser> users){
    	twitUser inUser = users.get(0);
        ImageView userPic = (ImageView) findViewById(R.id.user_Icon);
        TextView following = (TextView) findViewById(R.id.following);
        TextView followers = (TextView) findViewById(R.id.followers);
        TextView tweets = (TextView) findViewById(R.id.tweets);
        System.out.println("User checking: " + inUser.getUserName());
        if(userPic != null){
            userPic.setImageBitmap(inUser.getUserPic());
        } 
        if(following != null){
        	String first = inUser.getUserFollowing();
        	String next = " Following";
        	following.setText(first + next, BufferType.SPANNABLE);
	        Spannable s = (Spannable)following.getText();
	        int start = 0;
	        int end = first.length();
	        s.setSpan(new ForegroundColorSpan(Color.rgb(153, 217, 234)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(followers != null){
        	String first = inUser.getUserFollowers();
        	String next = " Followers";
        	followers.setText(first + next, BufferType.SPANNABLE);
	        Spannable s = (Spannable)followers.getText();
	        int start = 0;
	        int end = first.length();
	        s.setSpan(new ForegroundColorSpan(Color.rgb(153, 217, 234)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(tweets != null){
        	tweets.setTextColor(Color.rgb(153, 217, 234));
        	tweets.setText(" " + inUser.getUserTweets() + " TWEETS");
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
                TimeSpanConverter tSpan = new TimeSpanConverter();
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
                        	  System.out.println(tSpan.toTimeSpanString(o.getOrderTime()));
                        	  ts.setText(tSpan.toTimeSpanString(o.getOrderTime()));
                        }
                        if(up != null){
	                        up.setImageBitmap(o.getOrderPic());
                        }
                }
                return v;
        }
    }
}
