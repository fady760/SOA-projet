import { Component, OnInit } from '@angular/core';
import { Serie } from '../model/serie.model';
import { EpisodeService } from '../services/episode.service';
import { CommonModule } from '@angular/common';
import { UpdateSerieComponent } from '../update-serie/update-serie.component';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-liste-series',
  standalone: true,
  imports: [CommonModule, UpdateSerieComponent],
  templateUrl: './liste-series.component.html',
  styles: ``
})
export class ListeSeriesComponent implements OnInit {
  series!: Serie[];
  updatedSerie: Serie = { "idSerie": 0, "titre": "", "genre": "", "anneeDebut": 2024 };

  ajout: boolean = true;

  constructor(private episodeService: EpisodeService,
              public authService: AuthService) { }

  ngOnInit(): void {
    this.chargerSeries();
  }

  serieUpdated(serie: Serie) {
    console.log("Serie updated event", serie);
    if (this.ajout) {
      this.episodeService.ajouterSerie(serie).subscribe(() => {
        this.chargerSeries();
        this.updatedSerie = { "idSerie": 0, "titre": "", "genre": "", "anneeDebut": 2024 };
      });
    } else {
      this.episodeService.updateSerie(serie).subscribe(() => {
        this.chargerSeries();
        this.ajout = true;
        this.updatedSerie = { "idSerie": 0, "titre": "", "genre": "", "anneeDebut": 2024 };
      });
    }
  }
  
  chargerSeries() {
    this.episodeService.listeSeries().subscribe(series => {
      this.series = series._embedded.series;
      console.log(series);
    });
  }

  updateSerie(serie: Serie) {
    this.updatedSerie = { ...serie };
    this.ajout = false;  
  }

  supprimerSerie(serie: Serie) {
    let conf = confirm("Etes-vous sûr de vouloir supprimer cette série ?");
    if (conf) {
      this.episodeService.supprimerSerie(serie.idSerie).subscribe(() => {
        console.log("série supprimée");
        this.chargerSeries();
      });
    }
  }
}
