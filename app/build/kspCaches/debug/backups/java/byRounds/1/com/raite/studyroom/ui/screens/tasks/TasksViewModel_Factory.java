package com.raite.studyroom.ui.screens.tasks;

import com.raite.studyroom.data.repository.AuthRepository;
import com.raite.studyroom.data.repository.TaskRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class TasksViewModel_Factory implements Factory<TasksViewModel> {
  private final Provider<AuthRepository> authProvider;

  private final Provider<TaskRepository> tasksProvider;

  public TasksViewModel_Factory(Provider<AuthRepository> authProvider,
      Provider<TaskRepository> tasksProvider) {
    this.authProvider = authProvider;
    this.tasksProvider = tasksProvider;
  }

  @Override
  public TasksViewModel get() {
    return newInstance(authProvider.get(), tasksProvider.get());
  }

  public static TasksViewModel_Factory create(Provider<AuthRepository> authProvider,
      Provider<TaskRepository> tasksProvider) {
    return new TasksViewModel_Factory(authProvider, tasksProvider);
  }

  public static TasksViewModel newInstance(AuthRepository auth, TaskRepository tasks) {
    return new TasksViewModel(auth, tasks);
  }
}
