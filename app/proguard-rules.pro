# Keep kotlinx.serialization generated serializers.
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class com.raite.studyroom.** {
    *** Companion;
}
-keepclasseswithmembers class com.raite.studyroom.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Supabase / Ktor
-dontwarn org.slf4j.**
-dontwarn io.ktor.**
