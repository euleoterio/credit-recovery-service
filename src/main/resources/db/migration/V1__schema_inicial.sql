CREATE TABLE devedor (
                         id          UUID PRIMARY KEY,
                         nome        VARCHAR(150) NOT NULL,
                         documento   VARCHAR(20)  NOT NULL UNIQUE
);

CREATE TABLE divida (
                        id              UUID PRIMARY KEY,
                        devedor_id      UUID            NOT NULL REFERENCES devedor(id),
                        valor_original  NUMERIC(15, 2)  NOT NULL CHECK (valor_original > 0),
                        data_vencimento DATE            NOT NULL,
                        status          VARCHAR(30)     NOT NULL
);

CREATE INDEX idx_divida_devedor_id ON divida(devedor_id);
CREATE INDEX idx_divida_status     ON divida(status);