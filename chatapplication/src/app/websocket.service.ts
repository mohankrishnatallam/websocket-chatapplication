import { Injectable } from '@angular/core';

import * as Stomp from 'stompjs';

import * as SockJS from 'sockjs-client';
import { UserService } from './user.service';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {

  socketClient: any = null;
  users = []
  userId: string;
  userMap = new Map();
  onMessageRecieved = new Subject<any>();

  constructor(private userService: UserService, private toastr: ToastrService) {
    let ws = new SockJS('http://localhost:8080/ws');
    this.socketClient = Stomp.over(ws);
    let that = this;
    this.onMessageNotification = this.onMessageNotification.bind(this);
    this.onMessageReceived = this.onMessageReceived.bind(this);
    this.onRecieveMessage = this.onRecieveMessage.bind(this);
    this.socketClient.connect({}, () => {
      //this.users = [];
      // new user creation JOIN
      that.socketClient.send('/app/chat.addUser', {}, JSON.stringify({ sender: this.userId, type: 'JOIN' }));
      that.socketClient.subscribe('/user/' + this.userId + '/queue/notification', this.onMessageNotification);
      that.socketClient.subscribe('/topic/public', this.onMessageReceived);// new users added notification
      that.socketClient.subscribe('/user/' + this.userId + '/queue/chat', this.onRecieveMessage);
    })
  }

  public onMessageRecievedOb() {
    return this.onMessageRecieved.asObservable();
  }

  sendMessage(payload,from:String) {
    this.socketClient.send('/app/chat.sendMessage', {}, JSON.stringify(payload));
    let messages = this.userMap.get(from);
    // console.log(messages);
    messages.push(payload);
    // console.log(messages);
  }

  onMessageReceived(payload) {
    const parsedBody = JSON.parse(payload.body);
    console.log("onNewUserLogged-In parsedBody --> "+parsedBody);
    if (this.userId === parsedBody.sender) {
      this.userMap.set(parsedBody.sender, []);
    }else{
      this.users.push(parsedBody);
      this.userMap.set(parsedBody.sender, []);
    }
  }

  onMessageNotification(payload) {    
    if (payload && payload.body) {
      const parsedBody = JSON.parse(payload.body);
      console.log("onMessageNotification parsedBody --> "+parsedBody);
      if (parsedBody[0]) {
        this.users.push(...(parsedBody.map(p => {
          this.userMap.set(p.sender, []);
          return p
        })));
      }
    }
  }

  onRecieveMessage(payload) {
    const parsedBody = JSON.parse(payload.body);
    let messages = this.userMap.get(parsedBody.from);
    console.log("onRecieveMessage messages --> "+messages);
    messages.push(parsedBody);
    this.onMessageRecieved.next(parsedBody);
    this.users.forEach(e => {
      if (e.sender === parsedBody.from) {
        let count = e.unreadMessages;
        e.unreadMessages = count + 1;
      }
    })
    //this.toastr.success('test');
  }

  getAllActiveUsers() {
    return this.users;
  }

  getMessages(name: string) {
    return [...this.userMap.get(name)];
  }
}
