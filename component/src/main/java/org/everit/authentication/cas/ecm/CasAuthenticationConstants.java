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
package org.everit.authentication.cas.ecm;

/**
 * Constants of the Cas Authentication component.
 */
public final class CasAuthenticationConstants {

  public static final String ATTR_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES =
      "authenticationSessionAttributeNames.target";

  public static final String ATTR_CAS_SERVICE_TICKET_VALIDATION_URL =
      "cas.service.ticket.validation.url";

  public static final String ATTR_FAILURE_URL = "failure.url";

  public static final String ATTR_REQ_PARAM_NAME_LOGOUT_REQUEST = "requestParamNameLogoutRequest";

  public static final String ATTR_REQ_PARAM_NAME_SERVICE_TICKET = "requestParamNameServiceTicket";

  public static final String ATTR_RESOURCE_ID_RESOLVER = "resourceIdResolver.target";

  public static final String ATTR_SAX_PARSER_FACTORY = "saxParserFactory.target";

  public static final String DEFAULT_CAS_SERVICE_TICKET_VALIDATION_URL =
      "https://localhost:8443/cas/serviceValidate";

  public static final String DEFAULT_FAILURE_URL = "/failed.html";

  /**
   * The default value of the {@link #requestParamNameLogoutRequest}.
   */
  public static final String DEFAULT_REQ_PARAM_NAME_LOGOUT_REQUEST = "logoutRequest";

  /**
   * The default value of the {@link #requestParamNameServiceTicket}.
   */
  public static final String DEFAULT_REQ_PARAM_NAME_SERVICE_TICKET = "ticket";

  public static final String DEFAULT_SERVICE_DESCRIPTION_CAS_AUTHENTICATION =
      "Default CAS Authentication Component";

  public static final String SERVICE_FACTORYPID_CAS_AUTHENTICATION =
      "org.everit.authentication.cas.ecm.CasAuthentication";

  private CasAuthenticationConstants() {
  }

}
