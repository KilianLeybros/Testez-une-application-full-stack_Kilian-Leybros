import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { BehaviorSubject } from 'rxjs';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should login', () => {
    const sessionInformation: SessionInformation = {
      token: 'token',
      type: 'bearer',
      id: 1,
      username: 'Test',
      firstName: 'TestFirtName',
      lastName: 'TestLastName',
      admin: true,
    };
    service.logIn(sessionInformation);
    expect(service.sessionInformation).toBe(sessionInformation);
    expect(service.isLogged).toBeTruthy();
  });

  it('should logout', () => {
    service.logOut();
    expect(service.sessionInformation).toBeUndefined();
    expect(service.isLogged).toBeFalsy();
  });

  it('should return logging status as observable', async () => {
    const mockBehaviorSubject = new BehaviorSubject<boolean>(false);
    const $logged = service.$isLogged();
    expect($logged).toEqual(mockBehaviorSubject.asObservable());
  });
});
