CREATE TABLE IF NOT EXISTS event_record
(
  id              BIGSERIAL PRIMARY KEY,
  event_name      TEXT NOT NULL,
  aggregate_id    TEXT NOT NULL,
  version         BIGINT NOT NULL,
  timestamp       BIGINT NOT NULL,
  global_id       BIGSERIAL NOT NULL,
  payload         JSONB NOT NULL,
  UNIQUE(aggregate_id, version)
);