/*
 * Copyright 2011 Red Hat, Inc. and/or its affiliates.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package org.infinispan.loaders.remote.configuration;

import java.util.Properties;

import org.infinispan.executors.DefaultExecutorFactory;
import org.infinispan.commons.configuration.Builder;
import org.infinispan.commons.executors.ExecutorFactory;
import org.infinispan.commons.util.TypedProperties;

/**
 * Configures executor factory.
 */
public class ExecutorFactoryConfigurationBuilder extends AbstractRemoteCacheStoreConfigurationChildBuilder<RemoteCacheStoreConfigurationBuilder> implements Builder<ExecutorFactoryConfiguration> {

   private ExecutorFactory factory = new DefaultExecutorFactory();
   private Properties properties;

   ExecutorFactoryConfigurationBuilder(RemoteCacheStoreConfigurationBuilder builder) {
      super(builder);
      this.properties = new Properties();
   }

   /**
    * Specify factory class for executor
    *
    * NOTE: Currently Infinispan will not use the object instance, but instead instantiate a new
    * instance of the class. Therefore, do not expect any state to survive, and provide a no-args
    * constructor to any instance. This will be resolved in Infinispan 5.2.0
    *
    * @param factory
    *           clazz
    * @return this ExecutorFactoryConfig
    */
   public ExecutorFactoryConfigurationBuilder factory(ExecutorFactory factory) {
      this.factory = factory;
      return this;
   }

   /**
    * Add key/value property pair to this executor factory configuration
    *
    * @param key
    *           property key
    * @param value
    *           property value
    * @return previous value if exists, null otherwise
    */
   public ExecutorFactoryConfigurationBuilder addExecutorProperty(String key, String value) {
      this.properties.put(key, value);
      return this;
   }

   /**
    * Set key/value properties to this executor factory configuration
    *
    * @param props
    *           Properties
    * @return this ExecutorFactoryConfig
    */
   public ExecutorFactoryConfigurationBuilder withExecutorProperties(Properties props) {
      this.properties = props;
      return this;
   }

   @Override
   public void validate() {
      // No-op, no validation required
   }

   @Override
   public ExecutorFactoryConfiguration create() {
      return new ExecutorFactoryConfiguration(factory, TypedProperties.toTypedProperties(properties));
   }

   @Override
   public ExecutorFactoryConfigurationBuilder read(ExecutorFactoryConfiguration template) {
      this.factory = template.factory();
      this.properties = template.properties();

      return this;
   }

   @Override
   public String toString() {
      return "ExecutorFactoryConfigurationBuilder{" + "factory=" + factory + ", properties=" + properties + '}';
   }

}