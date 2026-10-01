package com.raite.studyroom.data.sync;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.raite.studyroom.data.local.dao.OutboxDao;
import com.raite.studyroom.data.local.dao.ResourceDao;
import com.raite.studyroom.data.remote.SupabaseContentSource;
import com.raite.studyroom.data.remote.SupabaseDataSource;
import com.raite.studyroom.data.repository.SettingsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class SyncWorker_Factory {
  private final Provider<OutboxDao> outboxDaoProvider;

  private final Provider<ResourceDao> resourceDaoProvider;

  private final Provider<SupabaseContentSource> contentProvider;

  private final Provider<SupabaseDataSource> roomsProvider;

  private final Provider<SettingsRepository> settingsProvider;

  public SyncWorker_Factory(Provider<OutboxDao> outboxDaoProvider,
      Provider<ResourceDao> resourceDaoProvider, Provider<SupabaseContentSource> contentProvider,
      Provider<SupabaseDataSource> roomsProvider, Provider<SettingsRepository> settingsProvider) {
    this.outboxDaoProvider = outboxDaoProvider;
    this.resourceDaoProvider = resourceDaoProvider;
    this.contentProvider = contentProvider;
    this.roomsProvider = roomsProvider;
    this.settingsProvider = settingsProvider;
  }

  public SyncWorker get(Context appContext, WorkerParameters params) {
    return newInstance(appContext, params, outboxDaoProvider.get(), resourceDaoProvider.get(), contentProvider.get(), roomsProvider.get(), settingsProvider.get());
  }

  public static SyncWorker_Factory create(Provider<OutboxDao> outboxDaoProvider,
      Provider<ResourceDao> resourceDaoProvider, Provider<SupabaseContentSource> contentProvider,
      Provider<SupabaseDataSource> roomsProvider, Provider<SettingsRepository> settingsProvider) {
    return new SyncWorker_Factory(outboxDaoProvider, resourceDaoProvider, contentProvider, roomsProvider, settingsProvider);
  }

  public static SyncWorker newInstance(Context appContext, WorkerParameters params,
      OutboxDao outboxDao, ResourceDao resourceDao, SupabaseContentSource content,
      SupabaseDataSource rooms, SettingsRepository settings) {
    return new SyncWorker(appContext, params, outboxDao, resourceDao, content, rooms, settings);
  }
}
