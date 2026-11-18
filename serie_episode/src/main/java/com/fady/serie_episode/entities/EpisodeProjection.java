package com.fady.serie_episode.entities;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "titreEp", types = { Episode.class })
public interface EpisodeProjection {
	public String getTitreEpisode();
}
