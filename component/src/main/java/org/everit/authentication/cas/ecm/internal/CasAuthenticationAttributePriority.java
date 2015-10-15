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

/**
 * Constants of CasAuthentication attribute priority.
 */
public final class CasAuthenticationAttributePriority {

  public static final int P1_SERVICE_DESCRIPTION = 1;

  public static final int P2_CAS_SERVICE_TICKET_VALIDATION_URL = 2;

  public static final int P3_FAILURE_URL = 3;

  public static final int P4_REQ_PARAM_NAME_SERVICE_TICKET = 4;

  public static final int P5_REQ_PARAM_NAME_LOGOUT_REQUEST = 5;

  public static final int P6_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES = 6;

  public static final int P7_RESOURCE_ID_RESOLVER = 7;

  public static final int P8_SAX_PARSER_FACTORY = 8;

  private CasAuthenticationAttributePriority() {
  }
}
