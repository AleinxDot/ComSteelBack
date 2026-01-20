CREATE TABLE suppliers
(
    id         bigint IDENTITY (1, 1) NOT NULL,
    ruc        varchar(255)           NOT NULL,
    name       varchar(255)           NOT NULL,
    address    varchar(255),
    phone      varchar(255),
    email      varchar(255),
    created_at datetime,
    CONSTRAINT pk_suppliers PRIMARY KEY (id)
)
GO

ALTER TABLE suppliers
    ADD CONSTRAINT uc_suppliers_ruc UNIQUE (ruc)
GO