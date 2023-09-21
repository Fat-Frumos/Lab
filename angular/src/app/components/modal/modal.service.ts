import {Injectable} from '@angular/core';
import {Observable, Subject} from "rxjs";
import {IModalData} from "../../interfaces/IModalData";

@Injectable()
export class ModalService {

  #control$: Subject<IModalData | null> = new Subject();

  public open(data: IModalData | null): void {
    this.#control$.next(data);
  }

  public close(): void {
    this.#control$.next(null);
  }

  public get modalSequence$(): Observable<any> {
    return this.#control$.asObservable();
  }
}
