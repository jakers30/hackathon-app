package com.raite.studyroom.ui.screens.room;

import androidx.lifecycle.SavedStateHandle;
import com.raite.studyroom.data.repository.AuthRepository;
import com.raite.studyroom.data.repository.QuizRepository;
import com.raite.studyroom.data.repository.ResourceRepository;
import com.raite.studyroom.data.repository.RoomRepository;
import com.raite.studyroom.data.repository.StudyMaterialRepository;
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
public final class RoomViewModel_Factory implements Factory<RoomViewModel> {
  private final Provider<AuthRepository> authProvider;

  private final Provider<RoomRepository> roomRepoProvider;

  private final Provider<ResourceRepository> resourceRepoProvider;

  private final Provider<StudyMaterialRepository> materialRepoProvider;

  private final Provider<QuizRepository> quizRepoProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public RoomViewModel_Factory(Provider<AuthRepository> authProvider,
      Provider<RoomRepository> roomRepoProvider, Provider<ResourceRepository> resourceRepoProvider,
      Provider<StudyMaterialRepository> materialRepoProvider,
      Provider<QuizRepository> quizRepoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.authProvider = authProvider;
    this.roomRepoProvider = roomRepoProvider;
    this.resourceRepoProvider = resourceRepoProvider;
    this.materialRepoProvider = materialRepoProvider;
    this.quizRepoProvider = quizRepoProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public RoomViewModel get() {
    return newInstance(authProvider.get(), roomRepoProvider.get(), resourceRepoProvider.get(), materialRepoProvider.get(), quizRepoProvider.get(), savedStateHandleProvider.get());
  }

  public static RoomViewModel_Factory create(Provider<AuthRepository> authProvider,
      Provider<RoomRepository> roomRepoProvider, Provider<ResourceRepository> resourceRepoProvider,
      Provider<StudyMaterialRepository> materialRepoProvider,
      Provider<QuizRepository> quizRepoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new RoomViewModel_Factory(authProvider, roomRepoProvider, resourceRepoProvider, materialRepoProvider, quizRepoProvider, savedStateHandleProvider);
  }

  public static RoomViewModel newInstance(AuthRepository auth, RoomRepository roomRepo,
      ResourceRepository resourceRepo, StudyMaterialRepository materialRepo,
      QuizRepository quizRepo, SavedStateHandle savedStateHandle) {
    return new RoomViewModel(auth, roomRepo, resourceRepo, materialRepo, quizRepo, savedStateHandle);
  }
}
