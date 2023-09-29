import {Injectable} from '@angular/core';
import {Observable, Subject} from "rxjs";
import {IModalData} from "../../interfaces/IModalData";
import {IMessage} from "../../interfaces/IMessage";
import {SpinnerService} from "../spinner/spinner.service";

@Injectable()
export class ModalService {

  #control$: Subject<IModalData | null> = new Subject();

  public open(data: IModalData | null): void {
    this.#control$.next(data);
    SpinnerService.visibility.next(false);
  }

  public close(): void {
    this.#control$.next(null);
  }

  public get modalSequence$(): Observable<any> {
    return this.#control$.asObservable();
  }

  showMessage(message: IMessage) {
    (async (): Promise<void> => {
      const {ItemComponent} = await import('../../components/item/item.component');
      this.open({
        component: ItemComponent,
        context: {
          message: message,
          save: () => this.close(),
          close: () => this.close(),
        },
      });
    })();
  }
}
