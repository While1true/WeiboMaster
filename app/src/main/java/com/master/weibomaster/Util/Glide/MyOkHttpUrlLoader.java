package com.master.weibomaster.Util.Glide;

import android.support.annotation.NonNull;

import com.bumptech.glide.integration.okhttp3.OkHttpStreamFetcher;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.master.weibomaster.Util.K2JUtils;

import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * A simple model loader for fetching media over http/https using OkHttp.
 */
public class MyOkHttpUrlLoader implements ModelLoader<GlideUrl, InputStream> {

  private final Call.Factory client;

  // Public API.
  @SuppressWarnings("WeakerAccess")
  public MyOkHttpUrlLoader(@NonNull Call.Factory client) {
    this.client = client;
  }

  @Override
  public boolean handles(@NonNull GlideUrl url) {
    return true;
  }

  @Override
  public LoadData<InputStream> buildLoadData(@NonNull GlideUrl model, int width, int height,
      @NonNull Options options) {
    return new LoadData<>(model, new OkHttpStreamFetcher(client, model));
  }

  /**
   * The default factory for {@link MyOkHttpUrlLoader}s.
   */
  // Public API.
  @SuppressWarnings("WeakerAccess")
  public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
    private static volatile Call.Factory internalClient;
    private final Call.Factory client;

    private static Call.Factory getInternalClient() {
      if (internalClient == null) {
        synchronized (Factory.class) {
          if (internalClient == null) {
            HttpsCerUtil.SSLParams sslSocketFactory = HttpsCerUtil.getSslSocketFactory(null, null, null);
            internalClient = new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory.sSLSocketFactory,sslSocketFactory.trustManager)
                    .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                      @Override
                      public void log(String message) {
                        K2JUtils.log("HttpLoggingInterceptor", message);
                      }
                    }).setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
          }
        }
      }
      return internalClient;
    }

    /**
     * Constructor for a new Factory that runs requests using a static singleton client.
     */
    public Factory() {
      this(getInternalClient());
    }

    /**
     * Constructor for a new Factory that runs requests using given client.
     *
     * @param client this is typically an instance of {@code OkHttpClient}.
     */
    public Factory(@NonNull Call.Factory client) {
      this.client = client;
    }

    @NonNull
    @Override
    public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
      return new MyOkHttpUrlLoader(client);
    }

    @Override
    public void teardown() {
      // Do nothing, this instance doesn't own the client.
    }
  }
}
