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
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.everit.authentication.context.AuthenticationContext;
import org.everit.web.servlet.HttpServlet;

/**
 * Simple Hello World Servlet.
 */
public class HelloWorldServlet extends HttpServlet {

  public static final String GUEST = "guest";

  public static final String UNKNOWN = "unknown";

  private final AuthenticationContext authenticationContext;

  public HelloWorldServlet(final AuthenticationContext authenticationContext) {
    super();
    this.authenticationContext = authenticationContext;
  }

  private String getUserName(final long currentResourceId) {
    if (currentResourceId == authenticationContext.getDefaultResourceId()) {
      return GUEST;
    } else if (currentResourceId == CasResourceIdResolver.JOHNDOE_RESOURCE_ID.get().longValue()) {
      return CasResourceIdResolver.JOHNDOE;
    } else if (currentResourceId == CasResourceIdResolver.JANEDOE_RESOURCE_ID.get().longValue()) {
      return CasResourceIdResolver.JANEDOE;
    } else {
      return UNKNOWN;
    }
  }

  @Override
  protected void service(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    long currentResourceId = authenticationContext.getCurrentResourceId();
    String userName = getUserName(currentResourceId);

    resp.setContentType("text/plain");
    PrintWriter out = resp.getWriter();
    out.print(userName);
    out.print("@");
    out.print(req.getServerName());
  }

}
