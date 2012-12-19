package org.twocloud.android;

import android.app.Activity;
import android.os.Bundle;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(android.R.anim.slide_out_right,
				android.R.anim.slide_in_left);
	}
}
