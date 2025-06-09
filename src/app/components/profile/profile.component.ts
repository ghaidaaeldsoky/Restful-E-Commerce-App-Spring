import { Component } from '@angular/core';
import { ProfileService } from '../../services/profile.service';

@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  selectedTab = 'account';
  firstName: string = '';
  email: string = '';
  phone: string = '';
  isEditMode: boolean = false;
  showUpdateMessage: boolean = false;

  constructor (private profileService: ProfileService) {}

  ngOnInit() {
    this.getPersonalInfo();
  }

  getPersonalInfo(): void {
    this.profileService.getPersonalInfo1().subscribe({
      next: (res) => {
        console.log("ressssssssssss: " + res);
        console.log("fname: " + res.firstName);
        console.log("email: " + res.email);
        console.log("phone: " + res.phone);

        this.firstName = res.firstName;
        this.email = res.email;
        this.phone = res.phone;
      },
      error: (err) => {
        console.error('Error fetching personal info:', err);
      }
    });
  }

   enableEdit(): void {
    this.isEditMode = true;
  }

  saveChanges(): void {
    const updatedInfo = {
      firstName: this.firstName,
      email: this.email,
      phone: this.phone
    };

    this.profileService.updatePersonalInfo(updatedInfo).subscribe({
      next: () => {
        this.isEditMode = false;
        this.showUpdateMessage = true;
  
        setTimeout(() => {
          this.showUpdateMessage = false;
        }, 3000);
      },
      error: (err) => {
        console.error('Error updating personal info:', err);
      }
    });
  }

}
