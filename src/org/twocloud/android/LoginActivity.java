package org.twocloud.android;

import org.twocloud.android.GoogleAccountFragment.OnAccountSelectedListener;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

public class LoginActivity extends SherlockFragmentActivity implements
		OnAccountSelectedListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	public void onAccountSelected(String name, String token) {
		// Toast.makeText(this, "Chose "+name+" with token "+token+".",
		// Toast.LENGTH_LONG).show();
		Intent i = new Intent(this, RegisterActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
		return;
	}

}
