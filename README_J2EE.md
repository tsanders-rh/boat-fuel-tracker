# Boat Fuel Tracker - Legacy J2EE Edition

## Overview

This is a **legacy J2EE application** intentionally designed with anti-patterns and deprecated APIs for testing the [Konveyor](https://www.konveyor.io/) application modernization platform.

⚠️ **WARNING**: This code contains intentional bad practices and should NOT be used as a reference for production applications!

## What is This?

This application is the Boat Fuel Tracker PWA converted into a legacy J2EE monolith with maximum anti-patterns to trigger Konveyor rule violations. It demonstrates common issues found in enterprise Java applications that need modernization.

## Technology Stack (Legacy)

- **Java EE 7** (old specification)
- **EJB 2.x** (deprecated)
- **Servlet 2.5** (old API)
- **Hibernate 4.3** (with proprietary annotations)
- **Log4j 1.2.17** (deprecated, security issues)
- **Maven** for build
- **MySQL 5** (old driver)

## Anti-Patterns Included

This application contains **12 major categories** of violations:

1. ✅ EJB 2.x Home/Remote interfaces
2. ✅ Hibernate proprietary annotations
3. ✅ Hardcoded JNDI lookups
4. ✅ WebSphere/JBoss vendor lock-in
5. ✅ File system dependencies
6. ✅ Old Servlet API (2.5) with XML config
7. ✅ Log4j 1.x usage
8. ✅ Mixed JPA and JDBC
9. ✅ Stateful session beans for HTTP session
10. ✅ Hibernate-specific persistence.xml
11. ✅ Hardcoded configuration in deployment descriptors
12. ✅ Old security patterns

See [KONVEYOR_ANALYSIS.md](./KONVEYOR_ANALYSIS.md) for complete details on all violations.

## Project Structure

```
boat-fuel-tracker/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/boatfuel/
│   │   │       ├── entity/          # JPA entities with Hibernate annotations
│   │   │       │   ├── User.java
│   │   │       │   └── FuelUp.java
│   │   │       ├── ejb/             # EJB 2.x beans
│   │   │       │   ├── FuelUpServiceHome.java
│   │   │       │   ├── FuelUpServiceRemote.java
│   │   │       │   ├── FuelUpServiceBean.java
│   │   │       │   └── UserSessionBean.java
│   │   │       ├── servlet/         # Old-style servlets
│   │   │       │   └── FuelUpServlet.java
│   │   │       └── util/            # Utilities with anti-patterns
│   │   │           ├── JNDILookupHelper.java
│   │   │           └── FileSystemHelper.java
│   │   ├── resources/
│   │   │   ├── META-INF/
│   │   │   │   └── persistence.xml  # Hibernate-specific config
│   │   │   └── log4j.properties     # Log4j 1.x config
│   │   └── webapp/
│   │       └── WEB-INF/
│   │           └── web.xml          # Servlet 2.5 descriptor
├── pom.xml                          # Maven with old dependencies
├── KONVEYOR_ANALYSIS.md             # Detailed violation analysis
└── README_J2EE.md                   # This file
```

## Building the Application

### Prerequisites
- JDK 8
- Maven 3.x

### Build
```bash
mvn clean package
```

This will create `target/boat-fuel-tracker.war`

## Deployment

### Supported Application Servers (Legacy)
- IBM WebSphere Application Server 8.5+
- JBoss EAP 6.x/7.x
- Oracle WebLogic 12c
- Apache TomEE 7.x

### Deployment Steps

1. **Configure Datasource**
   - Create JNDI datasource: `jdbc/BoatFuelTrackerDS`
   - Database: MySQL 5.x
   - Schema: `boatfuel`

2. **Deploy WAR**
   ```bash
   cp target/boat-fuel-tracker.war $SERVER_HOME/deployments/
   ```

3. **Create Directories**
   ```bash
   mkdir -p /opt/boatfuel/config
   mkdir -p /var/log/boatfuel
   mkdir -p /tmp/boatfuel
   ```

4. **Configure Security Realm**
   - Add users with roles: `user`, `admin`

## Running Konveyor Analysis

### Install Konveyor
```bash
# Install Konveyor Analyzer LSP
# See: https://github.com/konveyor/analyzer-lsp
```

### Analyze for Quarkus Migration
```bash
kantra analyze \
  --input . \
  --output ./konveyor-output \
  --target quarkus \
  --source java-ee \
  --rules https://github.com/konveyor/rulesets
```

### Analyze for Spring Boot Migration
```bash
kantra analyze \
  --input . \
  --output ./konveyor-output \
  --target spring-boot \
  --source java-ee
```

### Expected Results
- **100+ total violations**
- **Critical issues**: EJB 2.x, vendor lock-in, security vulnerabilities
- **High priority**: File system dependencies, hardcoded config
- **Medium priority**: Logging framework, old servlet API

## Violations by Category

| Category | Count | Severity |
|----------|-------|----------|
| EJB 2.x patterns | 70+ | Critical |
| Hibernate proprietary | 20+ | High |
| Hardcoded JNDI | 15+ | High |
| File system deps | 10+ | High |
| Vendor-specific APIs | 5+ | Critical |
| Log4j 1.x | 30+ | Critical |
| Servlet 2.5 | 10+ | Medium |
| Configuration issues | 15+ | Medium |

## Modernization Targets

### Option 1: Jakarta EE 10
- Modern Jakarta EE with CDI
- Standard JPA 3.0
- Servlet 6.0
- Deployable to WildFly, Payara, OpenLiberty

### Option 2: Quarkus
- Cloud-native, Kubernetes-ready
- Fast startup, low memory
- GraalVM native compilation
- Reactive capabilities

### Option 3: Spring Boot
- Popular enterprise framework
- Spring Data JPA
- Spring Security
- Wide ecosystem support

## Why This Code is Bad

### Not Cloud-Native
- ❌ Hardcoded file paths
- ❌ Stateful architecture
- ❌ Vendor lock-in
- ❌ Configuration in WAR file

### Not Portable
- ❌ WebSphere/JBoss specific code
- ❌ Hibernate proprietary features
- ❌ IIOP/RMI dependencies

### Security Issues
- ❌ Log4j 1.x vulnerabilities
- ❌ Hardcoded passwords
- ❌ Old security API

### Maintenance Nightmare
- ❌ EJB 2.x boilerplate
- ❌ Manual resource management
- ❌ No dependency injection
- ❌ XML-heavy configuration

## Original Application

The modern PWA version is on the `main` branch:
```bash
git checkout main
```

That version uses:
- Firebase Authentication
- Firestore Database
- Progressive Web App features
- Modern JavaScript
- GitHub Pages deployment

## Contributing

This is a demonstration project. Feel free to add MORE anti-patterns if you find patterns that Konveyor should detect!

## License

MIT License - Use for testing and demonstration only!

## Resources

- [Konveyor Project](https://www.konveyor.io/)
- [Konveyor Rulesets](https://github.com/konveyor/rulesets)
- [Jakarta EE](https://jakarta.ee/)
- [Quarkus](https://quarkus.io/)
- [Spring Boot](https://spring.io/projects/spring-boot)
