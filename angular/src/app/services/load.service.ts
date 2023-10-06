import {Inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {
  catchError,
  from,
  map,
  mergeMap,
  Observable,
  tap,
  throwError
} from 'rxjs';
import {ICertificate} from '../model/entity/ICertificate';
import {ITag} from "../model/entity/ITag";
import {ICategory} from "../interfaces/ICategory";
import {BASE_URL_TOKEN, SRC_URL_TOKEN} from "../config";
import {AuthService} from "./auth.service";
import {IUser} from "../model/entity/IUser";
import {IMessage} from "../interfaces/IMessage";
import {FormArray, FormGroup} from "@angular/forms";
import {SpinnerService} from "../components/spinner/spinner.service";
import {LoginState} from "../model/enum/LoginState";
import {ModalService} from "../components/modal/modal.service";
import {NavigateService} from "./navigate.service";
import {StatusCode} from "../model/enum/HttpStatusCode";
import {IOrder} from "../interfaces/IOrder";
import {IInvoice} from "../interfaces/IInvoice";

@Injectable({
  providedIn: 'root',
})
export class LoadService {

  constructor(
    @Inject(SRC_URL_TOKEN) private srcUrl: string,
    @Inject(BASE_URL_TOKEN) private baseUrl: string,
    private readonly navigator: NavigateService,
    private readonly modalService: ModalService,
    private readonly authService: AuthService,
    private readonly http: HttpClient) {
  }

  showByText(code: number, text: string) {
    const message: IMessage = StatusCode.getMessageForStatus(code, text);
    this.showMessage(message);
  }

  showByStatus(code: number) {
    const message: IMessage = StatusCode.getMessageForStatus(code, '');
    this.showMessage(message);
  }

  showMessage(message: IMessage) {
    this.modalService.showMessage(message);
    const delay: number = 1500;
    setTimeout(() => {
      this.navigator.redirect(message.href);  //TODO
    }, delay);
  }

  back(): void {
    this.navigator.back();
  }

  loginState(state: LoginState) {
    this.authService.loginState.next(state);
  }

  public sendOrders(total: number, user: IUser, callback: (statusCode: number) => void): void {
    const ids: string[] = user.certificates.map((certificate) => certificate.id);
    const counters: number[] = user.certificates.map((certificate) => certificate.count);
    const url = `${this.baseUrl}/orders/${user.username}?certificateIds=${ids.join(",")}&counters=${counters.join(",")}`;
    const invoice: IInvoice = {counters: counters, ids: ids, totalPrice: total};
    const requestBody = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${user.access_token}`,
      }
    };
    fetch(url, requestBody)
    .then((response) => {
      callback(response.status);
      let certificates = user.certificates;
      certificates.forEach(cert => {
        cert.count = 1;
        cert.checkout = false
      });
      this.authService.saveCertificates(certificates)
      user.invoices.push(invoice);
      this.authService.saveUser(user);
    })
    .catch((error) => {
      callback(error.status);
    });
  }

  async saveForm(form: FormGroup): Promise<number> {
    console.log(form.value)
    const accessToken = this.authService.getUser().access_token;

    const expirationDate = new Date(form.get('expired')?.value);
    const currentDate = new Date();
    const duration = Math.ceil(Math.abs(expirationDate.getTime() - currentDate.getTime()) / (1000 * 60 * 60 * 24));

    const selectedTags = [];
    const tagsControl = <FormArray>form.get('tags');
    for (let tagControl of tagsControl.controls) {
      if (tagControl.value.selected) {
        selectedTags.push({name: tagControl.value.name});
      }
    }
    let path;
    const fileControl = form.get('file');
    if (fileControl && fileControl.value) {
      const imageName = fileControl.value.split(/(\\|\/)/g).pop();
      path = imageName ? `${this.baseUrl}/upload/${imageName}` : '';
      console.log(path)
    }


    const requestBody = {
      "name": form.value.name,
      "description": form.value.description,
      "price": form.value.price,
      "duration": duration,
      "tags": selectedTags,
      "path": path
    };


    console.log(requestBody)

    const response = await fetch(`${this.baseUrl}/certificates`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestBody)
    });

    console.log(response.status);

    if (response.ok) {
      const jsonData = await response.json();
      console.log(jsonData)
      localStorage.setItem('product', JSON.stringify(jsonData));
    } else {
      const message = await response.text();
      throw new Error(`HTTP error! status: ${response.status}, message: ${message}`);
    }
    return response.status;
  }


  saveImage(form: any) {
    const fileControl = form.get('file');
    if (fileControl) {
      const imageData = new FormData();
      imageData.append('file', fileControl);
      this.http
      .post(`${this.baseUrl}/upload`, imageData)
      .pipe(
        catchError((error) => {
          return throwError(() => error.toString());
        })
      )
      .subscribe();
    }
  }

  getCertificates(page: number): Observable<ICertificate[]> {
    SpinnerService.toggle();
    return this.http
    .get<any>(`/certificates?page=${page.toFixed()}`)
    .pipe(map((data: any) => data._embedded.certificateDtoList
      .map(this.mapper))
    );
  }

  getCertificatesByTags(size: number, name: string): Observable<ICertificate[]> {
    return this.http
    .get(`/certificates/search?tagNames=${name}&size=${size}`)
    .pipe(map((data: any) => data._embedded.certificateDtoList
      .map(this.mapper))
    );
  }

  getTags(): Observable<string[]> {
    return this.http.get<any>('/tags?size=25')
    .pipe(map((data: any) => data._embedded.tagDtoList
    .map(this.tagMapper)));
  }

  loginUser(user: IUser): Observable<any> {
    return this.http.post(`/login`, user).pipe(
      tap((response: any): void => {
        user.id = response.id;
        user.access_token = response.access_token;
        user.refresh_token = response.refresh_token;
        user.expired_at = response.expired_at;
        user.certificates = [];
        user.state = LoginState.LOGGED_IN;
        this.authService.setUser(user);
      }),
      catchError(error => {
        return throwError(() => {
          this.showByStatus(error.status);
        });
      })
    );
  }

  signup(formData: any): Observable<any> {
    console.log(formData.toString())
    return from(import('../components/item/item.component')).pipe(
      mergeMap(() => {
        return this.http.post(`/signup`, formData).pipe(
          tap((response: any) =>
            this.showByText(20101, `${response.username} ${formData.firstName}`)
          )
        );
      })
    );
  }

  private mapper(data: any): ICertificate {
    console.log(data)
    return {
      id: data.id,
      name: data.name,
      description: data.description,
      shortDescription: data.shortDescription,
      company: data.company,
      price: data.price,
      duration: data.duration,
      createDate: new Date(data.createDate),
      lastUpdate: new Date(data.lastUpdateDate),
      favorite: false,
      checkout: false,
      path: data.path,
      tags: data.tags.map((tag: any): ITag => ({id: tag.id, name: tag.name})),
      count: 1
    };
  }

  private tagMapper = (data: any): ICategory => {
    return {
      name: data.name,
      tag: data.name,
      url: `${this.srcUrl}/200x150/?${data.name}`,
    };
  }

  getOrders(): Observable<IOrder[]> {
    const user = this.authService.getUser();
    const url = `${this.baseUrl}/orders/users/${user.id}`;
    const headers = new Headers({
      Authorization: `Bearer ${user.access_token}`,
      'Content-Type': 'application/json',
    });
    return from(
      fetch(url, {method: 'GET', headers: headers}).then(response => {
        if (!response.ok) {
          throw new Error(`Request failed with status: ${response.status}`);
        }
        return response.json();
      })
    ).pipe(
      map(data => data._embedded.orderDtoList as IOrder[])
    );
  }
}
