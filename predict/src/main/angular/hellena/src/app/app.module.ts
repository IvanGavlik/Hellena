import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {NavigationModule} from './navigation/navigation.module';
import {HeaderModule} from "./header/header.module";
import {FooterModule} from "./footer/footer.module";
import {ItemModule} from "./item/item.module";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    NavigationModule,
    HeaderModule,
    FooterModule,
    BrowserModule,
    AppRoutingModule,
    ItemModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
