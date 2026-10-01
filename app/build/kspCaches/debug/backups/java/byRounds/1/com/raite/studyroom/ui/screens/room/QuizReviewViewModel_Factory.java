package com.raite.studyroom.ui.screens.room;

import androidx.lifecycle.SavedStateHandle;
import com.raite.studyroom.data.repository.QuizRepository;
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
public final class QuizReviewViewModel_Factory implements Factory<QuizReviewViewModel> {
  private final Provider<QuizRepository> quizRepoProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public QuizReviewViewModel_Factory(Provider<QuizRepository> quizRepoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.quizRepoProvider = quizRepoProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public QuizReviewViewModel get() {
    return newInstance(quizRepoProvider.get(), savedStateHandleProvider.get());
  }

  public static QuizReviewViewModel_Factory create(Provider<QuizRepository> quizRepoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new QuizReviewViewModel_Factory(quizRepoProvider, savedStateHandleProvider);
  }

  public static QuizReviewViewModel newInstance(QuizRepository quizRepo,
      SavedStateHandle savedStateHandle) {
    return new QuizReviewViewModel(quizRepo, savedStateHandle);
  }
}
