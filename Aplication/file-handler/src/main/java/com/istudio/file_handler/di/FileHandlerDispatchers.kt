package com.istudio.file_handler.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val fileHandlerAppDispatchers: FileHandlerAppDispatchers)

enum class FileHandlerAppDispatchers {
  IO
}
