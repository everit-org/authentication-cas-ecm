/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.authentication.cas.ecm.tests;

import java.util.Optional;

import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.resource.resolver.ResourceIdResolver;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * Simple implementation of the {@link ResourceIdResolver}.
 */
@Component(componentId = "CasResourceIdResolver", configurationPolicy = ConfigurationPolicy.IGNORE)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@Service
public class CasResourceIdResolver implements ResourceIdResolver {

  public static final String JANEDOE = "janedoe";

  public static final Optional<Long> JANEDOE_RESOURCE_ID = Optional.of(456L);

  public static final String JOHNDOE = "johndoe";

  public static final Optional<Long> JOHNDOE_RESOURCE_ID = Optional.of(123L);

  @Override
  public Optional<Long> getResourceId(final String uniqueIdentifier) {
    if (uniqueIdentifier.equals(JOHNDOE)) {
      return JOHNDOE_RESOURCE_ID;
    } else if (uniqueIdentifier.equals(JANEDOE)) {
      return JANEDOE_RESOURCE_ID;
    }
    return Optional.empty();
  }

}
