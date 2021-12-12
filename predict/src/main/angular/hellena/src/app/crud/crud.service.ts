import {Crud} from "./crud";
import {Observable, of} from "rxjs";
import {Entity} from "./entity";
import {CrudConfiguration} from "./crud-configuration";

export abstract class CrudService implements Crud {

  constructor(private crudConfiguration: CrudConfiguration) {}

  count(): Observable<Number> {
    return of<Number>();
  }

  delete(entity: Entity): void {}

  deleteAll(entity: Entity[]): void;
  deleteAll(): void;
  deleteAll(entity?: Entity[]): void {}

  deleteAllById(id: string[]): void {}

  deleteById(id: string): void {}

  existById(id: string): Observable<true> {
    return of();
  }

  findAll(): Observable<Entity[]> {
    console.log('findAll ', this.crudConfiguration.endpoint);
    return of();
  }

  findAllById(id: string[]): Observable<Entity[]> {
    return of();
  }

  findById(id: string): Observable<Entity> {
    return of();
  }

  save(entity: Entity): Observable<Entity> {
    return of();
  }

  saveAll(entityList: Entity[]): Observable<Entity[]> {
    return of();
  }
}

