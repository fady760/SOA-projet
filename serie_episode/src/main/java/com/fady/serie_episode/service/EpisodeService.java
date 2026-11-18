package com.fady.serie_episode.service;

import java.util.List;

import com.fady.serie_episode.entities.Episode;
import com.fady.serie_episode.entities.Serie;

public interface EpisodeService {

	Episode saveEpisode(Episode e);

	Episode updateEpisode(Episode e);

	void deleteEpisode(Episode e);

	void deleteEpisodeById(Long id);

	Episode getEpisode(Long id);

	List<Episode> getAllEpisodes();

	List<Episode> findByTitreEpisode(String titre);

	List<Episode> findByTitreEpisodeContains(String titre);

	List<Episode> findByTitreDuree(String titre, Double duree);

	List<Episode> findBySerie(Serie serie);

	List<Episode> findBySerieIdSerie(Long id);

	List<Episode> findByOrderByTitreEpisodeAsc();

	List<Episode> trierEpisodesTitreDuree();
}
