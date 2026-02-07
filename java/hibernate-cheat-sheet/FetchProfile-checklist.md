## Checklist: Using FetchProfile in JPA/Hibernate

### ✅ Steps to Follow

1. **Define FetchProfile in Entity**
   - Use the `@FetchProfile` annotation on your entity class to declare fetch profiles and specify associations to be fetched.
   - Example:
     ```java
     @FetchProfile(
         name = "user-with-roles",
         fetchOverrides = {
             @FetchProfile.FetchOverride(
                 entity = User.class,
                 association = "roles",
                 mode = FetchMode.JOIN
             )
         }
     )
     @Entity
     public class User { ... }
     ```

2. **Enable FetchProfile in Session**
   - Before executing a query, enable the fetch profile using the Hibernate `Session` API:
     ```java
     session.enableFetchProfile("user-with-roles");
     ```

3. **Execute Query**
   - Run your query (e.g., `session.get(User.class, id)`). The enabled fetch profile will override the default fetch strategy for specified associations.

4. **Disable FetchProfile (if needed)**
   - After the query, disable the fetch profile to avoid affecting subsequent queries:
     ```java
     session.disableFetchProfile("user-with-roles");
     ```

---

### ⚠️ Things to Avoid

- **Using FetchProfile with JPA EntityManager**
  - FetchProfile is a Hibernate-specific feature and is not available via standard JPA `EntityManager`.

- **Forgetting to Disable FetchProfile**
  - Not disabling a fetch profile may unintentionally affect other queries in the same session.

- **Overfetching Data**
  - Only include associations in fetch profiles that are truly needed for the use case to avoid performance issues.

- **Ignoring Default Fetch Strategies**
  - Be aware that fetch profiles override default fetch strategies only when enabled.

---

### 👀 Watch Out For

- **Session Scope**
  - Fetch profiles are enabled per Hibernate `Session` and do not affect other sessions.

- **Association Names**
  - Ensure the association names in `@FetchProfile` match the field names in your entity.

- **Compatibility**
  - FetchProfile is not supported in all JPA providers; it is a Hibernate-specific feature.

- **Performance Impact**
  - Using `JOIN` fetch mode can result in large SQL joins; monitor query performance.

---

## Code Samples

### Good Example

```java
// Entity declaration
@FetchProfile(
    name = "user-with-roles",
    fetchOverrides = {
        @FetchProfile.FetchOverride(
            entity = User.class,
            association = "roles",
            mode = FetchMode.JOIN
        )
    }
)
@Entity
public class User {
    @OneToMany
    private List<Role> roles;
}

// Usage
Session session = sessionFactory.openSession();
session.enableFetchProfile("user-with-roles");
User user = session.get(User.class, userId); // roles fetched via JOIN
session.disableFetchProfile("user-with-roles");
session.close();
```

### Bad Example

```java
// Entity declaration without FetchProfile
@Entity
public class User {
    @OneToMany
    private List<Role> roles;
}

// Usage
Session session = sessionFactory.openSession();
// No fetch profile enabled; roles may be lazily loaded, causing N+1 problem
User user = session.get(User.class, userId);
session.close();
```

---

## References

1. Hibernate ORM Documentation: [https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#fetching-strategies-fetch-profiles](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#fetching-strategies-fetch-profiles)
