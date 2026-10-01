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
public final class SupabaseDataSource_Factory implements Factory<SupabaseDataSource> {
  private final Provider<SupabaseClient> clientProvider;

  public SupabaseDataSource_Factory(Provider<SupabaseClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public SupabaseDataSource get() {
    return newInstance(clientProvider.get());
  }

  public static SupabaseDataSource_Factory create(Provider<SupabaseClient> clientProvider) {
    return new SupabaseDataSource_Factory(clientProvider);
  }

  public static SupabaseDataSource newInstance(SupabaseClient client) {
    return new SupabaseDataSource(client);
  }
}
