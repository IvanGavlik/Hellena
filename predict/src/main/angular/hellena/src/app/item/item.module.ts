import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ItemCardComponent } from './item-card/item-card.component';
import { ItemContainerComponent } from './item-container/item-container.component';
import {HttpClientModule} from "@angular/common/http";



@NgModule({
  declarations: [
    ItemCardComponent,
    ItemContainerComponent
  ],
  imports: [
    CommonModule,
    HttpClientModule,
  ],
  exports: [
    ItemContainerComponent,
  ]
})
export class ItemModule { }
