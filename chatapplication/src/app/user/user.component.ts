import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../user.service';
import { WebsocketService } from '../websocket.service';

import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  @ViewChild('messageBody', {static: false}) private myScrollContainer: ElementRef;

  constructor(private userService: UserService,private wsservice : WebsocketService,private toastr: ToastrService) { }

  username:string="";
  activeUsers= [];
  selectedUser:any;
  message: string = '';
  messages = [];

  ngOnInit() {
    this.userService.username=this.userService.username?this.userService.username:(Math.random() + 1).toString(36).substring(7);;
    this.wsservice.userId=this.userService.username
    this.username=this.userService.username;
    this.activeUsers=this.wsservice.getAllActiveUsers()
    console.log('Logged in users:', this.wsservice.getAllActiveUsers());
    console.log('Current users:', this.username);
    this.toastr.success("Login Success!", "Success!");
    
    this.wsservice.onMessageRecievedOb().subscribe((message) => {
      this.messages.push(message);
      this.scrollToBottom();
    });
  }

  onUserClicked(user: any) {
    this.selectedUser=user;
    this.messages = this.wsservice.getMessages(this.selectedUser.sender);
    console.log('inside onuser clicked', JSON.parse(JSON.stringify(this.messages)))
  }

  onSubmit() {
    if (this.message) {
      let sendMessage ={content: this.message, to: this.selectedUser.sender,from: this.username};
      this.messages.push(sendMessage);
      this.wsservice.sendMessage(sendMessage,this.selectedUser.sender);
      this.scrollToBottom();
    }
  }

  scrollToBottom(): void {
    try {
        setTimeout(() => this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight, 10);
    } catch(err) {
      console.error(err);
    }                 
}

}
