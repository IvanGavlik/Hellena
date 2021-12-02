import { Component, OnInit } from '@angular/core';
import {ItemCardDto} from "../item-card/item-card-dto";

@Component({
  selector: 'app-item-container',
  templateUrl: './item-container.component.html',
  styleUrls: ['./item-container.component.css']
})
export class ItemContainerComponent implements OnInit {

  item: ItemCardDto = { name: 'test', storeName: 'Lidl Velika Gorica', orginalPrice: 19.99, actionPrice: 11.99 };

  constructor() { }

  ngOnInit(): void {
  }

}
