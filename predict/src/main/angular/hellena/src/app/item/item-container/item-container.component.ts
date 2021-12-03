import {Component, OnDestroy, OnInit} from '@angular/core';
import {ItemCardDto} from "../item-card/item-card-dto";
import {ItemService} from "../item.service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-item-container',
  templateUrl: './item-container.component.html',
  styleUrls: ['./item-container.component.css']
})
export class ItemContainerComponent implements OnInit {

  item: ItemCardDto = { name: 'test', storeName: 'Lidl Velika Gorica', orginalPrice: 19.99, actionPrice: 11.99 };

  items$: Observable<ItemCardDto[]> = new Observable<ItemCardDto[]>();

  constructor(private service: ItemService) { }

  ngOnInit(): void {
    this.items$ = this.service.getAll()
  }

}
