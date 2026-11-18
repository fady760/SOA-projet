import { Component, OnInit } from '@angular/core';
import { Episode } from '../model/episode.model';
import { Serie } from '../model/serie.model';
import { EpisodeService } from '../services/episode.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-recherche-par-serie',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './recherche-par-serie.component.html',
  styles: ``
})
export class RechercheParSerieComponent implements OnInit {

  episodes!: Episode[];
  IdSerie!: number;
  series!: Serie[];

  constructor(private episodeService: EpisodeService) {}

  ngOnInit(): void {
    this.episodeService.listeSeries().subscribe(series => {
      this.series = series._embedded.series;
      console.log(series);
    });
  }

  onChange() {
    this.episodeService.rechercherParSerie(this.IdSerie).subscribe(eps => {
      this.episodes = eps;
    });
  }
}
