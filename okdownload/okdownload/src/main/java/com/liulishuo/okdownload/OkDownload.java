/*
 * Copyright (c) 2017 LingoChamp Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liulishuo.okdownload;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BreakpointStore;
import com.liulishuo.okdownload.core.breakpoint.DownloadStore;
import com.liulishuo.okdownload.core.connection.DownloadConnection;
import com.liulishuo.okdownload.core.dispatcher.CallbackDispatcher;
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher;
import com.liulishuo.okdownload.core.download.DownloadStrategy;
import com.liulishuo.okdownload.core.file.DownloadOutputStream;
import com.liulishuo.okdownload.core.file.DownloadUriOutputStream;
import com.liulishuo.okdownload.core.file.ProcessFileStrategy;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
public class OkDownload {

    @SuppressLint("StaticFieldLeak")
    static volatile OkDownload singleton;

    private final DownloadDispatcher downloadDispatcher;
    private final CallbackDispatcher callbackDispatcher;
    private final BreakpointStore breakpointStore;
    private final DownloadConnection.Factory connectionFactory;
    private final DownloadOutputStream.Factory outputStreamFactory;
    private final ProcessFileStrategy processFileStrategy;
    private final DownloadStrategy downloadStrategy;
    public final ExecutorService executorService;
    private final Context context;

    @Nullable
    DownloadMonitor monitor;

    OkDownload(
            Context context,
            DownloadDispatcher downloadDispatcher,
            CallbackDispatcher callbackDispatcher,
            DownloadStore store,
            DownloadConnection.Factory connectionFactory,
            DownloadOutputStream.Factory outputStreamFactory,
            ProcessFileStrategy processFileStrategy,
            DownloadStrategy downloadStrategy,
            ExecutorService executorService
    ) {
        this.context = context;
        this.downloadDispatcher = downloadDispatcher;
        this.callbackDispatcher = callbackDispatcher;
        this.breakpointStore = store;
        this.connectionFactory = connectionFactory;
        this.outputStreamFactory = outputStreamFactory;
        this.processFileStrategy = processFileStrategy;
        this.downloadStrategy = downloadStrategy;
        this.executorService = executorService;
        this.downloadDispatcher.setDownloadStore(Util.createRemitDatabase(store));
    }

    public DownloadDispatcher downloadDispatcher() {
        return downloadDispatcher;
    }

    public CallbackDispatcher callbackDispatcher() {
        return callbackDispatcher;
    }

    public BreakpointStore breakpointStore() {
        return breakpointStore;
    }

    public DownloadConnection.Factory connectionFactory() {
        return connectionFactory;
    }

    public DownloadOutputStream.Factory outputStreamFactory() {
        return outputStreamFactory;
    }

    public ProcessFileStrategy processFileStrategy() {
        return processFileStrategy;
    }

    public DownloadStrategy downloadStrategy() {
        return downloadStrategy;
    }

    public Context context() {
        return this.context;
    }

    public void setMonitor(@Nullable DownloadMonitor monitor) {
        this.monitor = monitor;
    }

    @Nullable
    public DownloadMonitor getMonitor() {
        return monitor;
    }

    public static OkDownload with() {
        if (singleton == null) {
            throw new IllegalStateException("OkDownload == null");
        }
        return singleton;
    }

    public static void setSingletonInstance(@NonNull OkDownload okDownload) {
        if (singleton != null) {
            throw new IllegalArgumentException("OkDownload must be null.");
        }

        synchronized (OkDownload.class) {
            if (singleton != null) {
                throw new IllegalArgumentException("OkDownload must be null.");
            }
            singleton = okDownload;
        }
    }

    public static class Builder {
        private final Context context;
        private DownloadDispatcher downloadDispatcher = new DownloadDispatcher();
        private CallbackDispatcher callbackDispatcher = new CallbackDispatcher();
        private DownloadConnection.Factory connectionFactory = Util.createDefaultConnectionFactory();
        private ProcessFileStrategy processFileStrategy = new ProcessFileStrategy();
        private DownloadStrategy downloadStrategy = new DownloadStrategy();
        private DownloadOutputStream.Factory outputStreamFactory = new DownloadUriOutputStream.Factory();
        private ExecutorService executorService = new ThreadPoolExecutor(0,
                Integer.MAX_VALUE,
                60,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                Util.threadFactory("OkDownload", false)
        );

        private DownloadStore downloadStore;
        private DownloadMonitor monitor;

        public Builder(@NonNull Context context) {
            this.context = context.getApplicationContext();
            downloadStore = Util.createDefaultDatabase(context);
        }

        public Builder downloadDispatcher(@NonNull DownloadDispatcher downloadDispatcher) {
            this.downloadDispatcher = downloadDispatcher;
            return this;
        }

        public Builder callbackDispatcher(@NonNull CallbackDispatcher callbackDispatcher) {
            this.callbackDispatcher = callbackDispatcher;
            return this;
        }

        public Builder downloadStore(@NonNull DownloadStore downloadStore) {
            this.downloadStore = downloadStore;
            return this;
        }

        public Builder connectionFactory(@NonNull DownloadConnection.Factory connectionFactory) {
            this.connectionFactory = connectionFactory;
            return this;
        }

        public Builder outputStreamFactory(@NonNull DownloadOutputStream.Factory outputStreamFactory) {
            this.outputStreamFactory = outputStreamFactory;
            return this;
        }

        public Builder processFileStrategy(@NonNull ProcessFileStrategy processFileStrategy) {
            this.processFileStrategy = processFileStrategy;
            return this;
        }

        public Builder downloadStrategy(@NonNull DownloadStrategy downloadStrategy) {
            this.downloadStrategy = downloadStrategy;
            return this;
        }

        public Builder monitor(@NonNull DownloadMonitor monitor) {
            this.monitor = monitor;
            return this;
        }

        public Builder executor(@NonNull ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        public OkDownload build() {
            OkDownload okDownload = new OkDownload(
                    context,
                    downloadDispatcher,
                    callbackDispatcher,
                    downloadStore,
                    connectionFactory,
                    outputStreamFactory,
                    processFileStrategy,
                    downloadStrategy,
                    executorService
            );

            okDownload.setMonitor(monitor);

            Util.d("OkDownload", "downloadStore[" + downloadStore + "] connectionFactory["
                    + connectionFactory);
            return okDownload;
        }
    }
}
