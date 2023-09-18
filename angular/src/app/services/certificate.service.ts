import {Injectable, Input, OnDestroy} from '@angular/core';
import {ICriteria} from "../interfaces/ICriteria";
import {Certificate} from "../model/Certificate";
import {LocalStorageService} from "./local-storage.service";
import {FilterPipe} from "../pipe/filter.pipe";
import {Subject, Subscription, takeUntil} from "rxjs";
import {LoadService} from "./load.service";

@Injectable({
  providedIn: 'root',
})
export class CertificateService implements OnDestroy {

  @Input() criteria: ICriteria;
  certificates$: Certificate[] = [];
  loading: boolean = false;
  subscription!: Subscription;
  unSubscribers$: Subject<any> = new Subject();

  constructor(
    private loadService: LoadService,
    private filterPipe: FilterPipe,
    private storage: LocalStorageService) {
    this.criteria = {name: '', tag: ''} as ICriteria;
  }

  ngOnDestroy(): void {
    this.unSubscribers$.next(null);
    this.unSubscribers$.complete();
  }

  public filter(): void {
    this.certificates$ = this.filterPipe.transform(
      this.storage.getCertificatesFromLocalStorage(), this.criteria);
    console.log(this.certificates$)
  }

  loadMoreCertificates(page: number, size: number): void {
    const spinner = document.getElementById('loading-indicator');
    if (!this.loading && spinner) {
      spinner.style.display = 'block';
      this.loading = true;
      const len: number = this.storage.getCertificatesSize();
      if (len !== 0) {
        page = len / 25;
      }
      this.subscription = this.loadService
      .getCertificates(page, size)
      .pipe(takeUntil(this.unSubscribers$))
      .subscribe({
        next: (certificates: any): void => {
          if (Array.isArray(certificates)) {
            this.storage.saveCertificatesToLocalStorage(certificates);
            this.certificates$ = this.storage.getCertificatesFromLocalStorage();
            this.loading = false;
          }
          console.log("Saved: " + this.certificates$.length);

          spinner.style.display = 'none';
        },
        error: (error) => {
          console.error('Error loading certificates:', error);
          this.loading = false;
        },
        complete: () => {
          console.log('Certificates loading completed.');
        },
      });
    }
  }

  goBack() {
    window.history.back();
  }

  // public updateLoginLink() { // TODO
  //   const loginLink = document
  //   .getElementById('login-link');
  //   if (loginLink) {
  //     loginLink.textContent =
  //       localStorage.getItem('userLoggedIn') ===
  //       'true' ? 'Logout' : 'Login';
  //   }
  // }
}
