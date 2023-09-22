import {Inject, Injectable} from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpHeaders, HttpResponse
} from '@angular/common/http';
import {catchError, EMPTY, filter, map, Observable} from 'rxjs';
import {BASE_URL_TOKEN} from "../config";

@Injectable()
export class LoadInterceptor implements HttpInterceptor {

  constructor(
    @Inject(BASE_URL_TOKEN) private baseUrl: string
  ) {
  }

  public intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    let headers: HttpHeaders = request.headers
    .append('Content-Type', 'application/json')
    .append('Authorization', `Bearer `);

    const jsonReq = request.clone({
      url: `${this.baseUrl}${request.url}`,
      headers
    });
    return next.handle(jsonReq).pipe(
      filter(this.isHttpResponse),
      map((response: HttpResponse<any>) => {
        console.log(response);
        return response.clone({body: response.body?.data})
      }),
      catchError(() => {
        return EMPTY;
      }));
  }

  private isHttpResponse(event: HttpEvent<any>): event is HttpResponse<any> {
    return event instanceof HttpResponse;
  }
}
