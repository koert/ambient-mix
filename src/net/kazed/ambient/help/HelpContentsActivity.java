package net.kazed.ambient.help;

import net.kazed.ambient.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebView;

public class HelpContentsActivity extends Activity {
	
    private WebView webView;

    @Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.help_welcome);
        
        webView= (WebView) findViewById(R.id.webkit);
        
	}

    @Override
    protected void onResume() {
        super.onResume();
        webView.loadUrl("file:///android_asset/welcome.html");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean result = true;
        if (KeyEvent.KEYCODE_BACK == keyCode && webView.canGoBack()) {
            webView.goBack();
        } else {
            result = super.onKeyDown(keyCode, event);
        }
        return result;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean result = true;
        if (KeyEvent.KEYCODE_BACK == keyCode && webView.canGoBack()) {
            webView.goBack();
        } else {
            result = super.onKeyUp(keyCode, event);
        }
        return result;
    }

    
}
