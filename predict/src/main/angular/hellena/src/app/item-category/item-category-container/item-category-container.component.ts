import { Component, OnInit } from '@angular/core';
import {ItemCategoryConfiguration} from "../item-category-configuration";
import {ItemCategoryService} from "../item-category.service";

@Component({
  selector: 'app-item-category-container',
  templateUrl: './item-category-container.component.html',
  styleUrls: ['./item-category-container.component.css'],
  providers: [
    {provide: 'configuration', useClass: ItemCategoryConfiguration}
  ]
})
export class ItemCategoryContainerComponent implements OnInit {

  constructor(private service: ItemCategoryService) { }

  ngOnInit(): void {
    this.service.findAll().subscribe( el => el.forEach(i => console.log(i)));
  }

}
