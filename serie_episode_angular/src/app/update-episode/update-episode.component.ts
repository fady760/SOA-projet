import { Component, OnInit } from '@angular/core';
import { Episode } from '../model/episode.model';
import { ActivatedRoute, Router } from '@angular/router';
import { EpisodeService } from '../services/episode.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Serie } from '../model/serie.model';

@Component({
  selector: 'app-update-episode',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './update-episode.component.html',
  styles: ``,
})
export class UpdateEpisodeComponent implements OnInit {
  currentEpisode = new Episode();
  series!: Serie[];
  updatedSerieId!: number;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private episodeService: EpisodeService
  ) {}

  ngOnInit(): void {
    this.episodeService.listeSeries().subscribe(series => {
      console.log(series);
      this.series = series._embedded.series;
    });

    this.episodeService.consulterEpisode(this.activatedRoute.snapshot.params['id']).
    subscribe(ep => {
      this.currentEpisode = ep; 
      this.updatedSerieId = this.currentEpisode.serie?.idSerie;
    });
  }

  updateEpisode() {
    this.currentEpisode.serie = this.series.find(
      (serie) => serie.idSerie == this.updatedSerieId
    )!;
    this.episodeService.updateEpisode(this.currentEpisode).subscribe((ep) => {
      this.router.navigate(['episodes']);
    });
  }
}
