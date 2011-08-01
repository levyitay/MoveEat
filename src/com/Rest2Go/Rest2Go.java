package com.Rest2Go;

import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TabHost;

public class Rest2Go extends TabActivity {
	private LocalActivityManager mlam;

	  public void onCreate(Bundle paramBundle)
	  {
	    super.onCreate(paramBundle);
	    setContentView(R.layout.tab_layout);
	    Resources localResources = getResources();
	    TabHost localTabHost = getTabHost();
	    LocalActivityManager localLocalActivityManager1 = new LocalActivityManager(this, true);
	    this.mlam = localLocalActivityManager1;
	    this.mlam.dispatchCreate(paramBundle);
	    LocalActivityManager localLocalActivityManager2 = this.mlam;
	    localTabHost.setup(localLocalActivityManager2);
	    Intent localIntent1 = new Intent().setClass(this, LocationActivity.class);
	    TabHost.TabSpec localTabSpec1 = localTabHost.newTabSpec("By Location");
	    Drawable localDrawable1 = localResources.getDrawable(android.R.drawable.ic_menu_mylocation);
	    TabHost.TabSpec localTabSpec2 = localTabSpec1.setIndicator("By Location", localDrawable1).setContent(localIntent1);
	    localTabHost.addTab(localTabSpec2);
	    Intent localIntent2 = new Intent().setClass(this, SearchActivity.class);
	    TabHost.TabSpec localTabSpec3 = localTabHost.newTabSpec("Search");
	    Drawable localDrawable2 = localResources.getDrawable(android.R.drawable.ic_menu_search);
	    TabHost.TabSpec localTabSpec4 = localTabSpec3.setIndicator("Search", localDrawable2).setContent(localIntent2);
	    localTabHost.addTab(localTabSpec4);
	    localTabHost.setCurrentTab(0);
	  }

	  public void onPause()
	  {
	    super.onPause();
	    this.mlam.dispatchResume();
	  }

	  public void onResume()
	  {
	    super.onResume();
	    this.mlam.dispatchResume();
	  }

}
