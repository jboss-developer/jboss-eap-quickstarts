/*
 * JBoss, Home of Professional Open Source
 * Copyright 2018, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.datagrid.eap.subsystem.web;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.infinispan.Cache;
import org.infinispan.container.entries.CacheEntry;
import org.jboss.logging.Logger;

/**
 * A simple JSF controller to show how the RHDG cache configured with EAP infinispan subsystem is used.
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Model
public class JsfController {
  private static final Logger LOGGER = Logger.getLogger(JsfController.class);
  private CacheView cacheView;

//   @Resource(lookup = "java:jboss/infinispan/container/jdg-container")
//   CacheContainer container;   // container could be injected if needed

  /**
   * The direct lookup like
   *
   * @Resource(lookup = "java:jboss/infinispan/cache/jdg-container/EAPcache")
   *
   * is not working because of https://issues.redhat.com/browse/JBEAP-22290.
   * so the indirection with <resource-ref> in web.xml is needed until the bug is fixed.
   * Also the boss-deployment-structure.xml is necessary to have the dependency for org.infinispan classes otherwise there might be confusing failures
   */
  @Resource(name = "ApplicationCache")
  Cache<String, String> cache;

  /**
   * Initialize the controller.
   */
  @PostConstruct
  public void init() {
    LOGGER.debug("init");
    initForm();
  }

  public void initForm() {
    this.cacheView = new CacheView();
  }

  @Produces
  @Named
  public CacheView getCache() {
    return this.cacheView;
  }

  public void add() {
    LOGGER.info("Try to add entry key=" + cacheView.getKey());
    cache.put(cacheView.getKey(), cacheView.getValue());
    getSize();
  }

  public void get() {
    String key = cacheView.getKey();
    LOGGER.info("Try to read key=" + key);
    CacheEntry<String, String> cacheEntry = cache.getAdvancedCache().getCacheEntry(key);
    if (cacheEntry == null) {
      cacheView.setValue("NOT AVAILABLE");
    } else {
      cacheView.setValue(cacheEntry.getValue());
      cacheView.setLifespan(cacheEntry.getLifespan());
      cacheView.setMaxIdle(cacheEntry.getMaxIdle());
      cacheView.setCreation(cacheEntry.getCreated());
      cacheView.setLastUsed(cacheEntry.getLastUsed());
    }
    getSize();
  }

  public void delete() {
    String key = cacheView.getKey();
    LOGGER.info("Try to delete entry key=" + key);
    cache.remove(key);
  }

  public void list() {
    LOGGER.info("Try to list items");
    StringBuilder result = new StringBuilder();
    // read cache
    for (String key : cache.keySet()) {
      result.append(key + "  :  ");
      result.append(cache.get(key));
      result.append(" || ");
    }
    cacheView.setEntries(result.toString());
  }

  public void getSize() {
    LOGGER.info("size is " + cache.size());
    cacheView.setSize(cache.size());
  }

}
