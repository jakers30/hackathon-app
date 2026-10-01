package com.raite.studyroom.ui.screens.dashboard;

import com.raite.studyroom.data.repository.AuthRepository;
import com.raite.studyroom.data.repository.RoomRepository;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<AuthRepository> authProvider;

  private final Provider<RoomRepository> roomsProvider;

  private final Provider<TaskRepository> tasksProvider;

  public DashboardViewModel_Factory(Provider<AuthRepository> authProvider,
      Provider<RoomRepository> roomsProvider, Provider<TaskRepository> tasksProvider) {
    this.authProvider = authProvider;
    this.roomsProvider = roomsProvider;
    this.tasksProvider = tasksProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(authProvider.get(), roomsProvider.get(), tasksProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<AuthRepository> authProvider,
      Provider<RoomRepository> roomsProvider, Provider<TaskRepository> tasksProvider) {
    return new DashboardViewModel_Factory(authProvider, roomsProvider, tasksProvider);
  }

  public static DashboardViewModel newInstance(AuthRepository auth, RoomRepository rooms,
      TaskRepository tasks) {
    return new DashboardViewModel(auth, rooms, tasks);
  }
}
