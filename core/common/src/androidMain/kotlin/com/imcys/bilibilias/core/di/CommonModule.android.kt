package com.imcys.bilibilias.core.di

import com.imcys.bilibilias.core.storage.AndroidMediaStoreAccess
import com.imcys.bilibilias.core.storage.MediaStoreAccess
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val CommonModule: Module = module {
    factoryOf(::AndroidMediaStoreAccess) bind MediaStoreAccess::class
}