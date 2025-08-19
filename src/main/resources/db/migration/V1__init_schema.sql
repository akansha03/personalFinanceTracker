-- Enable required extensions (safe to run multiple times)

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Categories

CREATE TABLE IF NOT EXISTS categories (
  id          BIGSERIAL PRIMARY KEY,
  name        VARCHAR(100) NOT NULL,
  type        VARCHAR(10)  NOT NULL CHECK (type IN ('Income','Expense')),
  CONSTRAINT uq_category UNIQUE (name, type)
);

-- Transactions

CREATE TABLE IF NOT EXISTS transactions (
  transaction_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  date           TIMESTAMPTZ NOT NULL,
  amount         NUMERIC(12,2) NOT NULL CHECK (amount >= 0),
  type           VARCHAR(10) NOT NULL CHECK (type IN ('Income','Expense')),
  category_id    BIGINT REFERENCES categories(id),
  account        VARCHAR(30),
  note           TEXT,
  image_url      TEXT,
  description    TEXT,
  created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Update updated_at automatically

CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_transactions_updated ON transactions;
CREATE TRIGGER trg_transactions_updated
BEFORE UPDATE ON transactions
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- Helpful indexes (adjust if you add user/session column later)

CREATE INDEX IF NOT EXISTS idx_transactions_date ON transactions(date);
CREATE INDEX IF NOT EXISTS idx_transactions_type ON transactions(type);
CREATE INDEX IF NOT EXISTS idx_transactions_category ON transactions(category_id);