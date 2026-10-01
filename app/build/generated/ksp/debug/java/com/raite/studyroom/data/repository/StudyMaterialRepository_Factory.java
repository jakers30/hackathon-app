package com.raite.studyroom.data.repository;

import com.raite.studyroom.data.local.dao.StudyMaterialDao;
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
public final class StudyMaterialRepository_Factory implements Factory<StudyMaterialRepository> {
  private final Provider<SupabaseContentSource> contentProvider;

  private final Provider<AiProxyClient> aiProvider;

  private final Provider<StudyMaterialDao> daoProvider;

  private final Provider<Outbox> outboxProvider;

  public StudyMaterialRepository_Factory(Provider<SupabaseContentSource> contentProvider,
      Provider<AiProxyClient> aiProvider, Provider<StudyMaterialDao> daoProvider,
      Provider<Outbox> outboxProvider) {
    this.contentProvider = contentProvider;
    this.aiProvider = aiProvider;
    this.daoProvider = daoProvider;
    this.outboxProvider = outboxProvider;
  }

  @Override
  public StudyMaterialRepository get() {
    return newInstance(contentProvider.get(), aiProvider.get(), daoProvider.get(), outboxProvider.get());
  }

  public static StudyMaterialRepository_Factory create(
      Provider<SupabaseContentSource> contentProvider, Provider<AiProxyClient> aiProvider,
      Provider<StudyMaterialDao> daoProvider, Provider<Outbox> outboxProvider) {
    return new StudyMaterialRepository_Factory(contentProvider, aiProvider, daoProvider, outboxProvider);
  }

  public static StudyMaterialRepository newInstance(SupabaseContentSource content, AiProxyClient ai,
      StudyMaterialDao dao, Outbox outbox) {
    return new StudyMaterialRepository(content, ai, dao, outbox);
  }
}
