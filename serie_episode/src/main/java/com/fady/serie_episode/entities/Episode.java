package com.fady.serie_episode.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Episode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEpisode;
	private String titreEpisode;
	private int numeroEpisode;
	private Double duree;
	private int saison;

	@ManyToOne
	private Serie serie;

	public Episode() {
		super();
	}

	public Episode(String titreEpisode, int numeroEpisode, Double duree, int saison) {
		super();
		this.titreEpisode = titreEpisode;
		this.numeroEpisode = numeroEpisode;
		this.duree = duree;
		this.saison = saison;
	}

	public Episode(String titreEpisode, int numeroEpisode, Double duree, int saison, Serie serie) {
		super();
		this.titreEpisode = titreEpisode;
		this.numeroEpisode = numeroEpisode;
		this.duree = duree;
		this.saison = saison;
		this.serie = serie;
	}

	public Long getIdEpisode() {
		return idEpisode;
	}

	public void setIdEpisode(Long idEpisode) {
		this.idEpisode = idEpisode;
	}

	public String getTitreEpisode() {
		return titreEpisode;
	}

	public void setTitreEpisode(String titreEpisode) {
		this.titreEpisode = titreEpisode;
	}

	public int getNumeroEpisode() {
		return numeroEpisode;
	}

	public void setNumeroEpisode(int numeroEpisode) {
		this.numeroEpisode = numeroEpisode;
	}

	public Double getDuree() {
		return duree;
	}

	public void setDuree(Double duree) {
		this.duree = duree;
	}

	public int getSaison() {
		return saison;
	}

	public void setSaison(int saison) {
		this.saison = saison;
	}

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
	}

	@Override
	public String toString() {
		return "Episode [idEpisode=" + idEpisode + ", titreEpisode=" + titreEpisode + ", numeroEpisode=" + numeroEpisode
				+ ", duree=" + duree + ", saison=" + saison + "]";
	}
}
