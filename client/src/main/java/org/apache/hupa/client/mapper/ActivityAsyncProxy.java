/*
 *    Copyright 2012 Thomas Broyer <t.broyer@ltgt.net>
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
package org.apache.hupa.client.mapper;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * Makes it easy to put any {@link Activity} behind a split-point.
 * <p>
 * For simple activities, that could be done using an
 * {@link com.google.gwt.user.client.AsyncProxy AsyncProxy}, but it's not
 * possible for activities created through factories or via dependency
 * injection.
 * <p>
 * As a bonus, the {@link Activity} won't be instantiated at all in case the
 * {@link ActivityAsyncProxy} is {@link #onCancel cancelled} before the
 * {@link GWT.runAsync async call} returns.
 */
public abstract class ActivityAsyncProxy implements Activity {

    private boolean hasAsyncBeenIssued;
    private boolean hasAsyncBeenCancelled;
    private boolean hasAsyncFailed;
    private AcceptsOneWidget display;
    private EventBus eventBus;
    private Activity instance;

    @Override
    public String mayStop() {
        checkHasAsyncFailed();
        assert this.instance != null;
        return this.instance.mayStop();
    }

    @Override
    public void onCancel() {
        if (this.instance != null) {
            this.instance.onCancel();
        }
        checkHasAsyncFailed();
        this.hasAsyncBeenCancelled = true;
    }

    @Override
    public void onStop() {
        checkHasAsyncFailed();
        assert this.instance != null;
        this.instance.onStop();
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        if (this.instance != null) {
            this.instance.start(panel, eventBus);
            return;
        }
        checkHasAsyncFailed();
        assert !this.hasAsyncBeenIssued || this.hasAsyncBeenCancelled;
        this.display = panel;
        this.eventBus = eventBus;
        this.hasAsyncBeenCancelled = false;
        if (!this.hasAsyncBeenIssued) {
            this.hasAsyncBeenIssued = true;
            if (!ActivityAsyncProxy.this.hasAsyncBeenCancelled) {
                assert ActivityAsyncProxy.this.instance == null;
                ActivityAsyncProxy.this.instance = createInstance();
                ActivityAsyncProxy.this.instance.start(
                        ActivityAsyncProxy.this.display,
                        ActivityAsyncProxy.this.eventBus);
            }
//            doAsync(new RunAsyncCallback() {
//
//                @Override
//                public void onSuccess() {
//                    if (!ActivityAsyncProxy.this.hasAsyncBeenCancelled) {
//                        assert ActivityAsyncProxy.this.instance == null;
//                        ActivityAsyncProxy.this.instance = createInstance();
//                        ActivityAsyncProxy.this.instance.start(
//                                ActivityAsyncProxy.this.display,
//                                ActivityAsyncProxy.this.eventBus);
//                    }
//                }
//
//                @Override
//                public void onFailure(Throwable reason) {
//                    ActivityAsyncProxy.this.hasAsyncFailed = true;
//                    if (GWT.getUncaughtExceptionHandler() != null) {
//                        GWT.getUncaughtExceptionHandler().onUncaughtException(
//                                reason);
//                    }
//                }
//            });
        }
    }

    /**
     * Implementors should simply call {@link GWT#runAsync} here, and nothing
     * else.
     * <p>
     * This is required to have a different split-point generated for each
     * {@link ActivityAsyncProxy} sub-class.
     */
    protected abstract void doAsync(RunAsyncCallback callback);

    protected abstract Activity createInstance();

    private void checkHasAsyncFailed() {
        if (this.hasAsyncFailed) {
            throw new IllegalStateException("runAsync load previously failed");
        }
    }
}
