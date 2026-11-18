import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Serie } from '../model/serie.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-update-serie',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './update-serie.component.html',
  styles: ``
})
export class UpdateSerieComponent implements OnInit {

  @Input()
  serie!: Serie;

  @Output() 
  serieUpdated = new EventEmitter<Serie>();

  @Input()
  ajout!: boolean;

  ngOnInit(): void {
    console.log("ngOnInit du composant UpdateSerie ", this.serie);
  }

  saveSerie() {
    this.serieUpdated.emit(this.serie);
  }
}
