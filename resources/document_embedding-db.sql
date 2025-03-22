DROP EXTENSION  IF EXISTS vector CASCADE;
CREATE EXTENSION vector;

DROP TABLE IF EXISTS document_embedding;
DROP FUNCTION IF EXISTS match_documents;

CREATE TABLE document_embedding (
  id bigserial primary key,
  content text,
  metadata jsonb,
  embedding vector(1024)
);

CREATE FUNCTION match_documents (
  query_embedding vector(1024),
  match_threshold float,
  match_limit int DEFAULT 10
) returns table (
  id bigint,
  content text,
  metadata jsonb,
  similarity float
)
language plpgsql
as $$
#variable_conflict use_column
begin
  return query
  select
    id,
    content,
    metadata,
    1 - (embedding <=> query_embedding) as similarity
  from document_embedding
  where 1 - (embedding <=> query_embedding) > match_threshold
  order by embedding <=> query_embedding
  limit match_limit;
end;
$$;
