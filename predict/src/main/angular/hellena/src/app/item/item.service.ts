import {Inject, Injectable} from '@angular/core';
import {CrudService} from "../crud/crud.service";
import {CrudConfiguration} from "../crud/crud-configuration";
import {ItemConfiguration} from "./item-configuration";

@Injectable({
  providedIn: 'root'
})
export class ItemService extends CrudService {

  constructor(protected configuration: ItemConfiguration) {
    super(configuration);
  }

  /*  getAll(): Observable<ItemCardDto[]> {
      const base = environment.host + environment.version1;
      const endpoint = base + '/item/all';
      return this.http.get<ItemCardDto[]>(endpoint, {
        responseType: 'json'
      })
    }*/
}
