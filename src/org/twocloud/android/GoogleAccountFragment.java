package org.twocloud.android;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;

public class GoogleAccountFragment extends SherlockListFragment {
	private OnAccountSelectedListener mListener;
	private Account[] accounts;

	public interface OnAccountSelectedListener {
		public void onAccountSelected(String name, String token);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnAccountSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnAccountSelectedListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View result = inflater.inflate(R.layout.fragment_google_account,
				container, false);

		// get the list of accounts
		AccountManager accountManager = AccountManager.get(getActivity()
				.getApplicationContext());
		accounts = accountManager.getAccountsByType("com.google");

		// create an array adapter for them
		ArrayAdapter<Account> accountAdapter = new GoogleAccountAdapter(
				getActivity(), R.layout.listitem_google_account, accounts);
		this.setListAdapter(accountAdapter);
		return result;
	}

	@Override
	public void onResume() {
		super.onResume();
		View rootView = getView();
		View loadingMessage = rootView.findViewById(R.id.token_loading_layout);
		View accountList = rootView.findViewById(android.R.id.list);
		if (loadingMessage != null && accountList != null) {
			accountList.setVisibility(View.VISIBLE);
			loadingMessage.setVisibility(View.GONE);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (accounts.length <= position) {
			return;
		}
		View rootView = getView();
		View loadingMessage = rootView.findViewById(R.id.token_loading_layout);
		View accountList = rootView.findViewById(android.R.id.list);
		if (loadingMessage != null && accountList != null) {
			accountList.setVisibility(View.GONE);
			loadingMessage.setVisibility(View.VISIBLE);
			Log.d("2cloud",
					"loading message visibility: "
							+ loadingMessage.getVisibility());
		}
		AccountManager am = AccountManager.get(getActivity()
				.getApplicationContext());
		Bundle options = new Bundle();
		am.getAuthToken(
				accounts[position],
				"oauth2:https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile",
				options, getActivity(), new OnTokenAcquired(), new Handler(
						new OnError()));
	}

	private class OnTokenAcquired implements AccountManagerCallback<Bundle> {

		public void run(AccountManagerFuture<Bundle> result) {
			Bundle bundle;
			try {
				bundle = result.getResult();
				String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
				String name = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
				mListener.onAccountSelected(name, token);
			} catch (OperationCanceledException e) {
				View rootView = getView();
				View loadingMessage = rootView
						.findViewById(R.id.token_loading_layout);
				View accountList = rootView.findViewById(android.R.id.list);
				if (loadingMessage != null && accountList != null) {
					accountList.setVisibility(View.VISIBLE);
					loadingMessage.setVisibility(View.GONE);
				}
				Toast.makeText(getActivity(),
						"Please grant permission, or we can't log you in.",
						Toast.LENGTH_LONG).show();
			} catch (AuthenticatorException e) {
				View rootView = getView();
				View loadingMessage = rootView
						.findViewById(R.id.token_loading_layout);
				View accountList = rootView.findViewById(android.R.id.list);
				if (loadingMessage != null && accountList != null) {
					accountList.setVisibility(View.VISIBLE);
					loadingMessage.setVisibility(View.GONE);
				}
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				View rootView = getView();
				View loadingMessage = rootView
						.findViewById(R.id.token_loading_layout);
				View accountList = rootView.findViewById(android.R.id.list);
				if (loadingMessage != null && accountList != null) {
					accountList.setVisibility(View.VISIBLE);
					loadingMessage.setVisibility(View.GONE);
				}
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class OnError implements Callback {
		public boolean handleMessage(Message msg) {

			View rootView = getView();
			View loadingMessage = rootView
					.findViewById(R.id.token_loading_layout);
			View accountList = rootView.findViewById(android.R.id.list);
			if (loadingMessage != null && accountList != null) {
				accountList.setVisibility(View.VISIBLE);
				loadingMessage.setVisibility(View.GONE);
			}
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(),
					"Error obtaining auth token. Try again.", Toast.LENGTH_LONG)
					.show();
			return false;
		}
	}

	public class GoogleAccountAdapter extends ArrayAdapter<Account> {
		private Account[] accounts;
		private Context context;

		public GoogleAccountAdapter(Context context, int textViewResourceId,
				Account[] accounts) {
			super(context, textViewResourceId, accounts);
			this.accounts = accounts;
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.listitem_google_account, null);
			}

			if (accounts.length > position) {
				Account account = accounts[position];
				if (account != null) {
					TextView name = (TextView) v
							.findViewById(R.id.account_name_textview);

					if (name != null) {
						name.setText(account.name);
					}
				}
			}
			return v;
		}
	}
}
