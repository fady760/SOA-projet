package com.fady.serie_episode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import com.fady.serie_episode.entities.Episode;
import com.fady.serie_episode.entities.Serie;
import com.fady.serie_episode.repos.EpisodeRepository;
import com.fady.serie_episode.repos.SerieRepository;

@SpringBootApplication
public class SerieEpisodeApplication implements CommandLineRunner {

	@Autowired
	private RepositoryRestConfiguration repositoryRestConfiguration;

	@Autowired
	private SerieRepository serieRepository;

	@Autowired
	private EpisodeRepository episodeRepository;

	public static void main(String[] args) {
		SpringApplication.run(SerieEpisodeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		repositoryRestConfiguration.exposeIdsFor(Episode.class, Serie.class);

		episodeRepository.deleteAll();
		serieRepository.deleteAll();

		Serie s1 = new Serie(null, "Breaking Bad", "Thriller", 2008, null);
		Serie s2 = new Serie(null, "Dark", "Science-Fiction", 2017, null);
		Serie s3 = new Serie(null, "Stranger Things", "Horreur", 2016, null);
		Serie s4 = new Serie(null, "Euphoria", "Drame", 2019, null);
		Serie s5 = new Serie(null, "Naruto Shippuden", "Anime", 2007, null);
		Serie s6 = new Serie(null, "One Piece", "Anime", 1999, null);

		serieRepository.save(s1);
		serieRepository.save(s2);
		serieRepository.save(s3);
		serieRepository.save(s4);
		serieRepository.save(s5);
		serieRepository.save(s6);

		// Breaking Bad
		episodeRepository.save(new Episode("Pilot", 1, 58.0, 1, s1));
		episodeRepository.save(new Episode("Cat's in the Bag", 2, 48.0, 1, s1));
		episodeRepository.save(new Episode("And the Bag's in the River", 3, 47.0, 1, s1));

		// Dark
		episodeRepository.save(new Episode("Secrets", 1, 53.0, 1, s2));
		episodeRepository.save(new Episode("Lies", 2, 51.0, 1, s2));
		episodeRepository.save(new Episode("Past and Present", 3, 55.0, 1, s2));

		// Stranger Things
		episodeRepository.save(new Episode("The Vanishing of Will Byers", 1, 49.0, 1, s3));
		episodeRepository.save(new Episode("The Weirdo on Maple Street", 2, 55.0, 1, s3));
		episodeRepository.save(new Episode("Holly, Jolly", 3, 51.0, 1, s3));

		// Euphoria (10 episodes)
		episodeRepository.save(new Episode("Pilot", 1, 55.0, 1, s4));
		episodeRepository.save(new Episode("Stuntin' Like My Daddy", 2, 53.0, 1, s4));
		episodeRepository.save(new Episode("Made You Look", 3, 54.0, 1, s4));
		episodeRepository.save(new Episode("Shook Ones Pt. II", 4, 60.0, 1, s4));
		episodeRepository.save(new Episode("'03 Bonnie and Clyde", 5, 52.0, 1, s4));
		episodeRepository.save(new Episode("The Next Episode", 6, 58.0, 1, s4));
		episodeRepository.save(new Episode("The Trials and Tribulations", 7, 56.0, 1, s4));
		episodeRepository.save(new Episode("And Salt the Earth", 8, 65.0, 1, s4));
		episodeRepository.save(new Episode("Trying to Get to Heaven", 1, 59.0, 2, s4));
		episodeRepository.save(new Episode("Out of Touch", 2, 57.0, 2, s4));

		// Naruto Shippuden (10 episodes)
		episodeRepository.save(new Episode("Homecoming", 1, 23.0, 1, s5));
		episodeRepository.save(new Episode("The Akatsuki Makes Its Move", 2, 23.0, 1, s5));
		episodeRepository.save(new Episode("The Results of Training", 3, 23.0, 1, s5));
		episodeRepository.save(new Episode("The Jinchuriki of the Sand", 4, 23.0, 1, s5));
		episodeRepository.save(new Episode("The Kazekage Stands Tall", 5, 23.0, 1, s5));
		episodeRepository.save(new Episode("Mission Cleared", 6, 23.0, 1, s5));
		episodeRepository.save(new Episode("Run, Kankuro", 7, 23.0, 1, s5));
		episodeRepository.save(new Episode("Team Kakashi, Deployed", 8, 23.0, 1, s5));
		episodeRepository.save(new Episode("The Jinchuriki's Tears", 9, 23.0, 1, s5));
		episodeRepository.save(new Episode("Sealing Jutsu", 10, 23.0, 1, s5));

		// One Piece (10 episodes)
		episodeRepository.save(new Episode("I'm Luffy!", 1, 24.0, 1, s6));
		episodeRepository.save(new Episode("Enter the Great Swordsman!", 2, 24.0, 1, s6));
		episodeRepository.save(new Episode("Morgan vs. Luffy", 3, 24.0, 1, s6));
		episodeRepository.save(new Episode("Luffy's Past!", 4, 24.0, 1, s6));
		episodeRepository.save(new Episode("A Terrifying Mysterious Power!", 5, 24.0, 1, s6));
		episodeRepository.save(new Episode("Desperate Situation", 6, 24.0, 1, s6));
		episodeRepository.save(new Episode("Epic Battle!", 7, 24.0, 1, s6));
		episodeRepository.save(new Episode("Who is the Victor?", 8, 24.0, 1, s6));
		episodeRepository.save(new Episode("The Honorable Liar", 9, 24.0, 1, s6));
		episodeRepository.save(new Episode("The Weirdest Guy Ever!", 10, 24.0, 1, s6));
	}
}
