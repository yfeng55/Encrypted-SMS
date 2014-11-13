package com.yfeng.lockedsms;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;


public class ContactListFragment extends ListFragment implements LoaderCallbacks<Cursor> {

	private CursorAdapter mAdapter;

	// columns requested from the database
	private static final String[] PROJECTION = {
			ContactsContract.CommonDataKinds.Phone._ID, // _ID is always required
			ContactsContract.Contacts.DISPLAY_NAME,
			ContactsContract.CommonDataKinds.Phone.NUMBER };

	private static final String[] FROM = {
			ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
			ContactsContract.CommonDataKinds.Phone.NUMBER };

	// Name and phone number are displayed in the text1 textview in item layout
	private static final int[] TO = { android.R.id.text1};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// create adapter once
		Context context = getActivity();
		Cursor c = null; //no cursor yet
		
		//an adapter to map columns from a cursor to views in an xml layout
		mAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, c, FROM, TO, 0);
	}

	
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		
//		String columnnames[] = cursor.getColumnNames();
//		Log.i("EEE", columnnames[0]);
//		Log.i("EEE", columnnames[1]);
//		Log.i("EEE", columnnames[2]);
		
//		Log.i("EEE", cursor.getString(cursor.getColumnIndex("_ID")));
//		Log.i("EEE", cursor.getString(cursor.getColumnIndex("DISPLAY_NAME")));
//		Log.i("EEE", cursor.getString(cursor.getColumnIndex("DATA1")));
		

		Intent i = new Intent(v.getContext(), SendMessageActivity.class);
		Cursor cursor = (Cursor) mAdapter.getItem(position);
		
		i.putExtra("phonenumber", cursor.getString(cursor.getColumnIndex("DATA1")));
		i.putExtra("contactname", cursor.getString(cursor.getColumnIndex("DISPLAY_NAME")));
		
		startActivity(i);
	}	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//provide the adapter to the listview
		setListAdapter(mAdapter);
		getLoaderManager().initLoader(0, null, this);
	}
	
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		// load from the "Contacts table"
		Uri contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC";
		return new CursorLoader(getActivity(), contentUri, PROJECTION, null, null, sortOrder);
	}
	

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Once cursor is loaded, give it to adapter
		mAdapter.swapCursor(data);
	}
	

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// on reset take any old cursor away
		mAdapter.swapCursor(null);
	}
	
	
}














