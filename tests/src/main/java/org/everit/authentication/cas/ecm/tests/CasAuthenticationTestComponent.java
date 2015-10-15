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

import java.io.InputStream;
import java.util.EventListener;
import java.util.Map;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.apache.http.conn.HttpHostConnectException;
import org.everit.authentication.context.AuthenticationContext;
import org.everit.osgi.dev.testrunner.TestRunnerConstants;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.osgi.framework.BundleContext;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * Test for CasAuthentication.
 */
@Component(componentId = "CasAuthenticationTest", configurationPolicy = ConfigurationPolicy.FACTORY)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = TestRunnerConstants.SERVICE_PROPERTY_TESTRUNNER_ENGINE_TYPE,
        defaultValue = "junit4"),
    @StringAttribute(attributeId = TestRunnerConstants.SERVICE_PROPERTY_TEST_ID,
        defaultValue = "CasAuthenticationTest") })
@Service(value = CasAuthenticationTestComponent.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CasAuthenticationTestComponent {

  private static final String PROP_APP1 = "app1";

  private static final String PROP_APP2 = "app2";

  private AuthenticationContext authenticationContext1;

  private AuthenticationContext authenticationContext2;

  private BundleContext bundleContext;

  private EventListener casAuthenticationEventListener1;

  private EventListener casAuthenticationEventListener2;

  private Filter casAuthenticationFilter1;

  private Filter casAuthenticationFilter2;

  private SecureHttpClient janedoe;

  private SecureHttpClient johndoe;

  private SampleApp sampleApp1;

  private SampleApp sampleApp2;

  private Filter sessionAuthenticationFilter1;

  private Filter sessionAuthenticationFilter2;

  private Servlet sessionLogoutServlet1;

  private Servlet sessionLogoutServlet2;

  /**
   * Component activator method.
   */
  @Activate
  public void activate(final BundleContext bundleContext,
      final Map<String, Object> componentProperties)
          throws Exception {

    this.bundleContext = bundleContext;

    InputStream resourceAsStream =
        getClass().getClassLoader().getResourceAsStream("META-INF/ipaddresses.properties");
    Properties properties = new Properties();
    properties.load(resourceAsStream);
    SampleApp.pingCasLoginUrl(bundleContext);

    sampleApp1 = new SampleApp(properties.getProperty(PROP_APP1),
        sessionAuthenticationFilter1,
        sessionLogoutServlet1,
        casAuthenticationFilter1,
        casAuthenticationEventListener1,
        authenticationContext1);

    sampleApp2 = new SampleApp(properties.getProperty(PROP_APP2),
        sessionAuthenticationFilter2,
        sessionLogoutServlet2,
        casAuthenticationFilter2,
        casAuthenticationEventListener2,
        authenticationContext2);
  }

  /**
   * Logout from CAS in sample apps.
   */
  @After
  public void after() throws Exception {
    if (johndoe != null) {
      if (johndoe.isLoggedIn()) {
        sampleApp1.casLogout(johndoe);
      }
      johndoe.close();
    }
    if (janedoe != null) {
      if (janedoe.isLoggedIn()) {
        sampleApp2.casLogout(janedoe);
      }
      janedoe.close();
    }
  }

  @Before
  public void before() throws Exception {
    johndoe = new SecureHttpClient(CasResourceIdResolver.JOHNDOE, bundleContext);
    janedoe = new SecureHttpClient(CasResourceIdResolver.JANEDOE, bundleContext);
  }

  /**
   * Component deactivate method.
   */
  @Deactivate
  public void deactivate() throws Exception {
    after();
    sampleApp1.deactivate();
    sampleApp2.deactivate();
  }

  @ServiceRef(defaultValue = "")
  public void setAuthenticationContext1(final AuthenticationContext authenticationContext1) {
    this.authenticationContext1 = authenticationContext1;
  }

  @ServiceRef(defaultValue = "")
  public void setAuthenticationContext2(final AuthenticationContext authenticationContext2) {
    this.authenticationContext2 = authenticationContext2;
  }

  @ServiceRef(defaultValue = "")
  public void setCasAuthenticationEventListener1(
      final EventListener casAuthenticationEventListener1) {
    this.casAuthenticationEventListener1 = casAuthenticationEventListener1;
  }

  @ServiceRef(defaultValue = "")
  public void setCasAuthenticationEventListener2(
      final EventListener casAuthenticationEventListener2) {
    this.casAuthenticationEventListener2 = casAuthenticationEventListener2;
  }

  @ServiceRef(defaultValue = "")
  public void setCasAuthenticationFilter1(final Filter casAuthenticationFilter1) {
    this.casAuthenticationFilter1 = casAuthenticationFilter1;
  }

  @ServiceRef(defaultValue = "")
  public void setCasAuthenticationFilter2(final Filter casAuthenticationFilter2) {
    this.casAuthenticationFilter2 = casAuthenticationFilter2;
  }

  @ServiceRef(defaultValue = "")
  public void setSessionAuthenticationFilter1(final Filter sessionAuthenticationFilter1) {
    this.sessionAuthenticationFilter1 = sessionAuthenticationFilter1;
  }

  @ServiceRef(defaultValue = "")
  public void setSessionAuthenticationFilter2(final Filter sessionAuthenticationFilter2) {
    this.sessionAuthenticationFilter2 = sessionAuthenticationFilter2;
  }

  @ServiceRef(defaultValue = "")
  public void setSessionLogoutServlet1(final Servlet sessionLogoutServlet1) {
    this.sessionLogoutServlet1 = sessionLogoutServlet1;
  }

  @ServiceRef(defaultValue = "")
  public void setSessionLogoutServlet2(final Servlet sessionLogoutServlet2) {
    this.sessionLogoutServlet2 = sessionLogoutServlet2;
  }

  @Test
  public void test01SingleAppAccessHelloPageWithInvalidTicket() throws Exception {
    sampleApp1.assertHello(johndoe, HelloWorldServlet.GUEST);
    sampleApp1.casLoginWithInvalidTicket(johndoe);
    Assert.assertFalse(johndoe.isLoggedIn());
  }

  @Test
  public void test02SingleAppTryLoginWithUnmappedResourceId() throws Exception {
    SecureHttpClient unknown = new SecureHttpClient(HelloWorldServlet.UNKNOWN, bundleContext);
    sampleApp1.assertHello(unknown, HelloWorldServlet.GUEST);
    sampleApp1.casLogin(unknown);
    Assert.assertFalse(unknown.isLoggedIn());
    unknown.close();
  }

  @Test
  public void test03SingleAppAccessHello() throws Exception {
    sampleApp1.assertHello(johndoe, HelloWorldServlet.GUEST);

    sampleApp1.casLogin(johndoe);
    Assert.assertTrue(johndoe.isLoggedIn());
    sampleApp1.assertHello(johndoe, CasResourceIdResolver.JOHNDOE);
    sampleApp1.casLogout(johndoe);
    Assert.assertFalse(johndoe.isLoggedIn());
    sampleApp1.assertHello(johndoe, HelloWorldServlet.GUEST);

    sampleApp1.casLogin(johndoe);
    Assert.assertTrue(johndoe.isLoggedIn());
    sampleApp1.assertHello(johndoe, CasResourceIdResolver.JOHNDOE);
    sampleApp1.sessionLogout(johndoe);
    sampleApp1.assertHello(johndoe, HelloWorldServlet.GUEST);
    sampleApp1.casLoginWithTicket(johndoe);
    sampleApp1.assertHello(johndoe, CasResourceIdResolver.JOHNDOE);
    sampleApp1.casLogout(johndoe);
    Assert.assertFalse(johndoe.isLoggedIn());
    sampleApp1.assertHello(johndoe, HelloWorldServlet.GUEST);
  }

  @Test
  public void test04SingleAppRestart() throws Exception {
    sampleApp1.assertHello(johndoe, HelloWorldServlet.GUEST);

    sampleApp1.casLogin(johndoe);
    Assert.assertTrue(johndoe.isLoggedIn());
    sampleApp1.assertHello(johndoe, CasResourceIdResolver.JOHNDOE);

    sampleApp1.stop();
    try {
      sampleApp1.assertHello(johndoe, CasResourceIdResolver.JOHNDOE);
      Assert.fail();
    } catch (HttpHostConnectException e) {
      Assert.assertTrue(e.getMessage().contains("Connection refused"));
    }
    sampleApp1.setPort(); // required to set the port of the server to the selected random port
                          // otherwise Jetty will
                          // chose an other random port
    sampleApp1.start();

    sampleApp1.assertHello(johndoe, CasResourceIdResolver.JOHNDOE);
  }

  @Test
  public void test05SingleAppMultipleClients() throws Exception {
    sampleApp1.assertHello(johndoe, HelloWorldServlet.GUEST);
    sampleApp1.assertHello(janedoe, HelloWorldServlet.GUEST);

    sampleApp1.casLogin(johndoe);
    Assert.assertTrue(johndoe.isLoggedIn());
    Assert.assertFalse(janedoe.isLoggedIn());

    sampleApp1.assertHello(johndoe, CasResourceIdResolver.JOHNDOE);
    sampleApp1.assertHello(janedoe, HelloWorldServlet.GUEST);

    sampleApp1.casLogin(janedoe);
    Assert.assertTrue(johndoe.isLoggedIn());
    Assert.assertTrue(janedoe.isLoggedIn());

    sampleApp1.assertHello(johndoe, CasResourceIdResolver.JOHNDOE);
    sampleApp1.assertHello(janedoe, CasResourceIdResolver.JANEDOE);

    sampleApp1.casLogout(johndoe);
    Assert.assertFalse(johndoe.isLoggedIn());
    Assert.assertTrue(janedoe.isLoggedIn());

    sampleApp1.assertHello(johndoe, HelloWorldServlet.GUEST);
    sampleApp1.assertHello(janedoe, CasResourceIdResolver.JANEDOE);

    sampleApp1.casLogout(janedoe);
    Assert.assertFalse(johndoe.isLoggedIn());
    Assert.assertFalse(janedoe.isLoggedIn());

    sampleApp1.assertHello(johndoe, HelloWorldServlet.GUEST);
    sampleApp1.assertHello(janedoe, HelloWorldServlet.GUEST);
  }

  @Test
  public void test06MultipleAppOneClient() throws Exception {
    sampleApp1.assertHello(johndoe, HelloWorldServlet.GUEST);
    sampleApp2.assertHello(johndoe, HelloWorldServlet.GUEST);

    sampleApp1.casLogin(johndoe);
    Assert.assertTrue(johndoe.isLoggedIn());

    sampleApp2.casLoginWithTicket(johndoe);
    Assert.assertTrue(johndoe.isLoggedIn());

    sampleApp1.assertHello(johndoe, CasResourceIdResolver.JOHNDOE);
    sampleApp2.assertHello(johndoe, CasResourceIdResolver.JOHNDOE);

    sampleApp1.casLogout(johndoe);
    Assert.assertFalse(johndoe.isLoggedIn());

    sampleApp1.assertHello(johndoe, HelloWorldServlet.GUEST);
    sampleApp2.assertHello(johndoe, HelloWorldServlet.GUEST);
  }

}
