package com.raite.studyroom.data.repository;

import com.raite.studyroom.data.local.dao.TaskDao;
import com.raite.studyroom.data.remote.SupabaseContentSource;
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
public final class TaskRepository_Factory implements Factory<TaskRepository> {
  private final Provider<SupabaseContentSource> contentProvider;

  private final Provider<TaskDao> taskDaoProvider;

  private final Provider<Outbox> outboxProvider;

  public TaskRepository_Factory(Provider<SupabaseContentSource> contentProvider,
      Provider<TaskDao> taskDaoProvider, Provider<Outbox> outboxProvider) {
    this.contentProvider = contentProvider;
    this.taskDaoProvider = taskDaoProvider;
    this.outboxProvider = outboxProvider;
  }

  @Override
  public TaskRepository get() {
    return newInstance(contentProvider.get(), taskDaoProvider.get(), outboxProvider.get());
  }

  public static TaskRepository_Factory create(Provider<SupabaseContentSource> contentProvider,
      Provider<TaskDao> taskDaoProvider, Provider<Outbox> outboxProvider) {
    return new TaskRepository_Factory(contentProvider, taskDaoProvider, outboxProvider);
  }

  public static TaskRepository newInstance(SupabaseContentSource content, TaskDao taskDao,
      Outbox outbox) {
    return new TaskRepository(content, taskDao, outbox);
  }
}
