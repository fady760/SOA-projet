package com.fady.serie_episode.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.fady.serie_episode.entities.Episode;
import com.fady.serie_episode.entities.Serie;

@RepositoryRestResource(path = "rest")
public interface EpisodeRepository extends JpaRepository<Episode, Long> {

	List<Episode> findByTitreEpisode(String titre);

	List<Episode> findByTitreEpisodeContains(String titre);

	@Query("select e from Episode e where e.titreEpisode like %:titre and e.duree > :duree")
	List<Episode> findByTitreDuree(@Param("titre") String titre, @Param("duree") Double duree);

	@Query("select e from Episode e where e.serie = ?1")
	List<Episode> findBySerie(Serie serie);

	List<Episode> findBySerieIdSerie(Long id);

	List<Episode> findByOrderByTitreEpisodeAsc();

	@Query("select e from Episode e order by e.titreEpisode ASC, e.duree DESC")
	List<Episode> trierEpisodesTitreDuree();
}
