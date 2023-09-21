import {Component, OnDestroy, ViewEncapsulation} from '@angular/core';
import {Certificate} from "../../model/Certificate";
import {Observable, Subscription} from "rxjs";
import {LocalStorageService} from "../../services/local-storage.service";

@Component({
  selector: 'app-favorite',
  templateUrl: './favorite.component.html',
  styleUrls: ['./favorite.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class FavoriteComponent implements OnDestroy {
  certificates$!: Observable<Certificate[]>;
  private subscription!: Subscription;

  constructor(private storage: LocalStorageService) {
    this.certificates$ = this.storage.favoriteCertificates$;
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
