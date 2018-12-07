package com.flether.android.sanzijing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;


public class BookCover extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_cover);
		
	}
	
	
	public boolean onTouchEvent(MotionEvent ev) {
		
		Intent intent = new Intent();
		intent.setClass(BookCover.this, MainPage.class);
		startActivity(intent);
		return true;
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		finish();
		super.onStop();
	}
	
	

}
