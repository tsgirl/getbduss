package top.tsgirl.getbduss;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.SslErrorHandler;
import android.widget.Toast;


public class MainActivity extends Activity {
    private WebView webView;
    private ClipboardManager myClipboard;
    private ClipData myClip;

    // 打开进度条
    private void openProgressBar(int x) {
        setProgressBarIndeterminateVisibility(true);
        setProgress(x);
    }

    // 关闭进度条
    private void closeProgressBar() {
        setProgressBarIndeterminateVisibility(false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        setProgressBarIndeterminate(true);
        android.widget.Toast.makeText(getApplicationContext(), "成功登录后自动复制BDUSS~", Toast.LENGTH_LONG).show();
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        webView=(WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(true);
        //webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.1.3) Gecko/20091020 Ubuntu/9.10 (karmic) Firefox/3.5.3");
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 4.0.3; en-US; M351 Build/JOP40D) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/9.0.1.284 U3/0.8.0 Mobile Safari/533.1");
        android.webkit.CookieManager.getInstance().removeAllCookie();
        webView.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, android.net.http.SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
                String CookieStr = cookieManager.getCookie(url);
                if (CookieStr.contains("BDUSS")) {
                    int BdussOffset = CookieStr.indexOf("BDUSS=") + 6;
                    String Bduss = CookieStr.substring(BdussOffset);
                    if (Bduss.contains(";")) {
                        BdussOffset = Bduss.indexOf(";");
                        Bduss = Bduss.substring(0, BdussOffset);
                    }
                    myClip = ClipData.newPlainText("text", Bduss);
                    myClipboard.setPrimaryClip(myClip);
                    Toast.makeText(getApplicationContext(), "已复制BDUSS到剪贴板", Toast.LENGTH_LONG).show();
                }
                super.onPageFinished(view, url);
            }


        });
        webView.setWebChromeClient(new android.webkit.WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    closeProgressBar();
                } else {
                    openProgressBar(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        //webView.loadUrl("http://passport.baidu.com");
        webView.loadUrl("http://wappass.baidu.com/passport/?login&tpl=wimn&subpro=wimn&regtype=1&u=https%3A%2F%2Fpassport.baidu.com");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }
}

