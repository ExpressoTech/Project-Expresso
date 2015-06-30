package com.expresso.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class JSONParser
{
	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
    private String response_str;

    // constructor
	public JSONParser() {
	}

	public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);//iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			Log.e("JSON", json);
		}
		catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);			
		}
		catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;
	}
	public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {

		// Making HTTP request
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			String paramString = URLEncodedUtils.format(params, "utf-8");
			url += "?" + paramString;
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			Log.e("JSON", json);
		}
		catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		}
		catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;
	}

    public JSONObject getJSONWithImageUpload(String url, List<NameValuePair> params,String fileurl)
    {
        HttpEntity resEntity;
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        MultipartEntity reqEntity = new MultipartEntity();
        JSONObject json=null;
        for(int i=0;i<params.size();i++)
        {
            try {
                reqEntity.addPart(params.get(i).getName(), new StringBody(params.get(i).getValue()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        File file = new File(fileurl);
        FileBody bin = new FileBody(file);
        reqEntity.addPart(Constant.AVATAR, bin);

        try {
        post.setEntity(reqEntity);
        HttpResponse response = null;

            response = client.execute(post);

        resEntity = response.getEntity();
        response_str = EntityUtils.toString(resEntity);
        if (resEntity != null) {
            Log.i("RESPONSE", response_str);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
             json= new JSONObject(response_str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;

    }


}