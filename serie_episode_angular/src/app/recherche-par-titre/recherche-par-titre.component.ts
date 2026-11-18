import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Episode } from '../model/episode.model';
import { EpisodeService } from '../services/episode.service';
import { SearchFilterPipe } from '../search-filter.pipe';

@Component({
  selector: 'app-recherche-par-titre',
  standalone: true,
  imports: [FormsModule, CommonModule, SearchFilterPipe],
  templateUrl: './recherche-par-titre.component.html',
  styles: ``
})
export class RechercheParTitreComponent implements OnInit {

  titreEpisode!: string;
  episodes!: Episode[];
  allEpisodes!: Episode[];
  searchTerm!: string;
  
  constructor(private episodeService: EpisodeService) { }

  ngOnInit(): void {
    this.episodeService.listeEpisodes().subscribe(eps => {
      console.log(eps);
      this.episodes = eps;
      this.allEpisodes = eps;
    });
  }

  rechercherEps() {
    if (!this.titreEpisode || this.titreEpisode.trim() === '') {
      this.ngOnInit();
    } else {
      this.episodeService.rechercherParTitre(this.titreEpisode).subscribe(eps => {
        console.log(eps);
        this.episodes = eps;
      });
    }
  }

  onKeyUp(filterText: string) {
    if (!filterText) {
      this.episodes = this.allEpisodes;
    } else {
      this.episodes = this.allEpisodes.filter(item =>
        item.titreEpisode?.toLowerCase().includes(filterText.toLowerCase())
      );
    }
  }
}
