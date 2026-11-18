import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Episode } from '../model/episode.model';
import { EpisodeService } from '../services/episode.service';
import { RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-episodes',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './episodes.component.html',
  styleUrl: './episodes.component.css'
})
export class EpisodesComponent implements OnInit {
  episodes!: Episode[];

  constructor(private episodeService: EpisodeService,
              public authService: AuthService) {}

  ngOnInit() {
    this.chargerEpisodes();
  }

  chargerEpisodes() {
    this.episodeService.listeEpisodes().subscribe(eps => {
      console.log(eps);
      this.episodes = eps;
    }); 
  }

  supprimerEpisode(e: Episode) {
    let conf = confirm("Etes-vous sûr ?");
    if (conf)
      this.episodeService.supprimerEpisode(e.idEpisode).subscribe(() => {
        console.log("épisode supprimé");
        this.chargerEpisodes();
      });
  } 
}
