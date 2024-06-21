package com.example.demo.domain.topicos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicoRepository extends JpaRepository<Topico,Long> {

    Boolean existsByTituloAndMensaje(String titulo,String mensaje);

    @Query("SELECT t FROM Topico t WHERE FUNCTION('YEAR', t.fecha) = :year")
    Page<Topico> findByFechaYear(@Param("year") int year, Pageable pageable);
}
