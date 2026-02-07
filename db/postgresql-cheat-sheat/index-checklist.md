## Checklist: PostgreSQL Indexes (Including Multicolumn and Skip Scan)

### ✅ Steps to Follow

1. **Analyze Query Patterns**
   - Identify columns frequently used in WHERE, JOIN, ORDER BY, and GROUP BY clauses.

2. **Choose the Right Index Type**
   - Use B-Tree for most queries (equality, range).
   - Consider GIN, GiST, BRIN, etc., for special data types (arrays, full-text, spatial).

3. **Create Indexes**
   - For single-column queries:
     ```sql
     CREATE INDEX idx_table_col1 ON table(col1);
     ```
   - For queries filtering on multiple columns:
     ```sql
     CREATE INDEX idx_table_col1_col2 ON table(col1, col2);
     ```

4. **Leverage Multicolumn Indexes**
   - Place the most selective (most commonly filtered) column first.
   - Multicolumn indexes are most effective when queries filter on the leftmost columns.

5. **Understand Skip Scan Optimization (PostgreSQL 11+)**
   - Skip scan allows using a multicolumn index even if the query does not filter on the leading column, especially with IS NULL, IN, or when skipping values.
   - Example:
     ```sql
     -- Index on (col1, col2)
     CREATE INDEX idx_table_col1_col2 ON table(col1, col2);

     -- Query filtering only on col2
     SELECT * FROM table WHERE col2 = 'value';
     -- PostgreSQL 11+ may use skip scan optimization here
     ```

6. **Monitor and Analyze Index Usage**
   - Use `EXPLAIN` to check if indexes are being used as expected.
   - Regularly run `ANALYZE` to keep statistics up to date.

---

### ⚠️ Things to Avoid

- **Creating Unnecessary Indexes**
  - Too many indexes slow down writes and increase storage usage.

- **Ignoring Column Order in Multicolumn Indexes**
  - Indexes on (col1, col2) are not the same as (col2, col1).

- **Assuming Skip Scan Always Applies**
  - Skip scan is only available in PostgreSQL 11+ and for B-Tree indexes.

- **Neglecting Index Maintenance**
  - Periodically REINDEX or drop unused indexes.

---

### 👀 Watch Out For

- **Query Structure**
  - Indexes are most effective when queries match the index column order.
  - Functions or expressions on columns may prevent index usage unless you use expression indexes.

- **Index Bloat**
  - Monitor for index bloat, especially on frequently updated tables.

- **Partial and Expression Indexes**
  - Use partial indexes for queries on a subset of data.
  - Use expression indexes for queries involving computed columns.

---

## Code Samples

### Good Example

```sql
-- Multicolumn index for queries filtering on both columns
CREATE INDEX idx_orders_customer_date ON orders(customer_id, order_date);

-- Query using both columns (efficient)
SELECT * FROM orders WHERE customer_id = 42 AND order_date >= '2024-01-01';

-- Query using only order_date (PostgreSQL 11+ may use skip scan)
SELECT * FROM orders WHERE order_date >= '2024-01-01';

-- Check index usage
EXPLAIN SELECT * FROM orders WHERE order_date >= '2024-01-01';
```

### Bad Example

```sql
-- Index columns in wrong order for query patterns
CREATE INDEX idx_orders_date_customer ON orders(order_date, customer_id);

-- Query filtering only on customer_id (index not used efficiently)
SELECT * FROM orders WHERE customer_id = 42;
```

---

## References

1. PostgreSQL Documentation:  
   - Indexes Overview: [https://www.postgresql.org/docs/current/indexes.html](https://www.postgresql.org/docs/current/indexes.html)  
   - Multicolumn Indexes: [https://www.postgresql.org/docs/current/indexes-multicolumn.html](https://www.postgresql.org/docs/current/indexes-multicolumn.html)  
   - Index Skipping Optimization: [https://www.postgresql.org/docs/current/indexes-index-only-scans.html#INDEXES-SKIP-SCANS](https://www.postgresql.org/docs/current/indexes-index-only-scans.html#INDEXES-SKIP-SCANS)
