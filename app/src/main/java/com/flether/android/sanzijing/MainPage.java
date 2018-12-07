package com.flether.android.sanzijing;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;



public class MainPage extends Activity implements OnGestureListener {

	private ScrollView mScrollView;
	private TextView mYuanWen;
	private TextView mPinYin;
	private TextView mZhuShi;
	private TextView mYiWen;
	private TextView mQiShi;
	private Cursor mCursor;

	private int lastCursorPosition;
	private GestureDetector mGestureDetector;
	private MediaPlayer mMediaPlayer;

	public DataBaseHelper myDbHelper;
	public static final String PREFS_NAME = "mLastPage";


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_page);

		//Hide the Scroll bar
		mScrollView = (ScrollView)findViewById(R.id.SCROLL_VIEW);
		mScrollView.setVerticalScrollBarEnabled(false);


		mGestureDetector = new GestureDetector(this);

		mYuanWen = (TextView)findViewById(R.id.YUAN_WEN);
		mPinYin = (TextView)findViewById(R.id.PIN_YIN);
		mZhuShi = (TextView)findViewById(R.id.ZHU_SHI);
		mYiWen = (TextView)findViewById(R.id.YI_WEN);
		mQiShi = (TextView)findViewById(R.id.QI_SHI);

		myDbHelper = new DataBaseHelper(this);


		try {
			myDbHelper.openDataBase();
		}catch(SQLException sqle) {
			throw sqle;
		}

		mCursor = myDbHelper.fetchAllData();

		try {
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			lastCursorPosition = settings.getInt("lastCursorPosition", 0);
		} catch(Exception ex) {
			lastCursorPosition = 0;
		}


		if (mCursor != null) {
			mCursor.moveToPosition(lastCursorPosition);
			updateDisplay();
		}

		mMediaPlayer = new MediaPlayer();
		mMediaPlayer = MediaPlayer.create(MainPage.this, R.raw.szj);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("lastCursorPosition", mCursor.getPosition());
		editor.commit();


		mMediaPlayer.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateDisplay();
	}

	/*
	 * My private functions.
	 */

	private boolean updateDisplay(){
		if (mCursor != null) {
			String pageInfo = "第"+(mCursor.getPosition()+1) + "页/总" + mCursor.getCount()+"页";
			Toast toast = Toast.makeText(MainPage.this, pageInfo, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			String yuanWenText = mCursor.getString(mCursor.getColumnIndex(DataBaseHelper.KEY_YUANWEN));
			String pinYinText = mCursor.getString(mCursor.getColumnIndex(DataBaseHelper.KEY_PINYIN));
			String zhuShiText = mCursor.getString(mCursor.getColumnIndex(DataBaseHelper.KEY_ZHUSHI));
			String yiWenText = mCursor.getString(mCursor.getColumnIndex(DataBaseHelper.KEY_YIWEN));
			String qiShiText = mCursor.getString(mCursor.getColumnIndex(DataBaseHelper.KEY_QISHI));
			yuanWenText = yuanWenText.replace("。", "。\n");
			mYuanWen.setText(yuanWenText);
			mPinYin.setText(pinYinText);
			mZhuShi.setText(zhuShiText);
			mYiWen.setText(yiWenText);
			mQiShi.setText(qiShiText);
			mScrollView.smoothScrollTo(0, 0);
			return true;
		}
		else
			return false;
	}

	private void playMusic(MenuItem item){
		if (mMediaPlayer.isPlaying())
		{
			mMediaPlayer.stop();
		}
		else{
			try {
				mMediaPlayer.stop();
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			} catch (Exception e) {;}
		}
	}

	private void showInfoDialog(){
		new AlertDialog.Builder(MainPage.this).setTitle(
				R.string.app_info).setMessage(R.string.app_info_msg)
				.setPositiveButton(R.string.str_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface,
									int i) {
							}
						}).show();

	}

	private void onExitProcess(){
		mMediaPlayer.stop();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("lastCursorPosition", mCursor.getPosition());
		editor.commit();
		myDbHelper.close();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/*
	 * The gesture function comes here.
	 */


	@Override
	public boolean dispatchTouchEvent(MotionEvent ev){
		super.dispatchTouchEvent(ev);
		return mGestureDetector.onTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		this.mGestureDetector.onTouchEvent(event);
		return false;
	}


	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
						   float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > 150 && Math.abs(velocityX) > 200)
		{
			if (!mCursor.isLast() && mCursor!=null){
				mCursor.moveToNext();
				updateDisplay();
			}

		}
		else if (e2.getX() - e1.getX() > 150 && Math.abs(velocityX) > 200)
		{
			if (!mCursor.isFirst() && mCursor!=null)
			{
				mCursor.moveToPrevious();
				updateDisplay();
			}
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
							float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	/*
	 *  Create my menu
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem item = menu.findItem(R.id.MENU_VOICE);

		if (mMediaPlayer.isPlaying())
		{
			item.setIcon(R.drawable.ic_stop);
			item.setTitle("停止");
		}
		else
		{
			item.setIcon(R.drawable.ic_voice);
			item.setTitle("播放");
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int itemId = item.getItemId();
		switch (itemId)
		{
			case R.id.MENU_SEARCH:
				onSearchRequested();
				break;
			case R.id.MENU_FAVOR_ADD:
				if (mCursor!=null) {
					long rowId = mCursor.getPosition() + 1;
					boolean isFavor = true;
					myDbHelper.updateData (rowId, isFavor);
				}
				break;
			case R.id.MENU_FAVOR_GO:
				Intent intent = new Intent();
				intent.setClass(MainPage.this, FavorList.class);
				startActivity(intent);
				break;

			case R.id.MENU_VOICE:
				playMusic(item);
				break;

			case R.id.MENU_INFO:
				showInfoDialog();
				break;
			case R.id.MENU_QUIT:
				onExitProcess();
				break;
			default:
				break;
		}

		return super.onOptionsItemSelected(item);
	}

}
