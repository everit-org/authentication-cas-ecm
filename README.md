authentication-cas-ecm
======================

ECM based component for [authentication-cas][6].

#Component
The module contains one ECM component. The component can be 
instantiated multiple times via Configuration Admin. The component registers 
two OSGi services:
 - **javax.servlet.Filter**: Handles the CAS service ticket validation and CAS 
 logout request processing.
 - **java.util.EventListener**: As a **ServletContextListener**, a 
 **HttpSessionListener** and a **HttpSessionAttributeListener** to handle 
 ServletContext, HttpSession and HttpSession attribute related events that 
 ensures the operation of the component. For more information check the 
 javadoc of the *org.everit.authentication.cas.ecm.internal* package.

#Configuration
 - **Service Description**: The description of this component configuration. 
 It is used to easily identify the services registered by this component. 
 (service.description)
 - **CAS service ticket validation URL**: The URL provided by the CAS server 
 for service ticket validation. HTTPS protocol (and java keystore 
 configuration) is recommended for security reasons. For e.g. 
 "https://cas.example.com/cas/serviceValidate".
 (cas.service.ticket.validation.url)
 - **Failure URL**: The URL where the user will be redirected in case of 
 failed request processing. For e.g. "/failed.html", the user will be 
 redirected to "http://app.example.com/failed.html" (failure.url)
 - **AuthenticationSessionAttributeNames OSGi filter**: OSGi Service filter 
 expression for AuthenticationSessionAttributeNames instance. 
 (authenticationSessionAttributeNames.target)
 - **ResourceIdResolver OSGi filter**: OSGi Service filter expression for 
 ResourceIdResolver instance. (resourceIdResolver.target)
 - **SAXParserFactory OSGi filter**: OSGi Service filter expression for 
 SAXParserFactory instance. (saxParserFactory.target)

#Usage
A complex example can be found under the integration tests project in the 
*org.everit.authentication.cas.ecm.tests.CasAuthenticationTestComponent* 
class.

#Useful CAS links
 - [CAS 4.0.0 home page][3]
 - [CAS flow diagram][4]
 - [Logout and Single Logout (SLO)][5]

#Concept
Full authentication concept is available on blog post 
[Everit Authentication][1].

[1]: http://everitorg.wordpress.com/2014/07/31/everit-authentication/
[3]: http://jasig.github.io/cas/4.0.0/index.html
[4]: http://jasig.github.io/cas/4.0.0/images/cas_flow_diagram.png
[5]: http://jasig.github.io/cas/4.0.0/installation/Logout-Single-Signout.html
[6]: https://github.com/everit-org/authentication-cas
