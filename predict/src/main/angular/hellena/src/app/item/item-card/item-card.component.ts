import {Component, Input, OnInit} from '@angular/core';
import {ItemCardDto} from './item-card-dto';

@Component({
  selector: 'app-item-card',
  templateUrl: './item-card.component.html',
  styleUrls: ['./item-card.component.css']
})
export class ItemCardComponent implements OnInit {

  @Input() item?: ItemCardDto;

  constructor() { }

  ngOnInit(): void {
  }

}
