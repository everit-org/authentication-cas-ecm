/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.biz)
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
package org.everit.authentication.cas.ecm.internal;

import java.util.Dictionary;
import java.util.EventListener;
import java.util.Hashtable;

import javax.servlet.Filter;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;
import javax.xml.parsers.SAXParserFactory;

import org.everit.authentication.cas.CasAuthentication;
import org.everit.authentication.cas.ecm.CasAuthenticationConstants;
import org.everit.authentication.http.session.AuthenticationSessionAttributeNames;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.resource.resolver.ResourceIdResolver;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * ECM component for {@link Filter}, {@link ServletContextListener}, {@link HttpSessionListener}
 * {@link HttpSessionAttributeListener} and {@link EventListener} interfaces based on
 * {@link CasAuthentication}. The following cases are handled by this component:
 * <ul>
 * <li><b>CAS service ticket validation</b>: If the request contains a CAS service ticket, it will
 * be validated on the CAS server by invoking its service ticket validation URL. If the ticket is
 * valid and the returned principal (username) can be mapped to a Resource ID, then the Resource ID
 * will be assigned to the session.</li>
 * <li><b>CAS logout request processing</b>: If the request is a CAS logout request, then the
 * session assigned to the service ticket (received in the logout request) will be invalidated. The
 * CAS server sends the logout request asynchronously to the clients, therefore the session of the
 * logout request is not the same as the session of the user. The mapping of service tickets and
 * sessions are handled by the {@link org.everit.authentication.cas.CasHttpSessionRegistry}.</li>
 * </ul>
 * <p>
 * It is recommended to use this component in pair with
 * <a href="https://github.com/everit-org/authentication-http-session">authentication-http-session
 * </a>
 * </p>
 */
@Component(componentId = CasAuthenticationConstants.SERVICE_FACTORYPID_CAS_AUTHENTICATION,
    configurationPolicy = ConfigurationPolicy.FACTORY,
    label = "Everit CAS Authentication Component",
    description = "This component supports CAS ticket validation (login) and CAS logout request "
        + "handling (logout) and whole lifecycle of the CAS authentication in your application.")
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = CasAuthenticationConstants.DEFAULT_SERVICE_DESCRIPTION_CAS_AUTHENTICATION,
        priority = CasAuthenticationAttributePriority.P1_SERVICE_DESCRIPTION,
        label = "Service Description",
        description = "The description of this component configuration. It is used to easily "
            + "identify the service registered by this component.") })
@ManualService({
    Filter.class,
    ServletContextListener.class,
    HttpSessionListener.class,
    HttpSessionAttributeListener.class,
    EventListener.class })
public class CasAuthenticationComponent {

  private AuthenticationSessionAttributeNames authenticationSessionAttributeNames;

  /**
   * The service ticket validation URL of the CAS server.
   */
  private String casServiceTicketValidatorUrl;
  /**
   * The URL where the user will be redirected in case of failures.
   */
  private String failureUrl;

  private ResourceIdResolver resourceIdResolver;

  private SAXParserFactory saxParserFactory;

  private ServiceRegistration<?> serviceRegistration;

  /**
   * Activate method of component.
   */
  @Activate
  public void activate(final ComponentContext<CasAuthenticationComponent> componentContext) {
    Dictionary<String, Object> serviceProperties =
        new Hashtable<>(componentContext.getProperties());

    CasAuthentication casAuthentication =
        new CasAuthentication(casServiceTicketValidatorUrl, failureUrl,
            (String) serviceProperties.get(Constants.SERVICE_PID),
            authenticationSessionAttributeNames, resourceIdResolver, saxParserFactory);

    serviceRegistration = componentContext.registerService(new String[] {
        Filter.class.getName(), ServletContextListener.class.getName(),
        HttpSessionListener.class.getName(), HttpSessionAttributeListener.class.getName(),
        EventListener.class.getName() },
        casAuthentication,
        serviceProperties);
  }

  /**
   * Component deactivate method.
   */
  @Deactivate
  public void deactivate() {
    if (serviceRegistration != null) {
      serviceRegistration.unregister();
    }
  }

  @ServiceRef(attributeId = CasAuthenticationConstants.ATTR_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES,
      defaultValue = "",
      attributePriority = CasAuthenticationAttributePriority.P4_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES, // CS_DISABLE_LINE_LENGTH
      label = "AuthenticationSessionAttributeNames OSGi filter", description = "OSGi Service filter"
          + " expression for AuthenticationSessionAttributeNames instance.")
  public void setAuthenticationSessionAttributeNames(
      final AuthenticationSessionAttributeNames authenticationSessionAttributeNames) {
    this.authenticationSessionAttributeNames = authenticationSessionAttributeNames;
  }

  @StringAttribute(attributeId = CasAuthenticationConstants.ATTR_CAS_SERVICE_TICKET_VALIDATION_URL,
      defaultValue = CasAuthenticationConstants.DEFAULT_CAS_SERVICE_TICKET_VALIDATION_URL,
      priority = CasAuthenticationAttributePriority.P2_CAS_SERVICE_TICKET_VALIDATION_URL,
      label = "CAS service ticket validation URL",
      description = "The URL provided by the CAS server for service ticket validation. "
          + "HTTPS protocol (and java keystore configuration) is recommended for security reasons.")
  public void setCasServiceTicketValidatorUrl(final String casServiceTicketValidatorUrl) {
    this.casServiceTicketValidatorUrl = casServiceTicketValidatorUrl;
  }

  @StringAttribute(attributeId = CasAuthenticationConstants.ATTR_FAILURE_URL,
      defaultValue = CasAuthenticationConstants.DEFAULT_FAILURE_URL,
      priority = CasAuthenticationAttributePriority.P3_FAILURE_URL, label = "Failure URL",
      description = "The URL where the user will be redirected in case of failed request "
          + "processing.")
  public void setFailureUrl(final String failureUrl) {
    this.failureUrl = failureUrl;
  }

  @ServiceRef(attributeId = CasAuthenticationConstants.ATTR_RESOURCE_ID_RESOLVER, defaultValue = "",
      attributePriority = CasAuthenticationAttributePriority.P5_RESOURCE_ID_RESOLVER,
      label = "ResourceIdResolver OSGi filter",
      description = "OSGi Service filter expression for ResourceIdResolver instance.")
  public void setResourceIdResolver(final ResourceIdResolver resourceIdResolver) {
    this.resourceIdResolver = resourceIdResolver;
  }

  @ServiceRef(attributeId = CasAuthenticationConstants.ATTR_SAX_PARSER_FACTORY, defaultValue = "",
      attributePriority = CasAuthenticationAttributePriority.P6_SAX_PARSER_FACTORY,
      label = "SAXParserFactory OSGi filter",
      description = "OSGi Service filter expression for SAXParserFactory instance.")
  public void setSaxParserFactory(final SAXParserFactory saxParserFactory) {
    this.saxParserFactory = saxParserFactory;
  }

}
