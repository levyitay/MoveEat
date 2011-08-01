package com.Rest2Go;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurantActivity extends ActivityWrapper {

	private long mRestId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();

		long restId = intent.getLongExtra(Consts.REST_ID, -1);
		if (restId >= 0) {
			mRestId = restId;
			getRestDetails();
		} else {
			finish();
		}

	}

	private void getRestDetails() {
		new AsyncTask<Void, String, String>() {
			@Override
			protected void onPreExecute() {
				RestaurantActivity.this.showLoading(this);
			}

			@Override
			protected String doInBackground(Void... arg0) {
				return (Utils.preformRequest(Utils.BASE_URL+"?id="
						+ mRestId));
			}

			@Override
			protected void onPostExecute(String body) {
				Utils.log(body);
				if (body != null) {
					JSONObject obj = null;
					try {
						obj = new JSONObject(body);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (obj != null) {
						JSONArray arr = obj.optJSONArray("restaurants");
						displayRestDetails(arr);
					}
					removeDialog(ProgressDialog.STYLE_HORIZONTAL);
					
				}
			}

		}.execute();

	}

	protected void displayRestDetails(JSONArray arr) {
		setContentView(R.layout.restaurant_layout);
		for (int i = 0; i <= arr.length() - 1; i++) {
			JSONObject rest = arr.optJSONObject(i);
			String phone2 = "";
			String imageUrl = null;
			BitmapDrawable iconDraw = null;
			BitmapDrawable imgDraw = null;
			long id = rest.optLong(Consts.DB_COLUMNS.ID);
			String name = rest.optString(Consts.DB_COLUMNS.NAME);
			String address = rest.optString(Consts.DB_COLUMNS.ADDRESS);
			String phone = rest.optString(Consts.DB_COLUMNS.PHONE);
			if (rest.optString(Consts.DB_COLUMNS.SECONDARY_PHONE) != null)
				phone2 = rest.optString(Consts.DB_COLUMNS.SECONDARY_PHONE);
			if (rest.optString(Consts.DB_COLUMNS.IMAGE_URL) != null) {
				imageUrl = rest.optString(Consts.DB_COLUMNS.IMAGE_URL);
			}
			if (TextUtils.isEmpty(imageUrl) || !imageUrl.equals("null")) {
				imgDraw = LocationActivity.getDrawableFromURL(imageUrl);
			}
			TextView restName = (TextView) findViewById(R.id.txtRestName);
			TextView restAddress = (TextView) findViewById(R.id.txtRestAddress);
			TextView restPhone = (TextView) findViewById(R.id.txtRestPhone);
			TextView restPhone2 = (TextView) findViewById(R.id.txtRestPhone2);
			ImageView restImage = (ImageView) findViewById(R.id.restImg);

			restName.setText(name);
			restAddress.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("google.navigation:q="+((TextView)v).getText()));
					startActivity(intent);
				}
			});
			restAddress.setText(address);
			restPhone.setText(phone);
			Linkify.addLinks(restPhone, Linkify.PHONE_NUMBERS);
			Linkify.addLinks(restPhone2, Linkify.PHONE_NUMBERS);
			if (!TextUtils.isEmpty(phone2)) {
				restPhone2.setText(phone2);
			} else {
				restPhone2.setVisibility(View.GONE);
			}
			if (imgDraw != null) {
				restImage.setImageDrawable(imgDraw);
			}else{
				restImage.setImageResource(R.drawable.icon);
			}

		}

	}

}
