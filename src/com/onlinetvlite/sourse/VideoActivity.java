package com.onlinetvlite.sourse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class VideoActivity extends Activity{

	private Intent intent;
	private String channelPath;
	private VideoView videoView;
	private ProgressDialog progressDialog;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private String [] channelsNames;
	private String [] channelsPathes;
	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_video);
		adView = (AdView)findViewById(R.id.adViewVideoActivity);
		adView.loadAd(new AdRequest());

		//linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
		
		channelsNames = getResources().getStringArray(R.array.channels_names);
		channelsPathes = getResources().getStringArray(R.array.channels_path);
		adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_item, channelsNames);
		intent = getIntent();
		channelPath = intent.getStringExtra("channelPath");
		listView = (ListView)findViewById(R.id.list_view_video);
		listView.setVisibility(View.GONE);
		adView.setVisibility(View.GONE);
		listView.setAdapter(adapter);
		videoView = (VideoView)findViewById(R.id.video_view);
		videoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				if(progressDialog != null && progressDialog.isShowing()){
					progressDialog.dismiss();
					progressDialog = null;
				}
				videoView.start();
			}
		});
		videoView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(listView.getVisibility() == View.VISIBLE) {
					listView.setVisibility(View.GONE);
					adView.setVisibility(View.GONE);
				}
				else {
					listView.setVisibility(View.VISIBLE);
					adView.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				playTV(channelsPathes[position]);
			}
		});
		playTV(channelPath);
	}

	public void playTV(String channelPath) {
		if(progressDialog != null) progressDialog.dismiss();
		if(checkNetworkConnection()) {			
			Uri video = Uri.parse(channelPath);
			videoView.setVideoURI(video);
			showProgressDoalog();	
		} else {
			Toast.makeText(getApplicationContext(),getResources().getString(R.string.string_error_connect_to_internet), Toast.LENGTH_SHORT).show();
		}
	}

	public boolean checkNetworkConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	private void showProgressDoalog() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getResources().getString(R.string.string_dialog_title));
		progressDialog.setMessage(getResources().getString(R.string.string_dialog_message));
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (adView != null) {
			adView.destroy();
		}
		if(progressDialog != null) progressDialog.dismiss();
		videoView.stopPlayback();
	}

	@Override
	protected void onPause() {
		super.onPause();
		videoView.suspend();
	}

	@Override
	protected void onResume() {
		super.onResume();
		videoView.resume();
	}	
}