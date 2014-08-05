package com.messi.cailing;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.baidu.mobstat.StatService;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;


public class MainActivity extends BaseActivity{
	
	public static final String Url = "http://api.openspeech.cn/kyls/NTBjMDdmMWQ=";
	private ProgressBar mProgressBar;
	private PullToRefreshWebView mWebView;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    
    private long exitTime = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		initViews();
	}
	
	private void initViews(){
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mProgressBar = (ProgressBar) findViewById(R.id.progressbar_m);
		mWebView = (PullToRefreshWebView) findViewById(R.id.refreshable_webview);
		mWebView.requestFocus();//如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
		mWebView.getRefreshableView().getSettings().setJavaScriptEnabled(true);//如果访问的页面中有Javascript，则webview必须设置支持Javascript。

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
            	mActionBar.setTitle(MainActivity.this.getResources().getString(R.string.app_name));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
            	mActionBar.setTitle(MainActivity.this.getResources().getString(R.string.title_more));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
		//当前页面加载
		mWebView.getRefreshableView().setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mProgressBar.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				mProgressBar.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				mWebView.getRefreshableView().loadUrl(url);
				return true;
			}

		});
		mWebView.getRefreshableView().loadUrl(Url);
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	    	try {
				if(position == 2){
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("market://details?id=com.messi.languagehelper"));
					startActivity(intent);
					StatService.onEvent(MainActivity.this, "1.6_commend", "吐槽评价按钮", 1);
				}else if(position == 5){
//					StatService.onEvent(MainActivity.this, "1.8_contantus", "联系我们按钮", 1);
				}else{
					Intent intent = new Intent();
//					if(position == 0){
//						intent.setClass(MainActivity.this, SettingActivity.class);
//						StatService.onEvent(MainActivity.this, "1.6_settingbtn", "应用设置按钮", 1);
//					}else if(position == 1){
//						intent.setClass(MainActivity.this, RecommendActivity.class);
//						StatService.onEvent(MainActivity.this, "1.6_recommendbtn", "推荐应用按钮", 1);
//					}else if(position == 3){
//						intent.setClass(MainActivity.this, HelpActivity.class);
//						StatService.onEvent(MainActivity.this, "1.7_help", "使用帮助按钮", 1);
//					}else if(position == 4){
//						intent.setClass(MainActivity.this, AboutActivity.class);
//						StatService.onEvent(MainActivity.this, "1.6_aboutus", "关于我们按钮", 1);
//					}
					MainActivity.this.startActivity(intent);
					if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
						mDrawerLayout.closeDrawer(mDrawerList);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:  
			
//			StatService.onEvent(this, "1.6_homemenu", "主页左上角菜单", 1);
			break;
		case 0:  
			
//			StatService.onEvent(this, "1.8_menu_to_share_activity", "去自定义分享页面", 1);
			break;
//		case 1:  
//			toSettingActivity();
//			StatService.onEvent(this, "1.8_menu_to_settings", "语速调节", 1);
//			break;
		}
       return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if((keyCode == KeyEvent.KEYCODE_BACK ) && mWebView.getRefreshableView().canGoBack()){
			mWebView.getRefreshableView().goBack();
			return true;
		}else if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.exit_program), 0).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
		}
	}

}
