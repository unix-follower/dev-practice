## Checklist: Query Declaration with CriteriaBuilder in JPA/Hibernate

### ✅ Steps to Follow

1. **Obtain EntityManager**
   - Get an instance of `EntityManager` from your persistence context.

2. **Get CriteriaBuilder**
   - Use `em.getCriteriaBuilder()` to obtain a `CriteriaBuilder` instance.

3. **Create CriteriaQuery**
   - Use `cb.createQuery(EntityClass.class)` to create a `CriteriaQuery` for your entity.

4. **Define Query Root**
   - Set the root of the query with `query.from(EntityClass.class)`.

5. **Build Predicates and Conditions**
   - Use `CriteriaBuilder` methods (e.g., `equal`, `like`, `greaterThan`) to build query conditions.

6. **Set Selection and Where Clause**
   - Use `query.select(root)` and `query.where(predicate)` to define what to select and filter.

7. **Create TypedQuery and Execute**
   - Use `em.createQuery(query)` to create a `TypedQuery`, then execute with `getResultList()` or `getSingleResult()`.

---

### ⚠️ Things to Avoid

- **Hardcoding Query Strings**
  - Avoid using string-based queries when CriteriaBuilder can provide type safety.

- **Ignoring Null Checks**
  - Always check for null values in predicates to avoid runtime errors.

- **Overcomplicating Simple Queries**
  - Use CriteriaBuilder for dynamic or complex queries; for simple queries, JPQL may be more readable.

- **Forgetting to Close EntityManager**
  - Always close the `EntityManager` after use to prevent resource leaks.

---

### 👀 Watch Out For

- **Type Safety**
  - CriteriaBuilder provides compile-time type safety; leverage it to avoid runtime errors.

- **Dynamic Query Construction**
  - CriteriaBuilder is ideal for building queries dynamically based on user input or business logic.

- **Performance**
  - Complex criteria queries can impact performance; optimize predicates and joins.

- **Pagination**
  - Use `setFirstResult()` and `setMaxResults()` on `TypedQuery` for efficient pagination.

---

## Code Samples

### Good Example

```java
EntityManager em = emf.createEntityManager();
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<User> query = cb.createQuery(User.class);
Root<User> root = query.from(User.class);

// Build predicates
Predicate predicate = cb.equal(root.get("status"), "ACTIVE");
query.select(root).where(predicate);

// Execute query
List<User> users = em.createQuery(query).getResultList();

em.close();
```

### Bad Example

```java
EntityManager em = emf.createEntityManager();
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<User> query = cb.createQuery(User.class);
Root<User> root = query.from(User.class);

// No predicates, selects all users
query.select(root);

// No filtering, may return too many results
List<User> users = em.createQuery(query).getResultList();

// EntityManager not closed
```

---

## References

1. Hibernate ORM Documentation: [https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#criteria](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#criteria)
2. Vlad Mihalcea: "How to use the JPA Criteria API" [https://vladmihalcea.com/jpa-criteria-api/](https://vladmihalcea.com/jpa-criteria-api/)
