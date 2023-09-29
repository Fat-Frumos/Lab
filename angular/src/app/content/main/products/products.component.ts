import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  HostListener,
  OnDestroy,
  OnInit,
  ViewEncapsulation
} from '@angular/core';
import {Observable, Subject} from "rxjs";
import {ICriteria} from "../../../interfaces/ICriteria";
import {ScrollService} from "../../../services/scroll.service";
import {LocalStorageService} from "../../../services/local-storage.service";
import {ICertificate} from "../../../model/entity/ICertificate";
import {IState} from "../../../store/reducers";
import {Store} from "@ngrx/store";
import {
  getCertificatesPending
} from "../../../store/actions/certificate.action";
import {CertificateService} from "../../../services/certificate.service";

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ProductsComponent implements OnInit, OnDestroy, AfterViewInit {
  page: number = 0;
  size: number = 25;
  loading: boolean = false;
  unSubscribers$: Subject<any> = new Subject();
  criteria: ICriteria = {name: '', tag: ''};
  // public certificates$: Observable<ICertificate[]> = this.store.select('certificates', 'items');
  public loading$: Observable<boolean> = this.store.select('certificates', 'loading');

  constructor(
    private scroll: ScrollService,
    private storage: LocalStorageService,
    public service: CertificateService,
    public store: Store<IState>,
    private cdr: ChangeDetectorRef
  ) {
  }

  ngAfterViewInit(): void {
    console.log("After View Init")
    this.cdr.detectChanges();
    // this.scroll.restorePosition()
  }

  ngOnInit(): void {
    this.store.dispatch(getCertificatesPending())
    const saved: ICertificate[] = this.storage.getCertificates();
    if (saved.length !== 0) {
      this.service.certificates$ = saved;
    } else {
      this.service.loadMoreCertificates(this.page, this.size);
    }
    console.log("Saved certificates in Storage : " + this.service.certificates$.length);
  }

  ngOnDestroy(): void {
    this.unSubscribers$.next(null);
    this.unSubscribers$.complete();
    this.scroll.saveScrollPosition();
  }

  @HostListener('window:scroll', ['$event'])
  onScroll(): void {
    if (
      !this.loading && //TODO Observable store reducer
      window.innerHeight + window.scrollY >= document.body.offsetHeight - 80
    ) {
      this.service.loadMoreCertificates(this.page, this.size);
    }
  }
}
