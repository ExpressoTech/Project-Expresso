package com.expresso.adapter.UserProfileAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.expresso.activity.R;
import com.expresso.activity.UserProfileActivities.ImageFullScreen;
import com.expresso.activity.UserProfileActivities.UserProfileActivity;
import com.expresso.database.DatabaseHelper;
import com.expresso.model.UserProfileModel.Following;
import com.expresso.utils.Constant;
import com.expresso.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohit on 17/7/15.
 */
public class FollowerAdapter  extends ArrayAdapter<Following> {
    private String friend_id;
    private String f_id= UserProfileActivity.f_user_id;
    private String u_id=UserProfileActivity.USER_ID;
    private final Activity activity;
    private final String UNFOLLOW_FLAG="0";
    private final String FOLLOW_FLAG="1";
    private static class ViewHolder
    {
        TextView tv_username;
        ImageView iv_userImage;
        Button btn_unfollow;

    }
    Context context;
    public FollowerAdapter(Activity activity, ArrayList<Following> arr_deals) {
        super(activity, R.layout.feed_layout_row, arr_deals);
        this.activity=activity;
        //db=new DatabaseHelper(activity);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Following item = getItem(position);
        final ViewHolder holder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_display, parent, false);
            holder.tv_username= (TextView) convertView.findViewById(R.id.followersName);
            holder.iv_userImage= (ImageView) convertView.findViewById(R.id.followingimageview);
            holder.btn_unfollow=(Button)convertView.findViewById(R.id.unfollow);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        //set text of the button
         if(item.getFollowing_status().equalsIgnoreCase("false"))
            {
                holder.btn_unfollow.setText("Follow");
            }
            else
            {
                holder.btn_unfollow.setText("Unfollow");
            }




        holder.tv_username.setText(item.getUser_name());//populate Username in list

        if(String.valueOf(item.getUser_id()).equalsIgnoreCase(u_id))// make button invisible for the USERPROFILE
        {holder.btn_unfollow.setVisibility(View.GONE);}
        else
        {

            holder.btn_unfollow.setVisibility(View.VISIBLE);
        }



//        holder.follower_status=item.getFollowing_status();
//        if(holder.follower_status.equals("false"))
//        {
//            holder.btn_unfollow.setText("Follow");
//        }
//        else
//        {
//            holder.btn_unfollow.setText("Unfollow");
//
//        }

/*        holder.tv_feedcomment.setText(item.getFeed_comments());*/
        Log.e("Image URL:", item.getUser_avatar().toString());

        Glide.with(activity)
                .load(item.getUser_avatar())
                .centerCrop()
                .placeholder(R.drawable.user)
                .crossFade()
                .into(holder.iv_userImage);




        holder.btn_unfollow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String status = (String) holder.btn_unfollow.getText();
                if (status.equalsIgnoreCase("Follow")) {
                    friend_id = String.valueOf(item.getUser_id());
                    new Follow().execute();

                    holder.btn_unfollow.setText("Unfollow");
                } else {
                        friend_id = String.valueOf(item.getUser_id());

                        new UnFollow().execute();
                        holder.btn_unfollow.setText("Follow");
                }




            }
        });
        holder.iv_userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getContext(),ImageFullScreen.class);
                intent.putExtra("param_a", item.getUser_avatar());
                activity.startActivity(intent);


            }
        });
        holder.tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                intent.putExtra(Constant.USERID, String.valueOf(item.getUser_id()));
                activity.startActivity(intent);


            }
        });
        return convertView;
    }




    private class UnFollow extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
//            String p=u_id;
//            Log.d("Following friend_id", friend_id);
//            String p2=friend_id
// String flag="0";
            String mode ="email";
            JSONParser jsonParser = new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(Constant.USERID, u_id));
            parameters.add(new BasicNameValuePair(Constant.FRIENDSID,friend_id));
            parameters.add(new BasicNameValuePair(Constant.MODE,mode));
            parameters.add(new BasicNameValuePair(Constant.STATUS_FLAG, UNFOLLOW_FLAG));
            JSONObject json = jsonParser.getJSONFromUrl(Constant.FOLLOWUSER, parameters);


            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {



            Log.d("LISTVIEW::UNFOLLOW:", String.valueOf(result));

            try {
//            if(place_name.size()>0 ||locations_markers.size()>0||date_of_vist.size()>0)
//         .d   {
//                place_name.clear();
//                date_of_vist.clear();
//                month_of_visit.clear();
//                year_of_vist.clear();
//                locations_markers.clear();
//            }

                if(result.getString("status").equalsIgnoreCase("Success"))
                {
                    Toast.makeText(getContext(), result.getString("status"), Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }



        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
    private class Follow extends AsyncTask<String, Void, JSONObject> {
        ;
        @Override
        protected JSONObject doInBackground(String... params) {
//            String p=u_id;
//            Log.d("Following friend_id", u_id);
//            String p2=friend_id;
//            String p1="1";
            String mode ="email";
            JSONParser jsonParser = new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(Constant.USERID, u_id));
            parameters.add(new BasicNameValuePair(Constant.FRIENDSID,friend_id));
            parameters.add(new BasicNameValuePair(Constant.MODE,mode));
            parameters.add(new BasicNameValuePair(Constant.PROFILEFLAG, FOLLOW_FLAG));
            JSONObject json = jsonParser.getJSONFromUrl(Constant.FOLLOWUSER, parameters);


            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {



            Log.d("LISTVIEW::FOLLOW:", String.valueOf(result));

            try {
//            if(place_name.size()>0 ||locations_markers.size()>0||date_of_vist.size()>0)
//         .d   {
//                place_name.clear();
//                date_of_vist.clear();
//                month_of_visit.clear();
//                year_of_vist.clear();
//                locations_markers.clear();
//            }

                if(result.getString("status").equalsIgnoreCase("Success"))
                {
                    Toast.makeText(getContext(), result.getString("status"), Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }



        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}


