import { Injectable } from '@angular/core'; 
import { Episode } from '../model/episode.model';
import { Serie } from '../model/serie.model';
import { Observable, map } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SerieWrapper } from '../model/serieWrapped.model';
import { environment } from '../../environments/environment';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root',
})
export class EpisodeService {
  episodes!: Episode[];
  episode!: Episode;
  series!: Serie[];

  apiURL: string = environment.apiURL;
  apiURLSerie: string = environment.apiURLSerie;

  constructor(private http: HttpClient) {}

  listeEpisodes(): Observable<Episode[]> {
    return this.http.get<Episode[]>(this.apiURL);
  }

  ajouterEpisode(ep: Episode): Observable<Episode> {
    return this.http.post<Episode>(this.apiURL, ep, httpOptions);
  }
   
  supprimerEpisode(id: number) {
    const url = `${this.apiURL}/${id}`;
    return this.http.delete(url, httpOptions);
  }
    
  consulterEpisode(id: number): Observable<Episode> {
    const url = `${this.apiURL}/${id}`;
    return this.http.get<Episode>(url);
  }

  updateEpisode(ep: Episode): Observable<Episode> {
    return this.http.put<Episode>(this.apiURL, ep, httpOptions);
  }

  listeSeries(): Observable<SerieWrapper> {
    return this.http.get<any>(this.apiURLSerie).pipe(
      map(res => {
        if (res && res._embedded) return res;
        return { _embedded: { series: Array.isArray(res) ? res : [] } };
      })
    );
  }     

  consulterSerie(id: number): Serie {
    return this.series.find((serie) => serie.idSerie == id)!;
  }

  rechercherParSerie(idSerie: number): Observable<Episode[]> {
    const url = `${this.apiURL}/episodesserie/${idSerie}`;
    return this.http.get<Episode[]>(url);
  }

  rechercherParTitre(titre: string): Observable<Episode[]> {
    const url = `${this.apiURL}/episodesByTitre/${titre}`;
    return this.http.get<Episode[]>(url);
  }

  ajouterSerie(serie: Serie): Observable<Serie> {
    return this.http.post<Serie>(this.apiURLSerie, serie, httpOptions);
  }

  updateSerie(serie: Serie): Observable<Serie> {
    return this.http.put<Serie>(this.apiURLSerie, serie, httpOptions);
  }

  supprimerSerie(id: number): Observable<void> {
    const url = `${this.apiURLSerie}/${id}`;
    return this.http.delete<void>(url, httpOptions);
  }
}
