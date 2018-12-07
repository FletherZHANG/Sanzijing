package com.flether.android.sanzijing;



import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;



public class FavorList extends ListActivity {
	private DataBaseHelper mDbHelper;
	private Cursor mCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favor_list);
		
		
		mDbHelper = new DataBaseHelper(this);		
		
		try { 
			mDbHelper.openDataBase(); 
		}catch(SQLException sqle){ 
			throw sqle; 
		}
		
		mCursor = mDbHelper.fetchFavorData();
		
		
	    Intent intent = getIntent();  
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {  
	    	String searchKey = intent.getStringExtra(SearchManager.QUERY); 
	    	mCursor = mDbHelper.fetchSearchData(searchKey); 	
	    }	
		
		startManagingCursor(mCursor);
		 
         // Create an array to specify the fields we want to display in the list (only TITLE)
	    String[] from = new String[]{DataBaseHelper.KEY_YUANWEN};

	    // and an array of the fields we want to bind those fields to (in this case just text1)
	    int[] to = new int[]{R.id.FAVOR_ROW};

	    // Now create a simple cursor adapter and set it to display
	    SimpleCursorAdapter data = 
	    	new SimpleCursorAdapter(this, R.layout.favor_row, mCursor, from, to);
	    setListAdapter(data);
		registerForContextMenu(getListView());  
		}
	
    	@Override
    	protected void onListItemClick(ListView l, View v, int position, long id) {
    		super.onListItemClick(l, v, position, id);
    		   		
    	    Cursor cursor = mDbHelper.fetchData(id);
    	    startFavorDetail(cursor); 	
    }
    	
    	

    	
	@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			// mDbHelper.close();
			super.onDestroy();
		}

	private void startFavorDetail(Cursor cursor){
	    String yuanWenText = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.KEY_YUANWEN));
	    String pinYinText = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.KEY_PINYIN));
	    String zhuShiText = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.KEY_ZHUSHI));
	    String yiWenText = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.KEY_YIWEN));
	    String qiShiText = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.KEY_QISHI));
			
    	Bundle bundle = new Bundle();
        bundle.putString(DataBaseHelper.KEY_YUANWEN,yuanWenText);
        bundle.putString(DataBaseHelper.KEY_PINYIN,pinYinText);
        bundle.putString(DataBaseHelper.KEY_ZHUSHI,zhuShiText);
        bundle.putString(DataBaseHelper.KEY_YIWEN,yiWenText);
        bundle.putString(DataBaseHelper.KEY_QISHI,qiShiText);
	        
        Intent intent = new Intent(FavorList.this, FavorDetail.class);    
        intent.putExtras(bundle);
        startActivity(intent); 
    }
    	
    	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			
		switch(item.getItemId()) {
            case R.id.MENU_DETAIL:
            	Cursor cursor = mDbHelper.fetchData(info.id);
            	startFavorDetail(cursor);
				break;
            case R.id.MENU_REMOVE:            	
				mDbHelper.updateData (info.id, false);
				mCursor.deactivate(); 
				mCursor.requery();
				break;
            case R.id.MENU_CANCEL:
            	break;
            default:
            	break;
		    }
		    return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_favor, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem retMenu = menu.add(Menu.NONE, Menu.FIRST, 0, R.string.menu_ret);
		retMenu.setIcon(R.drawable.ic_ret);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		this.finish();
		return super.onOptionsItemSelected(item);
	}

	

}
