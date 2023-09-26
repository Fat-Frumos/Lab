import {Injectable, Input, OnDestroy} from '@angular/core';
import {ICriteria} from "../interfaces/ICriteria";
import {ICertificate} from "../model/entity/ICertificate";
import {LocalStorageService} from "./local-storage.service";
import {FilterPipe} from "../pipe/filter.pipe";
import {Subject, Subscription, takeUntil} from "rxjs";
import {LoadService} from "./load.service";
import {IUser} from "../model/entity/IUser";
import {FormGroup,} from "@angular/forms";
import {SpinnerService} from "../components/spinner/spinner.service";

@Injectable()
export class CertificateService implements OnDestroy {

  @Input() criteria: ICriteria;
  certificates$: ICertificate[];
  loading: boolean = false;
  subscription!: Subscription;
  unSubscribers$: Subject<any> = new Subject();

  constructor(
    private filterPipe: FilterPipe,
    public readonly load: LoadService,
    private storage: LocalStorageService) {
    this.criteria = {name: '', tag: ''} as ICriteria;
    this.certificates$ = this.storage.getCertificates();
  }

  ngOnDestroy(): void {
    this.unSubscribers$.next(null);
    this.unSubscribers$.complete();
  }

  public filter(): void {
    this.certificates$ = this.filterPipe.transform(
      this.storage.getCertificates(), this.criteria);
  }

  loadMoreCertificates(page: number, size: number): number {
    let p = page;
    if (!this.loading) {
      SpinnerService.toggle()
      this.loading = true;
      const len: number = this.storage.getCertificatesSize();
      if (len !== 0) {
        p = len / 25;
      }
      this.subscription = this.load
      .getCertificates(p, size)
      .pipe(takeUntil(this.unSubscribers$))
      .subscribe({
        next: (certificates: any): void => {
          if (Array.isArray(certificates)) {
            this.storage.saveCertificates(certificates);
            this.certificates$ = this.storage.getCertificates();
            this.loading = false;
          }
          console.log("Saved: " + certificates.length);
        },
        error: (error) => {
          console.error('Error loading certificates:', error);
          this.loading = false;
        },
        complete: () => {
          console.log(`Certificates loading completed. Total: ${this.certificates$.length}`);
          p++;
        },
      });
    }
    return p
  }

  findByTagName(name: string) {
    this.load.getCertificatesByTags(100, name)
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
    const user: IUser = this.storage.getUser();
    SpinnerService.toggle()
    this.load.sendOrders(user, (statusCode: number) => {
      if (statusCode === 201) {
        this.load.showByText(20103, user.username);
      } else {
        this.load.showByText(statusCode, `Failed to send orders by ${user.username}`);
      }
    });
  }

  async saveCertificate(form: FormGroup) {
    if (form.value) {
      console.log(form);
      // this.loadService.saveImage(form.get('file')); //TODO image
      this.load.saveForm(form)
      .then((code: number) => {
        this.load.showByStatus(code);
      }).catch((error) => {
        this.load.showByStatus(error.status);
      });
    } else {
      this.load.showByStatus(40002);
    }
  }

  goBack() {
    this.load.back();
  }
}
