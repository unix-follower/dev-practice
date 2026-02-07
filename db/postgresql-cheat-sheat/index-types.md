| Index Type   | Default? | Best For                                 | Not Suitable For                        | Supports Range Queries | Supports Equality | Supports Pattern Matching | Notes / Limitations                                                                                 |
|--------------|----------|------------------------------------------|-----------------------------------------|-----------------------|-------------------|--------------------------|-----------------------------------------------------------------------------------------------------|
| **B-Tree**   | Yes      | Most queries, equality, range, sorting   | Geometric, full-text, array, JSONB      | Yes                   | Yes               | Yes (left-anchored)      | Default index; efficient for most use cases                                                         |
| **Hash**     | No       | Simple equality comparisons              | Range, pattern, ordering                | No                    | Yes               | No                       | Faster for equality, but rarely used; not WAL-logged before PG 10                                   |
| **GIN**      | No       | Array, JSONB, full-text search           | Range, ordering                         | No                    | Yes               | Yes (for full-text)       | Good for multi-value fields; slower to update                                                       |
| **GiST**     | No       | Geometric, full-text, custom data types  | Simple equality                         | Yes                   | Yes               | Yes (with extensions)      | Flexible, supports many extensions (PostGIS, etc.)                                                  |
| **SP-GiST**  | No       | Non-balanced data, spatial, text search  | General-purpose queries                 | Yes                   | Yes               | Yes                      | Good for partitioned data, quadtrees, tries                                                         |
| **BRIN**     | No       | Very large tables, naturally ordered     | Small tables, random data               | Yes (approximate)     | Yes (approximate) | No                       | Low maintenance, small size, less precise; best for append-only, time-series                        |
| **Expression**| No      | Indexing computed values                 | Direct column queries                   | Depends on base type  | Depends           | Depends                  | Can be combined with other index types                                                              |
| **Partial**  | No       | Indexing subset of data                  | Full-table queries                      | Depends on base type  | Depends           | Depends                  | Reduces index size and maintenance; can be combined with other index types                          |

---

### References

1. PostgreSQL Documentation:  
   - Index Types: [https://www.postgresql.org/docs/current/indexes-types.html](https://www.postgresql.org/docs/current/indexes-types.html)  
   - Indexes Overview: [https://www.postgresql.org/docs/current/indexes.html](https://www.postgresql.org/docs/current/indexes.html)
