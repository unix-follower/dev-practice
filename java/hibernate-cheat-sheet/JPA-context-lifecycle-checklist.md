## Persistence Context Life Cycle with Hibernate: Checklist

### ✅ Steps to Follow

1. **Create EntityManager**
   - Use `EntityManagerFactory` to create an `EntityManager`.
   - Example: `EntityManager em = emf.createEntityManager();`

2. **Begin Transaction**
   - Start a transaction before performing any database operations.
   - Example: `em.getTransaction().begin();`

3. **Perform Entity Operations**
   - Use `em.persist()`, `em.merge()`, `em.remove()`, `em.find()` for CRUD operations.

4. **Flush Changes (if needed)**
   - Explicitly call `em.flush()` to synchronize the persistence context with the database.

5. **Commit or Rollback Transaction**
   - Commit with `em.getTransaction().commit();` or rollback with `em.getTransaction().rollback();` in case of errors.

6. **Clear Persistence Context (optional)**
   - Use `em.clear()` to detach all managed entities if needed.

7. **Close EntityManager**
   - Always close the `EntityManager` after use: `em.close();`

---

### ⚠️ Things to Avoid

- **Not Managing Transactions Properly**
  - Avoid performing operations outside of a transaction.

- **Forgetting to Close EntityManager**
  - Not closing the `EntityManager` can lead to resource leaks.

- **Ignoring Exceptions**
  - Always handle exceptions and rollback transactions when necessary.

- **Mixing Detached and Managed Entities**
  - Avoid operating on detached entities without merging them first.

- **Unnecessary Flushing**
  - Don’t call `em.flush()` too often; let Hibernate manage it unless you have a specific need.

---

### 👀 Watch Out For

- **Entity State Transitions**
  - Understand the difference between managed, detached, and removed states.

- **Thread Safety**
  - `EntityManager` is not thread-safe; never share it between threads.

- **Lazy Loading Pitfalls**
  - Accessing lazy-loaded properties outside the persistence context can cause `LazyInitializationException`.

- **Transaction Boundaries**
  - Ensure all operations that require a persistence context are within transaction boundaries.

---

## Code Samples

### Good Example

```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
EntityManager em = emf.createEntityManager();

try {
    em.getTransaction().begin();

    MyEntity entity = new MyEntity();
    em.persist(entity); // Managed

    // Other CRUD operations
    em.getTransaction().commit();
} catch (Exception e) {
    em.getTransaction().rollback();
    e.printStackTrace();
} finally {
    em.close();
    emf.close();
}
```

### Bad Example

```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
EntityManager em = emf.createEntityManager();

// No transaction started
MyEntity entity = new MyEntity();
em.persist(entity); // Will fail or behave unexpectedly

// No exception handling
// EntityManager not closed
```

---

## References

1. Hibernate ORM Documentation: [https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#persistence-context](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#persistence-context)
