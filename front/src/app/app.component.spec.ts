import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { SessionService } from './services/session.service';
import { BehaviorSubject, of } from 'rxjs';
import { AuthService } from './features/auth/services/auth.service';
import { UserService } from './services/user.service';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;

  let mockRouter = {
    navigate: jest
      .fn()
      .mockImplementation(() => new Promise<Boolean>(() => true)),
  };

  let mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
    isLogged: true,
    $isLogged: jest.fn().mockReturnValue(of(true)),
    logOut: jest.fn().mockReturnValue({}),
  };
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientModule, MatToolbarModule],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter },
        { provide: AuthService, useValue: {} },
      ],
      declarations: [AppComponent],
    }).compileComponents();
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
  });

  it('should return if user is logged', () => {
    const spySessionService = jest.spyOn(mockSessionService, '$isLogged');
    component.$isLogged();
    expect(spySessionService).toBeCalledTimes(1);
  });

  it('should logout', () => {
    const spySessionService = jest.spyOn(mockSessionService, 'logOut');
    const spyRouter = jest.spyOn(mockRouter, 'navigate');
    component.logout();
    expect(spySessionService).toBeCalledTimes(1);
    expect(spyRouter).toBeCalledWith(['']);
  });
});
