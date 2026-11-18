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
import com.fady.serie_episode.service.EpisodeService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EpisodeRESTController {

	@Autowired
	EpisodeService episodeService;

	@RequestMapping(path = {"", "/all"}, method = RequestMethod.GET)
	public List<Episode> getAllEpisodes() {
		return episodeService.getAllEpisodes();
	}

	@RequestMapping(value = {"/{id}", "/getbyid/{id}"}, method = RequestMethod.GET)
	public Episode getEpisodeById(@PathVariable("id") Long id) {
		return episodeService.getEpisode(id);
	}

	@RequestMapping(path = {"", "/addepisode"}, method = RequestMethod.POST)
	public Episode createEpisode(@RequestBody Episode episode) {
		return episodeService.saveEpisode(episode);
	}

	@RequestMapping(path = {"", "/updateepisode"}, method = RequestMethod.PUT)
	public Episode updateEpisode(@RequestBody Episode episode) {
		return episodeService.updateEpisode(episode);
	}

	@RequestMapping(value = {"/{id}", "/delepisode/{id}"}, method = RequestMethod.DELETE)
	public void deleteEpisode(@PathVariable("id") Long id) {
		episodeService.deleteEpisodeById(id);
	}

	@RequestMapping(value = "/episodesserie/{idSerie}", method = RequestMethod.GET)
	public List<Episode> getEpisodesBySerieId(@PathVariable("idSerie") Long idSerie) {
		return episodeService.findBySerieIdSerie(idSerie);
	}

	@RequestMapping(value = "/episodesByTitre/{titre}", method = RequestMethod.GET)
	public List<Episode> getEpisodesByTitre(@PathVariable("titre") String titre) {
		return episodeService.findByTitreEpisodeContains(titre);
	}
}
