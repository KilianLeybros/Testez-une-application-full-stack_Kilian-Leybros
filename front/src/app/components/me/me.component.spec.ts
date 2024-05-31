import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import { of } from 'rxjs';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/interfaces/user.interface';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { DatePipe } from '@angular/common';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let datePipe: DatePipe;
  let user: User = {
    id: 1,
    email: 'test@test.com',
    lastName: 'Test',
    firstName: 'Test',
    admin: true,
    password: 'test',
    createdAt: new Date('2024-05-22'),
    updatedAt: new Date('2024-05-23'),
  };

  let mockUserService = {
    getById: jest.fn().mockReturnValue(of(user)),
    delete: jest.fn().mockReturnValue(of([])),
  };

  let mockRouter = {
    navigate: jest
      .fn()
      .mockImplementation(() => new Promise<Boolean>(() => true)),
  };

  const mockSnackBar = {
    open: jest.fn(),
  };

  let mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
    isLogged: true,
    logOut: jest.fn().mockImplementation(() => {}),
  };
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        RouterTestingModule,
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        DatePipe,
        MatInputModule,
        MatSnackBarModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: Router, useValue: mockRouter },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({
                id: '1',
              }),
            },
          },
        },
      ],
    }).compileComponents();
    datePipe = new DatePipe('en-EN');
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should retrive user', () => {
    component.user = undefined;
    const spyUserService = jest.spyOn(mockUserService, 'getById');
    component.ngOnInit();
    expect(spyUserService).toHaveBeenCalled();
    expect(component.user).toBeDefined();
    expect(component.user!.id).toBe(mockSessionService.sessionInformation.id);
  });

  it('should call window.history.back when clicking on back button', () => {
    const backSpy = jest.spyOn(window.history, 'back');
    const compiled = fixture.nativeElement as HTMLElement;
    const button = Array.from(compiled.querySelectorAll('mat-icon')).find(
      (el) => el.textContent === 'arrow_back'
    )!.parentElement as HTMLButtonElement;
    button.click();
    expect(backSpy).toBeCalled();
  });

  it('should delete accound', () => {
    const spyUserService = jest.spyOn(mockUserService, 'delete');
    const snackBarSpy = jest.spyOn(mockSnackBar, 'open');
    const spySessionService = jest.spyOn(mockSessionService, 'logOut');
    const spyRouter = jest.spyOn(mockRouter, 'navigate');

    component.delete();

    expect(spyUserService).toHaveBeenCalledTimes(1);
    expect(snackBarSpy).toHaveBeenCalledTimes(1);
    expect(spySessionService).toHaveBeenCalledTimes(1);
    expect(spyRouter).toHaveBeenCalledWith(['/']);
  });

  it('should display user info', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const pElement = Array.from(
      compiled.querySelectorAll('mat-card-content p')
    );
    expect(pElement[0].textContent).toBe(
      `Name: ${user.firstName} ${user.lastName.toUpperCase()}`
    );
    expect(pElement[1].textContent).toBe(`Email: ${user.email}`);
    expect(pElement[2].textContent).toBe(`You are admin`);
    expect(pElement[3].textContent).toBe(
      `Create at:  ${datePipe.transform(user.createdAt, 'longDate')}`
    );
    expect(pElement[4].textContent).toBe(
      `Last update:  ${datePipe.transform(user.updatedAt, 'longDate')}`
    );
  });

  it('should display delete button when user is not admin', () => {
    component.user!.admin = false;
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    const button = Array.from(compiled.querySelectorAll('mat-icon')).find(
      (el) => el.textContent === 'delete'
    )?.parentElement as HTMLButtonElement;
    expect(button).toBeDefined();
  });
});
