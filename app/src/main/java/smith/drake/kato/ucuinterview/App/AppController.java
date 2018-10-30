package smith.drake.kato.ucuinterview.App;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import smith.drake.kato.ucuinterview.Model.LruBitmapCache;


public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private static AppController mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}


	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		try {
			MultiDex.install(this);
		} catch (RuntimeException multiDexException) {
			// Work around Robolectric causing multi dex installation to fail, see
			// https://code.google.com/p/android/issues/detail?id=82007.
			boolean isUnderUnitTest;

			try {
				Class<?> robolectric = Class.forName("org.robolectric.Robolectric");
				isUnderUnitTest = (robolectric != null);
			} catch (ClassNotFoundException e) {
				isUnderUnitTest = false;
			}

			if (!isUnderUnitTest) {
				// Re-throw if this does not seem to be triggered by Robolectric.
				throw multiDexException;
			}
		}
	}

}