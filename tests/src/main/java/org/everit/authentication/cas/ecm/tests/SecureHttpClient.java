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

import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.osgi.framework.BundleContext;

/**
 * Secure HttpClient implementation.
 */
public class SecureHttpClient {

  private final CloseableHttpClient httpClient;

  private final HttpClientContext httpClientContext;

  private boolean loggedIn = false;

  private final String principal;

  /**
   * Constructor.
   */
  public SecureHttpClient(final String principal, final BundleContext bundleContext)
      throws Exception {
    this.principal = principal;

    httpClientContext = HttpClientContext.create();
    httpClientContext.setCookieStore(new BasicCookieStore());

    KeyStore trustStore = KeyStore.getInstance("jks");
    trustStore.load(
        bundleContext.getBundle().getResource("/jetty-keystore").openStream(),
        "changeit".toCharArray());

    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
        TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(trustStore);
    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, trustManagers, new SecureRandom());

    httpClient = HttpClientBuilder.create()
        .setSslcontext(sslContext)
        .setRedirectStrategy(new DefaultRedirectStrategy())
        .build();
  }

  public void close() throws IOException {
    httpClient.close();
  }

  public CloseableHttpClient getHttpClient() {
    return httpClient;
  }

  public HttpClientContext getHttpClientContext() {
    return httpClientContext;
  }

  public String getPrincipal() {
    return principal;
  }

  public boolean isLoggedIn() {
    return loggedIn;
  }

  public void setLoggedIn(final boolean loggedIn) {
    this.loggedIn = loggedIn;
  }

}
