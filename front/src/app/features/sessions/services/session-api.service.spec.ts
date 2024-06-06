import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { Session } from '../interfaces/session.interface';
import { Observable } from 'rxjs';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(SessionApiService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  it('should get all sessions', () => {
    const mockData: Session[] = [
      {
        id: 1,
        name: 'Session1',
        description: 'Description1',
        date: new Date(),
        teacher_id: 2,
        users: [1, 2],
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      {
        id: 2,
        name: 'Session2',
        description: 'Description2',
        date: new Date(),
        teacher_id: 1,
        users: [2, 3],
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    ];

    service.all().subscribe((sessions: Session[]) => {
      expect(sessions).toBe(mockData);
    });

    const req = httpTesting.expectOne('api/session');

    expect(req.request.method).toBe('GET');

    req.flush(mockData);

    httpTesting.verify();
  });

  it('should get session detail', () => {
    const mockSession: Session = {
      id: 1,
      name: 'Session1',
      description: 'Description1',
      date: new Date(),
      teacher_id: 2,
      users: [1, 2],
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    service.detail(mockSession.id!.toString()).subscribe((session: Session) => {
      expect(session).toBe(mockSession);
    });

    const req = httpTesting.expectOne(`api/session/${mockSession.id}`);

    expect(req.request.method).toBe('GET');

    req.flush(mockSession);

    httpTesting.verify();
  });

  it('should delete session', () => {
    const sessionId = '1';

    service.delete(sessionId).subscribe((data: void) => {
      expect(data).toBe({});
    });

    const req = httpTesting.expectOne(`api/session/${sessionId}`);

    expect(req.request.method).toBe('DELETE');

    req.flush(new Observable<void>());

    httpTesting.verify();
  });

  it('should create session', () => {
    const mockSession: Session = {
      id: 1,
      name: 'Session1',
      description: 'Description1',
      date: new Date(),
      teacher_id: 2,
      users: [1, 2],
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    service.create(mockSession).subscribe((createdSession: Session) => {
      expect(createdSession).toBe(mockSession);
    });

    const req = httpTesting.expectOne('api/session');

    expect(req.request.method).toBe('POST');

    req.flush(mockSession);

    httpTesting.verify();
  });

  it('should update session', () => {
    const mockSession: Session = {
      id: 1,
      name: 'SessionModified',
      description: 'DescriptionModified',
      date: new Date(),
      teacher_id: 1,
      users: [2, 1],
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    service
      .update(mockSession.id!.toString(), {
        name: 'SessionModified',
        description: 'DescriptionModified',
        date: new Date(),
        teacher_id: 1,
        users: [2, 1],
        createdAt: new Date(),
        updatedAt: new Date(),
      })
      .subscribe((updatedSession: Session) => {
        expect(updatedSession).toBe(mockSession);
      });

    const req = httpTesting.expectOne(`api/session/${mockSession.id}`);

    expect(req.request.method).toBe('PUT');

    req.flush(mockSession);

    httpTesting.verify();
  });

  it('should participate to session', () => {
    const mockSessionId = '1';
    const mockUserId = '2';

    service.participate(mockSessionId, mockUserId).subscribe((data: void) => {
      expect(data).toBe({});
    });

    const req = httpTesting.expectOne(
      `api/session/${mockSessionId}/participate/${mockUserId}`
    );

    expect(req.request.method).toBe('POST');

    req.flush(new Observable<void>());

    httpTesting.verify();
  });

  it('should unparticipate to session', () => {
    const mockSessionId = '1';
    const mockUserId = '2';

    service.unParticipate(mockSessionId, mockUserId).subscribe((data: void) => {
      expect(data).toBe({});
    });

    const req = httpTesting.expectOne(
      `api/session/${mockSessionId}/participate/${mockUserId}`
    );

    expect(req.request.method).toBe('DELETE');

    req.flush(new Observable<void>());

    httpTesting.verify();
  });
});
