/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jclouds.karaf.cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jclouds.karaf.cache.tasks.UpdateCachesTask;

public class CacheManager<T> implements Runnable {

    protected ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    protected final List<Cacheable<T>> cacheables = new LinkedList<Cacheable<T>> ();
    protected final Map<String, T> services = new HashMap<String, T>();

    private UpdateCachesTask updatesCachesTask;

    public void init() {
      scheduledExecutorService.scheduleAtFixedRate(this,0,5, TimeUnit.MINUTES);
    }
    public void destroy() {
      if (updatesCachesTask != null) {
        updatesCachesTask.stop();
      }
      scheduledExecutorService.shutdownNow();
    }

    @Override
    public void run() {
        //Update all cacheables for all services.
        this.updatesCachesTask = new UpdateCachesTask(cacheables,services);
        this.updatesCachesTask.run();
    }
}
