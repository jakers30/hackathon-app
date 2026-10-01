package com.raite.studyroom.data.remote;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
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
public final class SupabaseContentSource_Factory implements Factory<SupabaseContentSource> {
  private final Provider<SupabaseClient> clientProvider;

  public SupabaseContentSource_Factory(Provider<SupabaseClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public SupabaseContentSource get() {
    return newInstance(clientProvider.get());
  }

  public static SupabaseContentSource_Factory create(Provider<SupabaseClient> clientProvider) {
    return new SupabaseContentSource_Factory(clientProvider);
  }

  public static SupabaseContentSource newInstance(SupabaseClient client) {
    return new SupabaseContentSource(client);
  }
}
