import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Episode } from '../model/episode.model';
import { EpisodeService } from '../services/episode.service';
import { Serie } from '../model/serie.model';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-episode',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-episode.component.html'
})
export class AddEpisodeComponent implements OnInit {

  newEpisode = new Episode();
  message!: string;
  series!: Serie[];
  newIdSerie!: number;
  newSerie!: Serie;

  constructor(private episodeService: EpisodeService,
              private router: Router) {}

  ngOnInit(): void {
    this.episodeService.listeSeries().subscribe(series => {
      console.log(series);
      this.series = series._embedded.series;
    });
  }

  addEpisode() {
    this.newEpisode.serie = this.series.find(serie => serie.idSerie == this.newIdSerie)!;
    this.episodeService.ajouterEpisode(this.newEpisode).subscribe(ep => {
      console.log(ep);
      this.router.navigate(['episodes']);
    }); 
  }
}
