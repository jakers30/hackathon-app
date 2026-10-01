package com.raite.studyroom.data.repository;

import android.content.Context;
import com.raite.studyroom.data.local.dao.ResourceDao;
import com.raite.studyroom.data.remote.SupabaseContentSource;
import com.raite.studyroom.data.remote.ai.AiProxyClient;
import com.raite.studyroom.data.sync.Outbox;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ResourceRepository_Factory implements Factory<ResourceRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<SupabaseContentSource> contentProvider;

  private final Provider<AiProxyClient> aiProvider;

  private final Provider<ResourceDao> resourceDaoProvider;

  private final Provider<Outbox> outboxProvider;

  public ResourceRepository_Factory(Provider<Context> contextProvider,
      Provider<SupabaseContentSource> contentProvider, Provider<AiProxyClient> aiProvider,
      Provider<ResourceDao> resourceDaoProvider, Provider<Outbox> outboxProvider) {
    this.contextProvider = contextProvider;
    this.contentProvider = contentProvider;
    this.aiProvider = aiProvider;
    this.resourceDaoProvider = resourceDaoProvider;
    this.outboxProvider = outboxProvider;
  }

  @Override
  public ResourceRepository get() {
    return newInstance(contextProvider.get(), contentProvider.get(), aiProvider.get(), resourceDaoProvider.get(), outboxProvider.get());
  }

  public static ResourceRepository_Factory create(Provider<Context> contextProvider,
      Provider<SupabaseContentSource> contentProvider, Provider<AiProxyClient> aiProvider,
      Provider<ResourceDao> resourceDaoProvider, Provider<Outbox> outboxProvider) {
    return new ResourceRepository_Factory(contextProvider, contentProvider, aiProvider, resourceDaoProvider, outboxProvider);
  }

  public static ResourceRepository newInstance(Context context, SupabaseContentSource content,
      AiProxyClient ai, ResourceDao resourceDao, Outbox outbox) {
    return new ResourceRepository(context, content, ai, resourceDao, outbox);
  }
}
