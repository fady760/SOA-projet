import { Routes } from '@angular/router';
import { EpisodesComponent } from './episodes/episodes.component';
import { AddEpisodeComponent } from './add-episode/add-episode.component';
import { UpdateEpisodeComponent } from './update-episode/update-episode.component';
import { RechercheParSerieComponent } from './recherche-par-serie/recherche-par-serie.component';
import { RechercheParTitreComponent } from './recherche-par-titre/recherche-par-titre.component';
import { ListeSeriesComponent } from './liste-series/liste-series.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { VerifEmailComponent } from './verif-email/verif-email.component';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { episodeGuard } from './episode.guard';
import { authGuard } from './auth.guard';

export const routes: Routes = [
    { path: "episodes", component: EpisodesComponent, canActivate: [authGuard] },
    { path: "add-episode", component: AddEpisodeComponent, canActivate: [episodeGuard] },  
    { path: "updateEpisode/:id", component: UpdateEpisodeComponent, canActivate: [episodeGuard] },
    { path: "rechercheParSerie", component: RechercheParSerieComponent, canActivate: [authGuard] },
    { path: "rechercheParTitre", component: RechercheParTitreComponent, canActivate: [authGuard] },
    { path: "listeSeries", component: ListeSeriesComponent, canActivate: [authGuard] },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'verifEmail', component: VerifEmailComponent },
    { path: 'app-forbidden', component: ForbiddenComponent },
    { path: "", redirectTo: "episodes", pathMatch: "full" }
];
