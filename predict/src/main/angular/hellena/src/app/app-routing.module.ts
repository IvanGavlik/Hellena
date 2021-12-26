import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {FrontPageComponent} from "./front-page/front-page/front-page.component";
import {AboutUsComponent} from "./about-us-page/about-us/about-us.component";
import {FrontPageModule} from "./front-page/front-page.module";
import {AboutUsPageModule} from "./about-us-page/about-us-page.module";

const routes: Routes = [
  { path: 'index', component: FrontPageComponent },
  { path: 'about-us', component: AboutUsComponent },
  { path: '', redirectTo: '/index',  pathMatch: 'full' }

];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
    FrontPageModule,
    AboutUsPageModule,
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
