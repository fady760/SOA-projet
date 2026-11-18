import { Serie } from './serie.model';

export class SerieWrapper {
    _embedded!: { series: Serie[] };
}
