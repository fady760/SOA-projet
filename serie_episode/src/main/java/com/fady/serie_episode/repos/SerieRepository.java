package com.fady.serie_episode.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fady.serie_episode.entities.Serie;

public interface SerieRepository extends JpaRepository<Serie, Long> {
}
