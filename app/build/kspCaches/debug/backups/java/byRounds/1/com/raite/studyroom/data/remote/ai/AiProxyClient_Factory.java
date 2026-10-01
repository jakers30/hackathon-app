package com.raite.studyroom.data.remote.ai;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
import io.ktor.client.HttpClient;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class AiProxyClient_Factory implements Factory<AiProxyClient> {
  private final Provider<HttpClient> httpProvider;

  private final Provider<SupabaseClient> supabaseProvider;

  public AiProxyClient_Factory(Provider<HttpClient> httpProvider,
      Provider<SupabaseClient> supabaseProvider) {
    this.httpProvider = httpProvider;
    this.supabaseProvider = supabaseProvider;
  }

  @Override
  public AiProxyClient get() {
    return newInstance(httpProvider.get(), supabaseProvider.get());
  }

  public static AiProxyClient_Factory create(Provider<HttpClient> httpProvider,
      Provider<SupabaseClient> supabaseProvider) {
    return new AiProxyClient_Factory(httpProvider, supabaseProvider);
  }

  public static AiProxyClient newInstance(HttpClient http, SupabaseClient supabase) {
    return new AiProxyClient(http, supabase);
  }
}
