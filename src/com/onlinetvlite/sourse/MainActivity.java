package com.onlinetvlite.sourse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.ads.InterstitialAd;

public class MainActivity extends Activity{

	private AdView adView;
	private InterstitialAd interstitial;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private String [] channelsNames;
	private String [] channelsPathes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		adView = (AdView)findViewById(R.id.adViewMainActivity);
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		adView.loadAd(adRequest);

		interstitial = new InterstitialAd(this, getResources().getString(R.string.adword_page));
		interstitial.loadAd(new AdRequest());

		channelsNames = getResources().getStringArray(R.array.channels_names);
		channelsPathes = getResources().getStringArray(R.array.channels_path);
		listView = (ListView)findViewById(R.id.list_view_main);
		adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, channelsNames);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(checkNetworkConnection()){
					Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
					intent.putExtra("channelPath", channelsPathes[position]);
					startActivity(intent);
				}else {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.string_error_connect_to_internet), Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exit:
			interstitial.show();
			finish();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean checkNetworkConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		interstitial.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
}
