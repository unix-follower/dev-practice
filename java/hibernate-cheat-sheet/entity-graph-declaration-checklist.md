## Checklist: Entity Graph Declaration in JPA/Hibernate

### ✅ Steps to Follow

1. **Decide Fetch Requirements**
   - Identify which attributes and associations you want to fetch eagerly for a specific use case.

2. **Declare the Entity Graph**
   - Use `@NamedEntityGraph` annotation on your entity class for static graphs.
   - Or, create a dynamic entity graph at runtime using `em.createEntityGraph()`.

3. **Add Attribute Nodes**
   - Specify which fields and associations should be included in the graph using `attributeNodes` or `addAttributeNodes()`.

4. **Apply Entity Graph to Query**
   - Set the entity graph as a query hint (e.g., `javax.persistence.fetchgraph` or `javax.persistence.loadgraph`) when executing a query.

5. **Execute the Query**
   - Run the query; the fetch plan will follow the entity graph definition.

---

### ⚠️ Things to Avoid

- **Overfetching**
  - Don’t include unnecessary attributes or associations in the entity graph, as this can lead to performance issues.

- **Ignoring Default Fetch Types**
  - Remember that entity graphs override default fetch strategies only for the specified query.

- **Forgetting to Apply the Graph**
  - Declaring an entity graph has no effect unless you apply it to a query.

- **Mixing FetchGraph and LoadGraph Unintentionally**
  - Understand the difference: `fetchgraph` fetches only specified attributes eagerly, while `loadgraph` fetches specified attributes eagerly and others as per their default fetch type.

---

### 👀 Watch Out For

- **Nested Associations**
  - Use `@NamedSubgraph` or `addSubgraph()` for nested associations.

- **Query Compatibility**
  - Not all query types support entity graphs (e.g., native queries may not).

- **EntityManager Scope**
  - Dynamic entity graphs are valid only for the current `EntityManager` instance.

---

## Code Samples

### Good Example

```java
// Entity declaration
@NamedEntityGraph(
    name = "User.detail",
    attributeNodes = {
        @NamedAttributeNode("profile"),
        @NamedAttributeNode("roles")
    }
)
@Entity
public class User { ... }

// Applying the entity graph
EntityGraph<?> graph = em.getEntityGraph("User.detail");
Map<String, Object> hints = new HashMap<>();
hints.put("javax.persistence.fetchgraph", graph);

User user = em.find(User.class, userId, hints);
```

### Bad Example

```java
// Entity graph declared but not applied
@NamedEntityGraph(
    name = "User.detail",
    attributeNodes = {
        @NamedAttributeNode("profile"),
        @NamedAttributeNode("roles")
    }
)
@Entity
public class User { ... }

// Query without applying the entity graph
User user = em.find(User.class, userId); // Will use default fetch strategy, not the entity graph
```

---

## References

1. Hibernate ORM Documentation: [https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#fetching-strategies-entity-graph](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#fetching-strategies-entity-graph)
2. Baeldung: "A Guide to JPA Entity Graphs" [https://www.baeldung.com/jpa-entity-graph](https://www.baeldung.com/jpa-entity-graph)
3. Vlad Mihalcea: "How to use JPA Entity Graphs to customize fetch plans" [https://vladmihalcea.com/jpa-entity-graph/](https://vladmihalcea.com/jpa-entity-graph/)
