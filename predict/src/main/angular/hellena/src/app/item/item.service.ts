import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ItemCardDto} from "./item-card/item-card-dto";
import {Observable, of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<ItemCardDto[]> {
    return this.http.get<ItemCardDto[]>('https://mysterious-dusk-84277.herokuapp.com/v1/item/all', {
      headers: {
        'Access-Control-Allow-Origin': '*'
      },
      responseType: 'json'
    })
  }
}
