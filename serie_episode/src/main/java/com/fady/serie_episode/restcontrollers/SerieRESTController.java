package com.fady.serie_episode.restcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fady.serie_episode.entities.Episode;
import com.fady.serie_episode.entities.Serie;
import com.fady.serie_episode.repos.SerieRepository;
import com.fady.serie_episode.service.EpisodeService;

@RestController
@RequestMapping("/series")
@CrossOrigin(origins = "*")
public class SerieRESTController {

	@Autowired
	private SerieRepository serieRepository;

	@Autowired
	private EpisodeService episodeService;

	@RequestMapping(method = RequestMethod.GET)
	public List<Serie> getAllSeries() {
		return serieRepository.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Serie getSerieById(@PathVariable("id") Long id) {
		return serieRepository.findById(id).orElse(null);
	}

	@RequestMapping(method = RequestMethod.POST)
	public Serie createSerie(@RequestBody Serie serie) {
		return serieRepository.save(serie);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public Serie updateSerie(@RequestBody Serie serie) {
		return serieRepository.save(serie);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteSerie(@PathVariable("id") Long id) {
		List<Episode> episodes = episodeService.findBySerieIdSerie(id);
		for (Episode ep : episodes) {
			episodeService.deleteEpisode(ep);
		}
		serieRepository.deleteById(id);
	}
}
