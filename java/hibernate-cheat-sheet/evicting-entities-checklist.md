## Checklist: Evicting (Detaching) Entities in JPA/Hibernate

### ✅ Steps to Follow

1. **Obtain a Managed Entity**
   - Load or persist an entity so it is managed by the current persistence context.
   - Example: `MyEntity entity = em.find(MyEntity.class, id);`

2. **Evict/Detach the Entity**
   - Use `em.detach(entity)` to remove the entity from the persistence context.

3. **Verify Detachment**
   - Optionally, check if the entity is detached: `!em.contains(entity)`

4. **(Optional) Reattach the Entity**
   - If you need to persist further changes, use `em.merge(entity)` to reattach.

---

### ⚠️ Things to Avoid

- **Modifying Detached Entities Without Merging**
  - Changes to detached entities are not tracked or persisted unless reattached.

- **Assuming Detach is a Substitute for Remove**
  - `detach()` only removes the entity from the context; it does not delete it from the database.

- **Detaching Entities Unintentionally**
  - Be careful with `em.clear()` or `em.close()`, which detach all managed entities.

- **Ignoring Transaction Boundaries**
  - Detach operations should be performed within an active transaction for consistency.

---

### 👀 Watch Out For

- **Entity State Awareness**
  - Know whether your entity is managed or detached before performing operations.

- **Lazy Loading Issues**
  - Accessing lazy-loaded properties on a detached entity will cause `LazyInitializationException`.

- **Performance Considerations**
  - Detaching large numbers of entities can help manage memory in long-running sessions.

---

## Code Samples

### Good Example

```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
EntityManager em = emf.createEntityManager();

try {
    em.getTransaction().begin();

    MyEntity entity = em.find(MyEntity.class, 1L); // Managed

    em.detach(entity); // Entity is now detached

    boolean isManaged = em.contains(entity); // false

    // To persist further changes:
    entity.setName("Updated Name");
    em.merge(entity); // Reattaches and persists changes

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

MyEntity entity = em.find(MyEntity.class, 1L); // Managed

em.detach(entity); // Detached

entity.setName("Won't be persisted"); // Changes are NOT tracked

// No merge, so changes are lost
em.getTransaction().commit(); // No effect on DB

em.close();
emf.close();
```

---

## References

1. Hibernate ORM Documentation: [https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#pc-detach](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#pc-detach)
