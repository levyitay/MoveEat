package com.Rest2Go;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class SearchActivity extends ActivityWrapper {

	private RadioGroup radioGroup;

	@Override
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);
		Button searchBtn = (Button) findViewById(R.id.search_btn);
		
		radioGroup = (RadioGroup) findViewById(R.id.radio_group);
		
		searchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (radioGroup.getCheckedRadioButtonId()==R.id.rest_rad_btn){
					EditText restText = (EditText) findViewById(R.id.rest_text);
					searchByRestName(restText.getText().toString());
				}else{
					EditText streetText = (EditText) findViewById(R.id.street_text);
					searchByStreet(streetText.getText().toString());
				}
				
			}
		});
		
	}

	protected void searchByRestName(final String string) {
		new AsyncTask<Void, String, String>() {
			@Override
			protected void onPreExecute() {
				SearchActivity.this.showLoading(this);
			}

			@Override
			protected String doInBackground(Void... arg0) {

				return (Utils.preformRequest(Utils.BASE_URL + "?search_name="+string));
			}

			@Override
			protected void onPostExecute(String body) {
				if (body != null) {
					Intent intent = new Intent(getBaseContext(),LocationActivity.class);
					intent.putExtra(Consts.Extras.SearchResult, body);
					startActivity(intent);
				}
			}

		}.execute();

		
	}

	protected void searchByStreet(final String string) {
		new AsyncTask<Void, String, String>() {
			@Override
			protected void onPreExecute() {
				SearchActivity.this.showLoading(this);
			}

			@Override
			protected String doInBackground(Void... arg0) {

				return (Utils.preformRequest(Utils.BASE_URL + "?address="+string));
			}

			@Override
			protected void onPostExecute(String body) {
				if (body != null) {
					Intent intent = new Intent(getBaseContext(),LocationActivity.class);
					intent.putExtra(Consts.Extras.SearchResult, body);
					startActivity(intent);
				}
			}

		}.execute();

		
	}
	
	
}
