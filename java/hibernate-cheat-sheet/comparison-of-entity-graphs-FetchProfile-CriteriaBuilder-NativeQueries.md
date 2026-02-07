| Feature           | Purpose / Use Case                                                                 | Fetch Strategy Control | Query Construction | Type Safety | Dynamic at Runtime | Static/Reusable | Supports Complex Joins | Notes                                                                                  |
|------------------------------------------------------------------------------------|-----------------------|--------------------|-------------|-------------------|--------------------|------------------|-----------------------|----------------------------------------------------------------------------------------|
| **Entity Graphs** | Override fetch plan for specific queries (eager/lazy loading of associations)      | Yes                   | No                 | Yes         | Yes (dynamic)      | Yes (named)      | No                    | Best for customizing fetch plans without changing queries                               |
| **FetchProfile**  | Override fetch plan for associations globally within a Hibernate session           | Yes                   | No                 | Yes         | Yes                | Yes              | No                    | Non-JPA standard; Hibernate-only; enabled/disabled per session                                            |
| **CriteriaBuilder** | Build type-safe, dynamic queries programmatically                                 | No (except via joins) | Yes                | Yes         | Yes                | No               | Yes                   | Ideal for dynamic, complex, or user-driven queries                                      |
| **Native Queries**| Execute raw SQL for maximum flexibility and DB-specific features                   | No (manual control)   | Yes                | No          | Yes                | No               | Yes                   | Use for vendor-specific SQL, complex joins, or performance tuning; loses type safety    |

---

### References with URLs

1. Hibernate ORM Documentation:  
   - Entity Graphs: [https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#fetching-strategies-entity-graph](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#fetching-strategies-entity-graph)  
   - FetchProfile: [https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#fetching-strategies-fetch-profiles](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#fetching-strategies-fetch-profiles)  
   - CriteriaBuilder: [https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#criteria](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#criteria)  
   - Native Queries: [https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#sql-executing](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#sql-executing)

2. Baeldung:  
   - Entity Graphs: [https://www.baeldung.com/jpa-entity-graph](https://www.baeldung.com/jpa-entity-graph)

3. Vlad Mihalcea:  
   - Entity Graphs: [https://vladmihalcea.com/jpa-entity-graph/](https://vladmihalcea.com/jpa-entity-graph/)
   - CriteriaBuilder: [https://vladmihalcea.com/jpa-criteria-api/](https://vladmihalcea.com/jpa-criteria-api/)
