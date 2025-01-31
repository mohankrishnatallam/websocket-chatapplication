import { Component } from '@angular/core';
import { LoginComponent } from './login/login.component';
import { WebsocketService } from './websocket.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'chatapplication';
  // constructor(private wsservice : WebsocketService ) {
  //   //this.openConnection()
  // }

  // openConnection() {
  //   //this.wsservice.initSocket();
  // }

  // sendMessage(){
  //   this.wsservice.sendmessage();
  // }
}
