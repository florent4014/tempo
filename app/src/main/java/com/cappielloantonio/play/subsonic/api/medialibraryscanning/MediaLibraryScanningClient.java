package com.cappielloantonio.play.subsonic.api.medialibraryscanning;

import android.content.Context;
import android.util.Log;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.api.system.SystemService;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.subsonic.utils.CacheUtil;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;

public class MediaLibraryScanningClient {
    private static final String TAG = "SystemClient";

    private final Context context;
    private final Subsonic subsonic;
    private Retrofit retrofit;
    private final MediaLibraryScanningService mediaLibraryScanningService;

    public MediaLibraryScanningClient(Context context, Subsonic subsonic) {
        this.context = context;
        this.subsonic = subsonic;

        this.retrofit = new Retrofit.Builder()
                .baseUrl(subsonic.getUrl())
                .addConverterFactory(TikXmlConverterFactory.create())
                .client(getOkHttpClient())
                .build();

        this.mediaLibraryScanningService = retrofit.create(MediaLibraryScanningService.class);
    }

    public Call<SubsonicResponse> startScan() {
        Log.d(TAG, "startScan()");
        return mediaLibraryScanningService.startScan(subsonic.getParams());
    }

    public Call<SubsonicResponse> getScanStatus() {
        Log.d(TAG, "getScanStatus()");
        return mediaLibraryScanningService.getScanStatus(subsonic.getParams());
    }

    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(getHttpLoggingInterceptor())
                .addInterceptor(CacheUtil.offlineInterceptor)
                .addNetworkInterceptor(CacheUtil.onlineInterceptor)
                .cache(getCache())
                .build();
    }

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return loggingInterceptor;
    }

    private Cache getCache() {
        int cacheSize = 10 * 1024 * 1024;
        return new Cache(context.getCacheDir(), cacheSize);
    }
}