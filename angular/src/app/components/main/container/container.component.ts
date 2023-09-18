import {
  AfterViewInit, ChangeDetectorRef,
  Component,
  HostListener, OnDestroy,
  OnInit,
  ViewEncapsulation,
} from '@angular/core';
import {Certificate} from '../../../model/Certificate';
import {LocalStorageService} from '../../../services/local-storage.service';
import {ScrollService} from '../../../services/scroll.service';
import {CertificateService} from "../../../services/certificate.service";
import {ICriteria} from "../../../interfaces/ICriteria";

@Component({
  selector: 'app-container',
  templateUrl: './container.component.html',
  styleUrls: ['./container.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ContainerComponent implements OnInit, OnDestroy, AfterViewInit {
  page: number = 0;
  size: number = 25;
  loading: boolean = false;
  criteria: ICriteria = {name: '', tag: ''};
  certificates$: Certificate[] = [];

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
    const saved: Certificate[] = this.storage.getCertificatesFromLocalStorage();
    if (saved.length !== 0) {
      this.service.certificates$ = saved;
    } else {
      this.service.loadMoreCertificates(this.page, this.size);
      this.cdr.detectChanges();
    }
    this.certificates$ = this.service.certificates$;
    console.log("saved certificates in Storage : " + this.service.certificates$.length);
  }

  ngOnDestroy(): void {
    this.scroll.saveScrollPosition();
  }

  @HostListener('window:scroll', ['$event'])
  onScroll(): void {
    if (
      !this.loading &&
      window.innerHeight + window.scrollY >= document.body.offsetHeight - 80
    ) {
      this.service.loadMoreCertificates(this.page, this.size);
      this.cdr.detectChanges();
    }
  }
}
