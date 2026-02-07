## Hibernate Bootstrap Process Checklist

### ✅ Steps to Follow

1. **Add Hibernate and JDBC Dependencies**
   - Use Maven/Gradle to add dependencies.

2. **Create Hibernate Configuration**
   - Use `hibernate.cfg.xml` or programmatic configuration.

3. **Build ServiceRegistry**
   - Use `StandardServiceRegistryBuilder`.

4. **Create MetadataSources**
   - Register entity classes/mapping files.

5. **Build Metadata**
   - Use `MetadataSources` to build `Metadata`.

6. **Build SessionFactory**
   - Create a `SessionFactory` from `Metadata`.

7. **Obtain Session**
   - Open a `Session` for DB operations.

8. **Perform Database Operations**
   - Use session for CRUD.

9. **Close Session and SessionFactory**
   - Always close resources.

---

### ⚠️ Things to Avoid

- Hardcoding credentials
- Not closing sessions
- Ignoring exceptions
- Mixing config methods
- Using outdated Hibernate

---

### 👀 Watch Out For

- Configuration typos
- Entity mapping issues
- Thread safety (`SessionFactory` is thread-safe, `Session` is not)
- Transaction management
- Resource management

---

## Code Samples

### Good Example

```java
// Programmatic Hibernate bootstrap (good practice)
StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
    .configure() // loads hibernate.cfg.xml
    .build();

MetadataSources sources = new MetadataSources(registry);
Metadata metadata = sources.getMetadataBuilder().build();

SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

Session session = sessionFactory.openSession();
Transaction tx = session.beginTransaction();

try {
    // Perform DB operations
    // session.save(entity);
    tx.commit();
} catch (Exception e) {
    tx.rollback();
    e.printStackTrace();
} finally {
    session.close();
    sessionFactory.close();
}
```

### Bad Example

```java
// Bad practice: hardcoded credentials, no resource management, no transaction
Configuration cfg = new Configuration();
cfg.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/db");
cfg.setProperty("hibernate.connection.username", "root");
cfg.setProperty("hibernate.connection.password", "password");

SessionFactory sessionFactory = cfg.buildSessionFactory();
Session session = sessionFactory.openSession();

// No transaction, no exception handling, session not closed
session.save(entity);
```

---

## References

1. Hibernate ORM Documentation: [https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#bootstrap](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#bootstrap)
