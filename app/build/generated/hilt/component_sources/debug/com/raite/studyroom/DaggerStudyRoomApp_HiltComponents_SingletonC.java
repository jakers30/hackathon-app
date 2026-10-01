package com.raite.studyroom;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.raite.studyroom.data.local.StudyRoomDatabase;
import com.raite.studyroom.data.local.dao.OutboxDao;
import com.raite.studyroom.data.local.dao.QuizDao;
import com.raite.studyroom.data.local.dao.ResourceDao;
import com.raite.studyroom.data.local.dao.RoomDao;
import com.raite.studyroom.data.local.dao.StudyMaterialDao;
import com.raite.studyroom.data.local.dao.TaskDao;
import com.raite.studyroom.data.local.dao.UserDao;
import com.raite.studyroom.data.remote.SupabaseContentSource;
import com.raite.studyroom.data.remote.SupabaseDataSource;
import com.raite.studyroom.data.remote.ai.AiProxyClient;
import com.raite.studyroom.data.repository.AuthRepository;
import com.raite.studyroom.data.repository.QuizRepository;
import com.raite.studyroom.data.repository.ResourceRepository;
import com.raite.studyroom.data.repository.RoomRepository;
import com.raite.studyroom.data.repository.SettingsRepository;
import com.raite.studyroom.data.repository.StudyMaterialRepository;
import com.raite.studyroom.data.repository.TaskRepository;
import com.raite.studyroom.data.sync.Outbox;
import com.raite.studyroom.data.sync.SyncScheduler;
import com.raite.studyroom.data.sync.SyncWorker;
import com.raite.studyroom.data.sync.SyncWorker_AssistedFactory;
import com.raite.studyroom.di.DatabaseModule_ProvideDatabaseFactory;
import com.raite.studyroom.di.DatabaseModule_ProvideOutboxDaoFactory;
import com.raite.studyroom.di.DatabaseModule_ProvideQuizDaoFactory;
import com.raite.studyroom.di.DatabaseModule_ProvideResourceDaoFactory;
import com.raite.studyroom.di.DatabaseModule_ProvideRoomDaoFactory;
import com.raite.studyroom.di.DatabaseModule_ProvideStudyMaterialDaoFactory;
import com.raite.studyroom.di.DatabaseModule_ProvideTaskDaoFactory;
import com.raite.studyroom.di.DatabaseModule_ProvideUserDaoFactory;
import com.raite.studyroom.di.NetworkModule_ProvideHttpClientFactory;
import com.raite.studyroom.di.NetworkModule_ProvideSupabaseClientFactory;
import com.raite.studyroom.ui.MainViewModel;
import com.raite.studyroom.ui.MainViewModel_HiltModules;
import com.raite.studyroom.ui.SessionViewModel;
import com.raite.studyroom.ui.SessionViewModel_HiltModules;
import com.raite.studyroom.ui.screens.auth.AuthViewModel;
import com.raite.studyroom.ui.screens.auth.AuthViewModel_HiltModules;
import com.raite.studyroom.ui.screens.dashboard.DashboardViewModel;
import com.raite.studyroom.ui.screens.dashboard.DashboardViewModel_HiltModules;
import com.raite.studyroom.ui.screens.room.QuizReviewViewModel;
import com.raite.studyroom.ui.screens.room.QuizReviewViewModel_HiltModules;
import com.raite.studyroom.ui.screens.room.RoomViewModel;
import com.raite.studyroom.ui.screens.room.RoomViewModel_HiltModules;
import com.raite.studyroom.ui.screens.rooms.CreateRoomViewModel;
import com.raite.studyroom.ui.screens.rooms.CreateRoomViewModel_HiltModules;
import com.raite.studyroom.ui.screens.rooms.RoomsViewModel;
import com.raite.studyroom.ui.screens.rooms.RoomsViewModel_HiltModules;
import com.raite.studyroom.ui.screens.settings.SettingsViewModel;
import com.raite.studyroom.ui.screens.settings.SettingsViewModel_HiltModules;
import com.raite.studyroom.ui.screens.tasks.TasksViewModel;
import com.raite.studyroom.ui.screens.tasks.TasksViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SingleCheck;
import io.github.jan.supabase.SupabaseClient;
import io.ktor.client.HttpClient;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerStudyRoomApp_HiltComponents_SingletonC {
  private DaggerStudyRoomApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public StudyRoomApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements StudyRoomApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public StudyRoomApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements StudyRoomApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public StudyRoomApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements StudyRoomApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public StudyRoomApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements StudyRoomApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public StudyRoomApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements StudyRoomApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public StudyRoomApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements StudyRoomApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public StudyRoomApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements StudyRoomApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public StudyRoomApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends StudyRoomApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends StudyRoomApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends StudyRoomApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends StudyRoomApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(10).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_auth_AuthViewModel, AuthViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_rooms_CreateRoomViewModel, CreateRoomViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_dashboard_DashboardViewModel, DashboardViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_raite_studyroom_ui_MainViewModel, MainViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_room_QuizReviewViewModel, QuizReviewViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_room_RoomViewModel, RoomViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_rooms_RoomsViewModel, RoomsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_raite_studyroom_ui_SessionViewModel, SessionViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_tasks_TasksViewModel, TasksViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_raite_studyroom_ui_screens_room_RoomViewModel = "com.raite.studyroom.ui.screens.room.RoomViewModel";

      static String com_raite_studyroom_ui_screens_settings_SettingsViewModel = "com.raite.studyroom.ui.screens.settings.SettingsViewModel";

      static String com_raite_studyroom_ui_SessionViewModel = "com.raite.studyroom.ui.SessionViewModel";

      static String com_raite_studyroom_ui_screens_auth_AuthViewModel = "com.raite.studyroom.ui.screens.auth.AuthViewModel";

      static String com_raite_studyroom_ui_screens_rooms_CreateRoomViewModel = "com.raite.studyroom.ui.screens.rooms.CreateRoomViewModel";

      static String com_raite_studyroom_ui_screens_rooms_RoomsViewModel = "com.raite.studyroom.ui.screens.rooms.RoomsViewModel";

      static String com_raite_studyroom_ui_MainViewModel = "com.raite.studyroom.ui.MainViewModel";

      static String com_raite_studyroom_ui_screens_tasks_TasksViewModel = "com.raite.studyroom.ui.screens.tasks.TasksViewModel";

      static String com_raite_studyroom_ui_screens_room_QuizReviewViewModel = "com.raite.studyroom.ui.screens.room.QuizReviewViewModel";

      static String com_raite_studyroom_ui_screens_dashboard_DashboardViewModel = "com.raite.studyroom.ui.screens.dashboard.DashboardViewModel";

      @KeepFieldType
      RoomViewModel com_raite_studyroom_ui_screens_room_RoomViewModel2;

      @KeepFieldType
      SettingsViewModel com_raite_studyroom_ui_screens_settings_SettingsViewModel2;

      @KeepFieldType
      SessionViewModel com_raite_studyroom_ui_SessionViewModel2;

      @KeepFieldType
      AuthViewModel com_raite_studyroom_ui_screens_auth_AuthViewModel2;

      @KeepFieldType
      CreateRoomViewModel com_raite_studyroom_ui_screens_rooms_CreateRoomViewModel2;

      @KeepFieldType
      RoomsViewModel com_raite_studyroom_ui_screens_rooms_RoomsViewModel2;

      @KeepFieldType
      MainViewModel com_raite_studyroom_ui_MainViewModel2;

      @KeepFieldType
      TasksViewModel com_raite_studyroom_ui_screens_tasks_TasksViewModel2;

      @KeepFieldType
      QuizReviewViewModel com_raite_studyroom_ui_screens_room_QuizReviewViewModel2;

      @KeepFieldType
      DashboardViewModel com_raite_studyroom_ui_screens_dashboard_DashboardViewModel2;
    }
  }

  private static final class ViewModelCImpl extends StudyRoomApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AuthViewModel> authViewModelProvider;

    private Provider<CreateRoomViewModel> createRoomViewModelProvider;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<MainViewModel> mainViewModelProvider;

    private Provider<QuizReviewViewModel> quizReviewViewModelProvider;

    private Provider<RoomViewModel> roomViewModelProvider;

    private Provider<RoomsViewModel> roomsViewModelProvider;

    private Provider<SessionViewModel> sessionViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<TasksViewModel> tasksViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.authViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.createRoomViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.mainViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.quizReviewViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.roomViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.roomsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.sessionViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.tasksViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(10).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_auth_AuthViewModel, ((Provider) authViewModelProvider)).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_rooms_CreateRoomViewModel, ((Provider) createRoomViewModelProvider)).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_dashboard_DashboardViewModel, ((Provider) dashboardViewModelProvider)).put(LazyClassKeyProvider.com_raite_studyroom_ui_MainViewModel, ((Provider) mainViewModelProvider)).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_room_QuizReviewViewModel, ((Provider) quizReviewViewModelProvider)).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_room_RoomViewModel, ((Provider) roomViewModelProvider)).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_rooms_RoomsViewModel, ((Provider) roomsViewModelProvider)).put(LazyClassKeyProvider.com_raite_studyroom_ui_SessionViewModel, ((Provider) sessionViewModelProvider)).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).put(LazyClassKeyProvider.com_raite_studyroom_ui_screens_tasks_TasksViewModel, ((Provider) tasksViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_raite_studyroom_ui_screens_rooms_RoomsViewModel = "com.raite.studyroom.ui.screens.rooms.RoomsViewModel";

      static String com_raite_studyroom_ui_screens_tasks_TasksViewModel = "com.raite.studyroom.ui.screens.tasks.TasksViewModel";

      static String com_raite_studyroom_ui_screens_room_QuizReviewViewModel = "com.raite.studyroom.ui.screens.room.QuizReviewViewModel";

      static String com_raite_studyroom_ui_screens_rooms_CreateRoomViewModel = "com.raite.studyroom.ui.screens.rooms.CreateRoomViewModel";

      static String com_raite_studyroom_ui_screens_settings_SettingsViewModel = "com.raite.studyroom.ui.screens.settings.SettingsViewModel";

      static String com_raite_studyroom_ui_screens_auth_AuthViewModel = "com.raite.studyroom.ui.screens.auth.AuthViewModel";

      static String com_raite_studyroom_ui_MainViewModel = "com.raite.studyroom.ui.MainViewModel";

      static String com_raite_studyroom_ui_screens_dashboard_DashboardViewModel = "com.raite.studyroom.ui.screens.dashboard.DashboardViewModel";

      static String com_raite_studyroom_ui_screens_room_RoomViewModel = "com.raite.studyroom.ui.screens.room.RoomViewModel";

      static String com_raite_studyroom_ui_SessionViewModel = "com.raite.studyroom.ui.SessionViewModel";

      @KeepFieldType
      RoomsViewModel com_raite_studyroom_ui_screens_rooms_RoomsViewModel2;

      @KeepFieldType
      TasksViewModel com_raite_studyroom_ui_screens_tasks_TasksViewModel2;

      @KeepFieldType
      QuizReviewViewModel com_raite_studyroom_ui_screens_room_QuizReviewViewModel2;

      @KeepFieldType
      CreateRoomViewModel com_raite_studyroom_ui_screens_rooms_CreateRoomViewModel2;

      @KeepFieldType
      SettingsViewModel com_raite_studyroom_ui_screens_settings_SettingsViewModel2;

      @KeepFieldType
      AuthViewModel com_raite_studyroom_ui_screens_auth_AuthViewModel2;

      @KeepFieldType
      MainViewModel com_raite_studyroom_ui_MainViewModel2;

      @KeepFieldType
      DashboardViewModel com_raite_studyroom_ui_screens_dashboard_DashboardViewModel2;

      @KeepFieldType
      RoomViewModel com_raite_studyroom_ui_screens_room_RoomViewModel2;

      @KeepFieldType
      SessionViewModel com_raite_studyroom_ui_SessionViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.raite.studyroom.ui.screens.auth.AuthViewModel 
          return (T) new AuthViewModel(singletonCImpl.authRepositoryProvider.get());

          case 1: // com.raite.studyroom.ui.screens.rooms.CreateRoomViewModel 
          return (T) new CreateRoomViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.roomRepositoryProvider.get());

          case 2: // com.raite.studyroom.ui.screens.dashboard.DashboardViewModel 
          return (T) new DashboardViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.roomRepositoryProvider.get(), singletonCImpl.taskRepositoryProvider.get());

          case 3: // com.raite.studyroom.ui.MainViewModel 
          return (T) new MainViewModel(singletonCImpl.settingsRepositoryProvider.get());

          case 4: // com.raite.studyroom.ui.screens.room.QuizReviewViewModel 
          return (T) new QuizReviewViewModel(singletonCImpl.quizRepositoryProvider.get(), viewModelCImpl.savedStateHandle);

          case 5: // com.raite.studyroom.ui.screens.room.RoomViewModel 
          return (T) new RoomViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.roomRepositoryProvider.get(), singletonCImpl.resourceRepositoryProvider.get(), singletonCImpl.studyMaterialRepositoryProvider.get(), singletonCImpl.quizRepositoryProvider.get(), viewModelCImpl.savedStateHandle);

          case 6: // com.raite.studyroom.ui.screens.rooms.RoomsViewModel 
          return (T) new RoomsViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.roomRepositoryProvider.get());

          case 7: // com.raite.studyroom.ui.SessionViewModel 
          return (T) new SessionViewModel(singletonCImpl.authRepositoryProvider.get());

          case 8: // com.raite.studyroom.ui.screens.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.settingsRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get(), singletonCImpl.syncSchedulerProvider.get(), singletonCImpl.outboxProvider.get());

          case 9: // com.raite.studyroom.ui.screens.tasks.TasksViewModel 
          return (T) new TasksViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.taskRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends StudyRoomApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends StudyRoomApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends StudyRoomApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<StudyRoomDatabase> provideDatabaseProvider;

    private Provider<SupabaseClient> provideSupabaseClientProvider;

    private Provider<SupabaseContentSource> supabaseContentSourceProvider;

    private Provider<SupabaseDataSource> supabaseDataSourceProvider;

    private Provider<SettingsRepository> settingsRepositoryProvider;

    private Provider<SyncWorker_AssistedFactory> syncWorker_AssistedFactoryProvider;

    private Provider<AuthRepository> authRepositoryProvider;

    private Provider<RoomRepository> roomRepositoryProvider;

    private Provider<Outbox> outboxProvider;

    private Provider<TaskRepository> taskRepositoryProvider;

    private Provider<HttpClient> provideHttpClientProvider;

    private Provider<AiProxyClient> aiProxyClientProvider;

    private Provider<QuizRepository> quizRepositoryProvider;

    private Provider<ResourceRepository> resourceRepositoryProvider;

    private Provider<StudyMaterialRepository> studyMaterialRepositoryProvider;

    private Provider<SyncScheduler> syncSchedulerProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private OutboxDao outboxDao() {
      return DatabaseModule_ProvideOutboxDaoFactory.provideOutboxDao(provideDatabaseProvider.get());
    }

    private ResourceDao resourceDao() {
      return DatabaseModule_ProvideResourceDaoFactory.provideResourceDao(provideDatabaseProvider.get());
    }

    private Map<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return Collections.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>singletonMap("com.raite.studyroom.data.sync.SyncWorker", ((Provider) syncWorker_AssistedFactoryProvider));
    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    private UserDao userDao() {
      return DatabaseModule_ProvideUserDaoFactory.provideUserDao(provideDatabaseProvider.get());
    }

    private RoomDao roomDao() {
      return DatabaseModule_ProvideRoomDaoFactory.provideRoomDao(provideDatabaseProvider.get());
    }

    private TaskDao taskDao() {
      return DatabaseModule_ProvideTaskDaoFactory.provideTaskDao(provideDatabaseProvider.get());
    }

    private QuizDao quizDao() {
      return DatabaseModule_ProvideQuizDaoFactory.provideQuizDao(provideDatabaseProvider.get());
    }

    private StudyMaterialDao studyMaterialDao() {
      return DatabaseModule_ProvideStudyMaterialDaoFactory.provideStudyMaterialDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<StudyRoomDatabase>(singletonCImpl, 1));
      this.provideSupabaseClientProvider = DoubleCheck.provider(new SwitchingProvider<SupabaseClient>(singletonCImpl, 3));
      this.supabaseContentSourceProvider = DoubleCheck.provider(new SwitchingProvider<SupabaseContentSource>(singletonCImpl, 2));
      this.supabaseDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<SupabaseDataSource>(singletonCImpl, 4));
      this.settingsRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SettingsRepository>(singletonCImpl, 5));
      this.syncWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<SyncWorker_AssistedFactory>(singletonCImpl, 0));
      this.authRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepository>(singletonCImpl, 6));
      this.roomRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<RoomRepository>(singletonCImpl, 7));
      this.outboxProvider = DoubleCheck.provider(new SwitchingProvider<Outbox>(singletonCImpl, 9));
      this.taskRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<TaskRepository>(singletonCImpl, 8));
      this.provideHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<HttpClient>(singletonCImpl, 12));
      this.aiProxyClientProvider = DoubleCheck.provider(new SwitchingProvider<AiProxyClient>(singletonCImpl, 11));
      this.quizRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<QuizRepository>(singletonCImpl, 10));
      this.resourceRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ResourceRepository>(singletonCImpl, 13));
      this.studyMaterialRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<StudyMaterialRepository>(singletonCImpl, 14));
      this.syncSchedulerProvider = DoubleCheck.provider(new SwitchingProvider<SyncScheduler>(singletonCImpl, 15));
    }

    @Override
    public void injectStudyRoomApp(StudyRoomApp studyRoomApp) {
      injectStudyRoomApp2(studyRoomApp);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private StudyRoomApp injectStudyRoomApp2(StudyRoomApp instance) {
      StudyRoomApp_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.raite.studyroom.data.sync.SyncWorker_AssistedFactory 
          return (T) new SyncWorker_AssistedFactory() {
            @Override
            public SyncWorker create(Context appContext, WorkerParameters params) {
              return new SyncWorker(appContext, params, singletonCImpl.outboxDao(), singletonCImpl.resourceDao(), singletonCImpl.supabaseContentSourceProvider.get(), singletonCImpl.supabaseDataSourceProvider.get(), singletonCImpl.settingsRepositoryProvider.get());
            }
          };

          case 1: // com.raite.studyroom.data.local.StudyRoomDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.raite.studyroom.data.remote.SupabaseContentSource 
          return (T) new SupabaseContentSource(singletonCImpl.provideSupabaseClientProvider.get());

          case 3: // io.github.jan.supabase.SupabaseClient 
          return (T) NetworkModule_ProvideSupabaseClientFactory.provideSupabaseClient();

          case 4: // com.raite.studyroom.data.remote.SupabaseDataSource 
          return (T) new SupabaseDataSource(singletonCImpl.provideSupabaseClientProvider.get());

          case 5: // com.raite.studyroom.data.repository.SettingsRepository 
          return (T) new SettingsRepository(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 6: // com.raite.studyroom.data.repository.AuthRepository 
          return (T) new AuthRepository(singletonCImpl.supabaseDataSourceProvider.get(), singletonCImpl.userDao());

          case 7: // com.raite.studyroom.data.repository.RoomRepository 
          return (T) new RoomRepository(singletonCImpl.supabaseDataSourceProvider.get(), singletonCImpl.roomDao());

          case 8: // com.raite.studyroom.data.repository.TaskRepository 
          return (T) new TaskRepository(singletonCImpl.supabaseContentSourceProvider.get(), singletonCImpl.taskDao(), singletonCImpl.outboxProvider.get());

          case 9: // com.raite.studyroom.data.sync.Outbox 
          return (T) new Outbox(singletonCImpl.outboxDao());

          case 10: // com.raite.studyroom.data.repository.QuizRepository 
          return (T) new QuizRepository(singletonCImpl.supabaseContentSourceProvider.get(), singletonCImpl.aiProxyClientProvider.get(), singletonCImpl.quizDao());

          case 11: // com.raite.studyroom.data.remote.ai.AiProxyClient 
          return (T) new AiProxyClient(singletonCImpl.provideHttpClientProvider.get(), singletonCImpl.provideSupabaseClientProvider.get());

          case 12: // io.ktor.client.HttpClient 
          return (T) NetworkModule_ProvideHttpClientFactory.provideHttpClient();

          case 13: // com.raite.studyroom.data.repository.ResourceRepository 
          return (T) new ResourceRepository(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.supabaseContentSourceProvider.get(), singletonCImpl.aiProxyClientProvider.get(), singletonCImpl.resourceDao(), singletonCImpl.outboxProvider.get());

          case 14: // com.raite.studyroom.data.repository.StudyMaterialRepository 
          return (T) new StudyMaterialRepository(singletonCImpl.supabaseContentSourceProvider.get(), singletonCImpl.aiProxyClientProvider.get(), singletonCImpl.studyMaterialDao(), singletonCImpl.outboxProvider.get());

          case 15: // com.raite.studyroom.data.sync.SyncScheduler 
          return (T) new SyncScheduler(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
