import {
  AfterViewInit,
  ChangeDetectorRef,
  Component, HostListener,
  OnDestroy,
  OnInit
} from '@angular/core';
import {Subject} from "rxjs";
import {ICriteria} from "../../../interfaces/ICriteria";
import {ScrollService} from "../../../services/scroll.service";
import {LocalStorageService} from "../../../services/local-storage.service";
import {CertificateService} from "../../../services/certificate.service";
import {Certificate} from "../../../model/Certificate";

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit, OnDestroy, AfterViewInit {
  page: number = 0;
  size: number = 25;
  loading: boolean = false;
  unSubscribers$: Subject<any> = new Subject();
  criteria: ICriteria = {name: '', tag: ''};

  constructor(
    private scroll: ScrollService,
    private storage: LocalStorageService,
    public service: CertificateService,
    private cdr: ChangeDetectorRef
  ) {
  }

  ngAfterViewInit(): void {
    console.log("After View Init")
    this.cdr.detectChanges();
  }

  ngOnInit(): void {
    const saved: Certificate[] = this.storage.getCertificates();
    if (saved.length !== 0) {
      this.service.certificates$ = saved;
    } else {
      this.service.loadMoreCertificates(this.page, this.size);
    }
    console.log("saved certificates in Storage : " + this.service.certificates$.length);
  }

  ngOnDestroy(): void {
    this.unSubscribers$.next(null);
    this.unSubscribers$.complete();
    this.scroll.saveScrollPosition();
  }

  @HostListener('window:scroll', ['$event'])
  onScroll(): void {
    if (
      !this.loading &&
      window.innerHeight + window.scrollY >= document.body.offsetHeight - 80
    ) {
      this.service.loadMoreCertificates(this.page, this.size);
    }
  }
}
