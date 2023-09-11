import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent {
  // public host = "http://localhost:8080/api";
  public host = "https://gift-store.onrender.com/api";
  public certificatesList = document.getElementById('certificates-list');
  public username = localStorage.getItem("user");
}
