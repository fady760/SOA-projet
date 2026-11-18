import { Serie } from "./serie.model";

export class Episode {
    idEpisode!: number;
    titreEpisode!: string;
    numeroEpisode!: number;
    duree!: number;
    saison!: number;
    serie!: Serie;
}
