package com.flether.android.sanzijing;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;





public class FavorDetail extends Activity {

	private TextView mYuanWen;
	private TextView mPinYin;
	private TextView mZhuShi;
	private TextView mYiWen;
	private TextView mQiShi;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_page);
		
		//Hide the Scollbar
		ScrollView sView = (ScrollView)findViewById(R.id.SCROLL_VIEW);        
        sView.setVerticalScrollBarEnabled(false);
		
		mYuanWen = (TextView)findViewById(R.id.YUAN_WEN); 
		mPinYin = (TextView)findViewById(R.id.PIN_YIN);
	    mZhuShi = (TextView)findViewById(R.id.ZHU_SHI); 
	    mYiWen = (TextView)findViewById(R.id.YI_WEN); 
	    mQiShi = (TextView)findViewById(R.id.QI_SHI); 
	    
	    Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
        String yuanWenText = bundle.getString(DataBaseHelper.KEY_YUANWEN);
        String pinYinText = bundle.getString(DataBaseHelper.KEY_PINYIN);
        String zhuShiText = bundle.getString(DataBaseHelper.KEY_ZHUSHI);
        String yiWenText = bundle.getString(DataBaseHelper.KEY_YIWEN);
        String qiShiText = bundle.getString(DataBaseHelper.KEY_QISHI);
        yuanWenText = yuanWenText.replace("。", "。\n");
        qiShiText = qiShiText.replace("。", "。\n");
        
        mYuanWen.setText(yuanWenText);
        mPinYin.setText(pinYinText);
        mZhuShi.setText(zhuShiText);
        mYiWen.setText(yiWenText);	
        mQiShi.setText(qiShiText);	
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
