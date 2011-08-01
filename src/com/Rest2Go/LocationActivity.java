package com.Rest2Go;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class LocationActivity extends ActivityWrapper {

	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Button btnGetLoc;
	protected ListView listView;
	protected ListView lstTest;

	RestDataAdapter arrayAdapter;

	private double mLongitude;
	private double mLatitude;

	ArrayList<RestData> restInfo;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.Exit:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_layout);
		lstTest = (ListView) findViewById(R.id.lstText);
		restInfo = new ArrayList<RestData>();
		arrayAdapter = new RestDataAdapter(this.getApplicationContext(), R.layout.listitems,
				restInfo);
		lstTest.setAdapter(arrayAdapter);
		lstTest.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
				long restId = (Long) arg1.getTag(R.layout.listitems);
				intent.putExtra(Consts.REST_ID, restId);
				LocationActivity.this.startActivity(intent);

			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		Intent intent = getIntent();
		if (!intent.hasExtra(Consts.Extras.SearchResult)) {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationListener = new LocationListener() {

				@Override
				public void onLocationChanged(Location location) {
					try {
						showCurrentLocation();
						locationManager.removeUpdates(locationListener);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onProviderDisabled(String provider) {
				}

				@Override
				public void onProviderEnabled(String provider) {
				}

				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {
				}
			};
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					Consts.MINIMUM_TIME_BETWEEN_UPDATES,
					Consts.MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
			showCurrentLocation();
			SendLocInfo();
		} else {
			String body = intent.getStringExtra(Consts.Extras.SearchResult);
			JSONObject obj = null;
			try {
				obj = new JSONObject(body);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (obj != null) {
				LocationActivity.this.removeDialog(ProgressDialog.STYLE_HORIZONTAL);
				JSONArray arr = obj.optJSONArray("restaurants");
				displayData(arr);
			}
		}

	}

	protected void showCurrentLocation() {
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location != null) {
			String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude());
			Utils.log(message);
			this.mLatitude = location.getLatitude();
			this.mLongitude = location.getLongitude();
		}

	}

	protected void displayData(JSONArray arr) {

		for (int i = 0; i <= arr.length() - 1; i++) {
			JSONObject rest = arr.optJSONObject(i);
			String phone2 = "";
			String iconUrl = null;
			BitmapDrawable iconDraw = null;
			BitmapDrawable imgDraw = null;
			long id = rest.optLong(Consts.DB_COLUMNS.ID);
			String name = rest.optString(Consts.DB_COLUMNS.NAME);
			String address = rest.optString(Consts.DB_COLUMNS.ADDRESS);
			String phone = rest.optString(Consts.DB_COLUMNS.PHONE);
			if (rest.optString(Consts.DB_COLUMNS.SECONDARY_PHONE) != null)
				phone2 = rest.optString(Consts.DB_COLUMNS.SECONDARY_PHONE);
			if (rest.optString(Consts.DB_COLUMNS.ICON_URL) != null) {
				iconUrl = rest.optString(Consts.DB_COLUMNS.ICON_URL);
			}
			if (iconUrl != null && !iconUrl.equals("null")) {
				iconDraw = getDrawableFromURL(iconUrl);
			}
			restInfo.add(new RestData(id, name, address, phone, phone2, iconUrl, iconDraw));
			arrayAdapter.notifyDataSetChanged();
		}

	}

	static public BitmapDrawable getDrawableFromURL(String iconUrl) {
		URL myFileUrl = null;
		BitmapDrawable res = null;
		if (!iconUrl.contains("http://")){
			iconUrl=Consts.IMAGE_PATH+iconUrl;
		}
		try {
			myFileUrl = new URL(iconUrl);
		} catch (MalformedURLException e) {
			Utils.log("failed to download image from " + iconUrl + e.getMessage());
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();

			Bitmap streamBitmap = BitmapFactory.decodeStream(is);
			res = new BitmapDrawable(streamBitmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	private void SendLocInfo() {
		new AsyncTask<Void, String, String>() {
			@Override
			protected void onPreExecute() {
				LocationActivity.this.showLoading(this);
			}

			@Override
			protected String doInBackground(Void... arg0) {

				return (Utils.preformRequest(Utils.BASE_URL + "?x=" + mLongitude + "&y="
						+ mLatitude));
			}

			@Override
			protected void onPostExecute(String body) {
				if (body != null) {
				}
				JSONObject obj = null;
				try {
					obj = new JSONObject(body);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (obj != null) {
					LocationActivity.this.removeDialog(ProgressDialog.STYLE_HORIZONTAL);
					JSONArray arr = obj.optJSONArray("restaurants");
					displayData(arr);
				}
			}

		}.execute();

	}

	@Override
	protected void onDestroy() {
		if (locationManager != null) {
			locationManager.removeUpdates(locationListener);
		}
		super.onDestroy();
	}

}