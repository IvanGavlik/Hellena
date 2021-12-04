import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ItemCardDto} from "./item-card/item-card-dto";
import {Observable, of} from "rxjs";
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<ItemCardDto[]> {
    const base = environment.host + environment.version1;
    const endpoint = base + '/item/all';
    return this.http.get<ItemCardDto[]>(endpoint, {
      responseType: 'json'
    })
  }
}
