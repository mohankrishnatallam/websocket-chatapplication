import { Component, OnInit, Input,EventEmitter, Output  } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  @Input() username: string;
  @Input() activeUsers: [];

  @Output() userClicked = new EventEmitter<string>();

  constructor() { }

  ngOnInit() {
    console.log(this.activeUsers);
  }

  onClick(user: string) {
    //console.log('Clicked on user:', user);
    this.userClicked.emit(user);
  }

}
