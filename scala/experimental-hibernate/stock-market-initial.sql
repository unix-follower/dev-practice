--DROP TABLE stock_market.stock;

CREATE TABLE stock_market.stock (
  ticker VARCHAR(255) NOT NULL,
  date_at TIMESTAMP WITH TIME ZONE NOT NULL,
  open DOUBLE PRECISION NOT NULL,
  high DOUBLE PRECISION NOT NULL,
  low DOUBLE PRECISION NOT NULL,
  close DOUBLE PRECISION NOT NULL,
  adjusted_close DOUBLE PRECISION NOT NULL,
  volume BIGINT NOT NULL,
  dividends REAL NOT NULL,
  stock_splits REAL NOT NULL,
  capital_gains REAL NOT NULL,
  PRIMARY KEY (ticker, date_at)
);
