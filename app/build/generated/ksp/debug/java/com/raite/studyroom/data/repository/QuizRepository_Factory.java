package com.raite.studyroom.data.repository;

import com.raite.studyroom.data.local.dao.QuizDao;
import com.raite.studyroom.data.remote.SupabaseContentSource;
import com.raite.studyroom.data.remote.ai.AiProxyClient;
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
public final class QuizRepository_Factory implements Factory<QuizRepository> {
  private final Provider<SupabaseContentSource> contentProvider;

  private final Provider<AiProxyClient> aiProvider;

  private final Provider<QuizDao> quizDaoProvider;

  public QuizRepository_Factory(Provider<SupabaseContentSource> contentProvider,
      Provider<AiProxyClient> aiProvider, Provider<QuizDao> quizDaoProvider) {
    this.contentProvider = contentProvider;
    this.aiProvider = aiProvider;
    this.quizDaoProvider = quizDaoProvider;
  }

  @Override
  public QuizRepository get() {
    return newInstance(contentProvider.get(), aiProvider.get(), quizDaoProvider.get());
  }

  public static QuizRepository_Factory create(Provider<SupabaseContentSource> contentProvider,
      Provider<AiProxyClient> aiProvider, Provider<QuizDao> quizDaoProvider) {
    return new QuizRepository_Factory(contentProvider, aiProvider, quizDaoProvider);
  }

  public static QuizRepository newInstance(SupabaseContentSource content, AiProxyClient ai,
      QuizDao quizDao) {
    return new QuizRepository(content, ai, quizDao);
  }
}
