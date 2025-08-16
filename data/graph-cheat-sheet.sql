SELECT * FROM pg_available_extensions WHERE name = 'age';
SELECT * FROM pg_extension WHERE extname = 'age';

CREATE EXTENSION age;
LOAD 'age';

SET search_path = ag_catalog, "$user", public;
SHOW search_path;
SHOW ALL;

ALTER SCHEMA ag_catalog OWNER TO assistant_graph_svc;
GRANT USAGE ON SCHEMA ag_catalog TO pubchem_graph;
GRANT CREATE ON SCHEMA ag_catalog TO pubchem_graph;
ALTER DEFAULT PRIVILEGES IN SCHEMA ag_catalog GRANT ALL PRIVILEGES ON TABLES TO pubchem_graph;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA ag_catalog TO assistant_graph_svc;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA ag_catalog TO pubchem_graph;

SELECT p.proname AS function_name,
       r.rolname AS grantee,
       p.proacl AS privileges
FROM pg_proc p
JOIN pg_roles r ON p.proowner = r.oid
WHERE p.proname = 'create_graph';

SELECT grantee, privilege_type FROM information_schema.schema_privileges WHERE schema_name = 'ag_catalog';
SELECT grantee, table_name, privilege_type FROM information_schema.role_table_grants WHERE table_schema = 'ag_catalog';

SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'chemical_compound_graph';

--truncate table ag_graph cascade;
SELECT * FROM ag_catalog.ag_graph;
SELECT * FROM ag_catalog.ag_label;

SELECT ag_catalog.drop_graph('chemical_compound_graph', true);
SELECT ag_catalog.drop_graph('molecule_graph', true);
SELECT create_graph('chemical_compound_graph');
SELECT create_graph('molecule_graph');
SELECT create_graph('bioinformatics_graph');

SELECT * FROM chemical_compound_graph;

-- bioinformatics_graph
SELECT * FROM cypher('bioinformatics_graph', $$
    CREATE (s:Sequence {id: 'seq1', description: 'Example Sequence'})
           -[:NEXT]->(a:Nucleotide {id: 1, base: 'A'})
           -[:NEXT]->(b:Nucleotide {id: 2, base: 'T'})
           -[:NEXT]->(c:Nucleotide {id: 3, base: 'C'})
           -[:NEXT]->(d:Nucleotide {id: 4, base: 'G'})
$$) AS (s agtype);

SELECT * FROM cypher('bioinformatics_graph', $$
    MATCH (s:Sequence {id: 'seq1'})-[:NEXT*]->(n:Nucleotide)
    RETURN n
$$) AS (n agtype);

-- =====================================

SELECT * FROM cypher('chemical_compound_graph', $$
    CREATE (c:Compound {
        name: '1-Amino-2-propanol',
        formula: 'C3H9NO'
    })
$$) AS (result agtype);

-- Create element nodes and relationships
SELECT * FROM cypher('chemical_compound_graph', $$
    MATCH (c:Compound {formula: 'C3H9NO'})
    CREATE (c1:Element {symbol: 'C'})<-[:HAS_ELEMENT {count: 3}]-(c),
           (h:Element {symbol: 'H'})<-[:HAS_ELEMENT {count: 9}]-(c),
           (n:Element {symbol: 'N'})<-[:HAS_ELEMENT {count: 1}]-(c),
           (o:Element {symbol: 'O'})<-[:HAS_ELEMENT {count: 1}]-(c)
$$) AS (result agtype);

SELECT * FROM cypher('chemical_compound_graph', $$
    MATCH (c:Compound {name: '1-Amino-2-propanol'})-[:HAS_ELEMENT]->(e:Element)
    RETURN e.symbol, e.count
$$) AS (symbol text, count int);

-- \df+ get_compound_elements
DROP FUNCTION ag_catalog.get_compound_data_by_name;
CREATE OR REPLACE FUNCTION get_compound_data_by_name(compound_name VARCHAR(1024), "offset" INT, "limit" INT)
RETURNS TABLE(total_compounds BIGINT, total_elements BIGINT, total_edges BIGINT, compound agtype, element agtype, relationship agtype) AS $function$
BEGIN
    RETURN QUERY EXECUTE FORMAT(
        $query$
        WITH total_compounds AS (
                SELECT * FROM cypher('chemical_compound_graph', $cypher$
                    MATCH (c:Compound)
	                WHERE c.name = %L
                    RETURN COUNT(c)
                $cypher$) AS (total BIGINT)
            ),
        total_elements AS (
                SELECT * FROM cypher('chemical_compound_graph', $cypher$
                    MATCH (c:Compound)-[r:HAS_ELEMENT]->(e:Element)
	                WHERE c.name = %L
                    RETURN COUNT(e)
                $cypher$) AS (total BIGINT)
            ),
        total_edges AS (
                SELECT * FROM cypher('chemical_compound_graph', $cypher$
                    MATCH (c:Compound)-[r:HAS_ELEMENT]->()
	                WHERE c.name = %L
                    RETURN COUNT(r)
                $cypher$) AS (total BIGINT)
            )
        SELECT total_compounds.total, total_elements.total, total_edges.total, compound, element, relationship
        FROM total_compounds, total_elements, total_edges, LATERAL (
            SELECT *
            FROM cypher('chemical_compound_graph', $cypher$
                MATCH (c:Compound)-[r:HAS_ELEMENT]->(e:Element)
                WHERE c.name = %L
                RETURN c, e, r
                SKIP %s
                LIMIT %s
            $cypher$) AS (compound agtype, element agtype, relationship agtype)
        ) AS paginated_results
        $query$,
		compound_name, compound_name, compound_name, compound_name, "offset", "limit"
    );
END;
$function$ LANGUAGE plpgsql;

SELECT * FROM ag_catalog.get_compound_data_by_name('1-Amino-2-propanol', 0, 10);

DROP FUNCTION ag_catalog.find_all;
CREATE OR REPLACE FUNCTION ag_catalog.find_all("offset" BIGINT, "limit" BIGINT)
RETURNS TABLE(total_compounds BIGINT, total_elements BIGINT, total_edges BIGINT, compound agtype, element agtype, relationship agtype) AS $function$
BEGIN
    RETURN QUERY EXECUTE FORMAT(
        $query$
        WITH total_compounds AS (
                SELECT * FROM cypher('chemical_compound_graph', $cypher$
                    MATCH (c:Compound)
                    RETURN COUNT(c)
                $cypher$) AS (total BIGINT)
            ),
        total_elements AS (
                SELECT * FROM cypher('chemical_compound_graph', $cypher$
                    MATCH (e:Element)
                    RETURN COUNT(e)
                $cypher$) AS (total BIGINT)
            ),
        total_edges AS (
                SELECT * FROM cypher('chemical_compound_graph', $cypher$
                    MATCH ()-[r:HAS_ELEMENT]->()
                    RETURN COUNT(r)
                $cypher$) AS (total BIGINT)
            )
        SELECT total_compounds.total, total_elements.total, total_edges.total, compound, element, relationship
        FROM total_compounds, total_elements, total_edges, LATERAL (
            SELECT *
            FROM cypher('chemical_compound_graph', $cypher$
                MATCH (c:Compound)-[r:HAS_ELEMENT]->(e:Element)
                RETURN c, e, r
                SKIP %s
                LIMIT %s
            $cypher$) AS (compound agtype, element agtype, relationship agtype)
        ) AS paginated_results
        $query$,
        "offset", "limit"
    );
END;
$function$ LANGUAGE plpgsql;

SELECT * FROM ag_catalog.find_all(0, 10);

SELECT * FROM cypher('chemical_compound_graph', $$
    MATCH (c:Compound {formula: 'C3H9NO'})-[:HAS_ELEMENT]->(e:Element)
    RETURN SUM(e.count) AS total_atoms
$$) AS (total_atoms int);

SELECT * FROM cypher('chemical_compound_graph', $$
    MATCH (c:Compound {formula: 'C3H9NO'})-[:HAS_ELEMENT]->(e:Element {symbol: 'O'})
    RETURN e.symbol, e.count
$$) AS (symbol text, count int);

-- Create atom nodes and bond relationships
SELECT * FROM cypher('molecule_graph', $$
    CREATE (c1:Atom {index: 1, element: 'C'})-[:BOND {type: 'single'}]->(c2:Atom {index: 2, element: 'C'}),
           (c2)-[:BOND {type: 'single'}]->(n:Atom {index: 3, element: 'N'}),
           (c2)-[:BOND {type: 'single'}]->(o:Atom {index: 4, element: 'O'})
$$) AS (result agtype);

SELECT * FROM cypher('molecule_graph', $$
    MATCH (a:Atom)-[b:BOND]->(a1:Atom)
    RETURN a.element AS atom1, b.type AS bondType, a1.element AS atom2
$$) AS (atom1 text, bondType text, atom2 text);

SELECT * FROM cypher('molecule_graph', $$
    MATCH (a:Atom {element: 'C', index: 2})-[b:BOND]->(neighbor:Atom)
    RETURN neighbor.element AS connected_atom, b.type AS bondType
$$) AS (connected_atom text, bondType text);

SELECT * FROM cypher('molecule_graph', $$
    MATCH (a:Atom)-[b:BOND]->()
    RETURN a.element AS atom, COUNT(b) AS bond_count
$$) AS (atom text, bond_count int);
