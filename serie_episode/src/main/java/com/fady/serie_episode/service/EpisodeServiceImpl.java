package com.fady.serie_episode.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fady.serie_episode.entities.Episode;
import com.fady.serie_episode.entities.Serie;
import com.fady.serie_episode.repos.EpisodeRepository;

@Service
public class EpisodeServiceImpl implements EpisodeService {

	@Autowired
	EpisodeRepository episodeRepository;

	@Override
	public Episode saveEpisode(Episode e) {
		return episodeRepository.save(e);
	}

	@Override
	public Episode updateEpisode(Episode e) {
		return episodeRepository.save(e);
	}

	@Override
	public void deleteEpisode(Episode e) {
		episodeRepository.delete(e);
	}

	@Override
	public void deleteEpisodeById(Long id) {
		episodeRepository.deleteById(id);
	}

	@Override
	public Episode getEpisode(Long id) {
		return episodeRepository.findById(id).get();
	}

	@Override
	public List<Episode> getAllEpisodes() {
		return episodeRepository.findAll();
	}

	@Override
	public List<Episode> findByTitreEpisode(String titre) {
		return episodeRepository.findByTitreEpisode(titre);
	}

	@Override
	public List<Episode> findByTitreEpisodeContains(String titre) {
		return episodeRepository.findByTitreEpisodeContains(titre);
	}

	@Override
	public List<Episode> findByTitreDuree(String titre, Double duree) {
		return episodeRepository.findByTitreDuree(titre, duree);
	}

	@Override
	public List<Episode> findBySerie(Serie serie) {
		return episodeRepository.findBySerie(serie);
	}

	@Override
	public List<Episode> findBySerieIdSerie(Long id) {
		return episodeRepository.findBySerieIdSerie(id);
	}

	@Override
	public List<Episode> findByOrderByTitreEpisodeAsc() {
		return episodeRepository.findByOrderByTitreEpisodeAsc();
	}

	@Override
	public List<Episode> trierEpisodesTitreDuree() {
		return episodeRepository.trierEpisodesTitreDuree();
	}
}
