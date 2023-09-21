import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ExchangeService {
  private rateSource = new BehaviorSubject<{ value: number, currency: string }[]>([]);
  private indexSubject: BehaviorSubject<number> = new BehaviorSubject<number>(0);
  currentRates = this.rateSource.asObservable();

  get index$(): Observable<number> {
    return this.indexSubject.asObservable();
  }
  updateIndex(newIndex: number): void {
    this.indexSubject.next(newIndex);
  }
  changeRates(rates: { value: number, currency: string }[]) {
    this.rateSource.next(rates);
  }
}
