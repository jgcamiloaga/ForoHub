-- Creating the 'topicos' table
CREATE TABLE topicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    autorx_id BIGINT NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    mensaje VARCHAR(200) NOT NULL,
    nombre_curso VARCHAR(14) NOT NULL,
    status VARCHAR(100) NOT NULL,
    fecha DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_topicos_autorx_id FOREIGN KEY (autorx_id) REFERENCES autorxs(id)
);

