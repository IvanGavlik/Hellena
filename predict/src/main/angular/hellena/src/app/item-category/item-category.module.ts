import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ItemCategoryContainerComponent } from './item-category-container/item-category-container.component';



@NgModule({
  declarations: [
    ItemCategoryContainerComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    ItemCategoryContainerComponent
  ]
})
export class ItemCategoryModule { }
