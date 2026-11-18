package com.fady.serie_episode.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Serie {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSerie;
	private String titre;
	private String genre;
	private int anneeDebut;

	@JsonIgnore
	@OneToMany(mappedBy = "serie")
	private List<Episode> episodes;
}
