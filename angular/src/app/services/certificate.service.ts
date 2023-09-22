import {Injectable, Input, OnDestroy} from '@angular/core';
import {ICriteria} from "../interfaces/ICriteria";
import {Certificate} from "../model/Certificate";
import {LocalStorageService} from "./local-storage.service";
import {FilterPipe} from "../pipe/filter.pipe";
import {Subject, Subscription, takeUntil} from "rxjs";
import {LoadService} from "./load.service";
import {User} from "../model/User";
import {IMessage} from "../interfaces/IMessage";
import {FormGroup,} from "@angular/forms";

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
      this.storage.getCertificates(), this.criteria);
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
            this.storage.saveCertificates(certificates);
            this.certificates$ = this.storage.getCertificates();
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

  findByTagName(name: string) {
    this.loadService.getCertificatesByTags(100, name)
    .pipe(takeUntil(this.unSubscribers$))
    .subscribe({
      next: (certificates: any): void => {
        if (Array.isArray(certificates)) {
          this.storage.saveCertificates(certificates);
          this.certificates$ = certificates;
          console.log('Certificates loading completed.' + name);
        }
      }
    })
  }

  sendOrders() {
    const user: User = this.storage.getUser();
    let path: IMessage = this.loadService.sendOrders(user);
    this.loadService.showMessage(`Order send by ${user.username}`)
    this.loadService.redirect(path);
  }


  async saveCertificate(form: FormGroup) {
    if (form) {
      console.log(form);
      // this.loadService.saveImage(form.get('file'));
      this.loadService.saveForm(form)
      .then((message: IMessage) => {
        console.log(message.name);
        this.loadService.redirect(message);
      })
      .catch((error) => {
        console.error(error);
      });
    } else {
      this.loadService.showMessage('Invalid form data');
    }
  }

  goBack() {
    this.loadService.back();
  }
}
