-- Creating the 'autorxs' table
CREATE TABLE autorxs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL,
    clave VARCHAR(300) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

-- Inserting a record into the 'autorxs' table
INSERT INTO autorxs (id, email, clave) VALUES
(1, 'ana.souza@voll.med', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.');
