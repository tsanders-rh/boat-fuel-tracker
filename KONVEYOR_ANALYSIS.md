# Boat Fuel Tracker - Legacy J2EE Application

## Purpose

This is an **intentionally legacy** J2EE application designed for testing with [Konveyor](https://www.konveyor.io/) application modernization tools. It contains numerous anti-patterns, deprecated APIs, and vendor-specific code that Konveyor rules should detect and flag for modernization.

## Anti-Patterns and Violations Included

This application intentionally includes the following violations that Konveyor should detect:

### 1. EJB 2.x Usage (Deprecated)
**Files:**
- `src/main/java/com/boatfuel/ejb/FuelUpServiceHome.java` - EJB 2.x Home interface
- `src/main/java/com/boatfuel/ejb/FuelUpServiceRemote.java` - EJB 2.x Remote interface
- `src/main/java/com/boatfuel/ejb/FuelUpServiceBean.java` - SessionBean implementation
- `src/main/java/com/boatfuel/ejb/UserSessionBean.java` - Stateful session bean

**Violations:**
- EJB 2.x Home/Remote interfaces with RMI
- `javax.ejb.SessionBean` interface usage
- `javax.ejb.SessionSynchronization` usage
- Manual `ejbCreate()`, `ejbActivate()`, `ejbPassivate()` lifecycle methods
- `PortableRemoteObject.narrow()` for EJB lookups

**Modernization:**
- Migrate to EJB 3.x with `@Stateless`/`@Stateful` annotations
- Use `@EJB` dependency injection instead of JNDI lookups
- Replace with CDI beans where appropriate
- Remove Home/Remote interfaces

### 2. Hibernate Proprietary Annotations
**Files:**
- `src/main/java/com/boatfuel/entity/User.java`
- `src/main/java/com/boatfuel/entity/FuelUp.java`

**Violations:**
- `@org.hibernate.annotations.Cache` - Hibernate-specific caching
- `@org.hibernate.annotations.GenericGenerator` - Hibernate ID generation
- `@org.hibernate.annotations.Type` - Hibernate-specific types
- `@org.hibernate.annotations.CreationTimestamp` - Hibernate timestamps
- `@org.hibernate.annotations.Index` - Old Hibernate index annotation

**Modernization:**
- Use standard JPA 2.2/3.0 annotations
- Replace `@GenericGenerator` with `@GeneratedValue(strategy = GenerationType.UUID)`
- Use JPA 2.2 `@Index` in `@Table` annotation
- Use JPA lifecycle callbacks instead of Hibernate timestamps

### 3. Hardcoded JNDI Lookups
**Files:**
- `src/main/java/com/boatfuel/util/JNDILookupHelper.java`
- `src/main/java/com/boatfuel/servlet/FuelUpServlet.java`
- `src/main/java/com/boatfuel/ejb/FuelUpServiceBean.java`

**Violations:**
- Manual `InitialContext` creation
- Hardcoded JNDI names: `"jdbc/BoatFuelTrackerDS"`, `"ejb/com/boatfuel/*"`
- No dependency injection
- Manual resource lookups instead of `@Resource` or `@Inject`

**Modernization:**
- Use `@Resource` for datasource injection
- Use `@EJB` or `@Inject` for bean injection
- Remove manual JNDI lookups
- Use CDI for dependency management

### 4. Vendor-Specific APIs
**Files:**
- `src/main/java/com/boatfuel/util/JNDILookupHelper.java`
- `src/main/java/com/boatfuel/util/FileSystemHelper.java`
- `pom.xml`

**WebSphere-specific:**
- `com.ibm.websphere.naming.WsnInitialContextFactory`
- `corbaloc:iiop:` IIOP protocol references
- WebSphere JTA platform in persistence.xml

**JBoss-specific:**
- `org.jboss.vfs.VFS` API usage
- JBoss-specific JNDI paths: `"java:jboss/*"`

**Modernization:**
- Remove vendor-specific API dependencies
- Use standard Java EE/Jakarta EE APIs
- Externalize server-specific configuration
- Use portable patterns compatible with any application server

### 5. File System Dependencies
**Files:**
- `src/main/java/com/boatfuel/util/FileSystemHelper.java`
- `src/main/resources/log4j.properties`

**Violations:**
- Hardcoded absolute paths:
  - `/opt/boatfuel/config`
  - `/var/log/boatfuel`
  - `C:\BoatFuel\exports` (Windows path)
- Direct file system I/O operations
- Configuration files on file system
- File-based audit logging

**Modernization:**
- Use ConfigMaps or environment variables for configuration
- Use volume mounts for file storage
- Use centralized logging (stdout/stderr)
- Use object storage (S3, etc.) instead of local file system
- Remove hardcoded paths

### 6. Old Servlet API (2.5)
**Files:**
- `src/main/webapp/WEB-INF/web.xml` - Servlet 2.5 descriptor
- `src/main/java/com/boatfuel/servlet/FuelUpServlet.java`

**Violations:**
- XML-based servlet configuration instead of annotations
- `<servlet>` and `<servlet-mapping>` in web.xml
- `extends HttpServlet` without `@WebServlet`
- `<ejb-ref>` references in web.xml
- HTML generation in servlet (no templates)

**Modernization:**
- Use `@WebServlet` annotations
- Remove web.xml servlet mappings
- Use modern MVC framework (JAX-RS, Spring MVC)
- Use templating (JSP, Thymeleaf, etc.) instead of PrintWriter

### 7. Deprecated Logging Framework
**Files:**
- `src/main/resources/log4j.properties`
- All Java files using `org.apache.log4j.Logger`

**Violations:**
- Log4j 1.x usage (has known security vulnerabilities)
- Hardcoded log file paths in configuration
- Direct Log4j API usage throughout code

**Modernization:**
- Migrate to Log4j 2.x or SLF4J + Logback
- Use container logging (stdout/stderr)
- Remove file-based logging
- Use structured logging (JSON format)

### 8. Mixed JPA and JDBC
**Files:**
- `src/main/java/com/boatfuel/ejb/FuelUpServiceBean.java`

**Violations:**
- Direct JDBC `Connection`/`PreparedStatement` usage
- Manual SQL queries alongside JPA
- Manual resource management (no try-with-resources)
- Mixing persistence paradigms in same class

**Modernization:**
- Use JPA for all database operations
- Use JPQL or Criteria API instead of SQL
- Remove direct JDBC code
- Use JPA repositories pattern

### 9. Stateful Session Bean for HTTP Session
**Files:**
- `src/main/java/com/boatfuel/ejb/UserSessionBean.java`

**Violations:**
- Using stateful EJB to store HTTP session data
- EJB passivation/activation for web state
- Not cloud-native (requires sticky sessions)

**Modernization:**
- Use HTTP session directly
- Use stateless authentication (JWT tokens)
- Use external session store (Redis) if needed
- Make application stateless for Kubernetes

### 10. Hibernate-Specific Configuration
**Files:**
- `src/main/resources/META-INF/persistence.xml`

**Violations:**
- `org.hibernate.ejb.HibernatePersistence` provider
- Hibernate-specific properties: `hibernate.dialect`, `hibernate.hbm2ddl.auto`
- Hibernate cache configuration
- WebSphere-specific JTA platform

**Modernization:**
- Use standard JPA provider configuration
- Externalize database-specific settings
- Use standard JPA properties
- Remove vendor-specific transaction platform

### 11. Environment-Specific Configuration in Code
**Files:**
- `src/main/webapp/WEB-INF/web.xml`

**Violations:**
- `<env-entry>` with hardcoded paths
- Configuration in deployment descriptor
- No externalized configuration

**Modernization:**
- Use external configuration (ConfigMaps, environment variables)
- Use Spring Boot application.properties/yaml
- Use MicroProfile Config
- 12-factor app compliance

### 12. Security Anti-Patterns
**Files:**
- `src/main/webapp/WEB-INF/web.xml`
- `src/main/java/com/boatfuel/util/FileSystemHelper.java`

**Violations:**
- Old form-based authentication in web.xml
- `<security-constraint>` instead of annotations
- Hardcoded password in default config file

**Modernization:**
- Use Java EE Security API or Spring Security
- Use OAuth2/OpenID Connect
- Externalize credentials
- Use secret management (Vault, Kubernetes secrets)

## Running Konveyor Analysis

### Prerequisites
```bash
# Install Konveyor CLI
# Follow instructions at https://github.com/konveyor/analyzer-lsp
```

### Build the Application
```bash
mvn clean package
```

### Run Konveyor Analysis
```bash
# Analyze the source code
kantra analyze \
  --input /path/to/boat-fuel-tracker \
  --output ./konveyor-report \
  --rules https://github.com/konveyor/rulesets \
  --target quarkus \
  --source java-ee

# Or analyze for Spring Boot migration
kantra analyze \
  --input /path/to/boat-fuel-tracker \
  --output ./konveyor-report \
  --rules https://github.com/konveyor/rulesets \
  --target spring-boot \
  --source java-ee
```

### Expected Violations

Konveyor should detect and report:

1. **70+ EJB 2.x migration issues**
2. **20+ Hibernate proprietary API usages**
3. **15+ hardcoded JNDI lookup violations**
4. **10+ file system dependency issues**
5. **5+ vendor-specific API usages**
6. **Log4j 1.x security vulnerabilities**
7. **Servlet 2.5 migration needs**
8. **Configuration management anti-patterns**

## Modernization Path

### Target Architecture Options

#### Option 1: Jakarta EE 10
- Migrate to EJB 3.x or CDI
- Use standard JPA 3.0
- Update to Servlet 5.0+
- Use Jakarta EE Security API

#### Option 2: Quarkus
- Replace EJBs with CDI beans
- Use Quarkus extensions
- Use RESTEasy Reactive for REST
- Hibernate ORM with Panache

#### Option 3: Spring Boot
- Replace EJBs with Spring beans
- Use Spring Data JPA
- Use Spring MVC or WebFlux
- Spring Security

### Cloud-Native Targets
- Containerize with Docker
- Deploy to Kubernetes/OpenShift
- Use external configuration (ConfigMaps)
- Use external storage (PVC, S3)
- Implement health checks and metrics
- Use distributed tracing

## Original Application

This is a modernized version of the Boat Fuel Tracker PWA that was converted back to legacy J2EE to demonstrate Konveyor's capabilities.

## License

MIT License - This code is intentionally bad for demonstration purposes!
