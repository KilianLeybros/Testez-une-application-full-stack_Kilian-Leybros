import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';
import { RouterTestingModule } from '@angular/router/testing';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessions: Session[] | null;
  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  const mockSessionServiceApi = {
    all: () =>
      of([
        {
          id: 1,
          name: 'testSession',
          description: 'testDescription',
          date: new Date(),
          teacher_id: 2,
          users: [],
          createdAt: new Date(),
          updatedAt: new Date(),
        },
      ]),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        RouterTestingModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionServiceApi },
      ],
    }).compileComponents();
    sessions = null;
    mockSessionService.sessionInformation.admin = true;
    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('session$ shouldnt be bull', async () => {
    const sub = component.sessions$.subscribe((s) => (sessions = s));
    sub.unsubscribe();
    expect(sessions).not.toBeNull();
  });

  it('should display list of sessions', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('mat-card.item')).not.toBeNull();
  });

  it('should show create button when user connected is admin', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(
      compiled.querySelector('button[routerLink="create"]')
    ).not.toBeNull();
  });

  it('shouldnt show create button when user connected is not admin', () => {
    mockSessionService.sessionInformation.admin = false;
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('button[routerLink="create"]')).toBeNull();
  });

  it('should show edit button when user connected is admin', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const buttonContainerLength =
      compiled.querySelector('mat-card-actions')?.children.length;
    expect(buttonContainerLength).toBeGreaterThan(1);
  });

  it('shouldnt show edit button when user connected is not admin', () => {
    mockSessionService.sessionInformation.admin = false;
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    const buttonContainerLength =
      compiled.querySelector('mat-card-actions')?.children.length;
    expect(buttonContainerLength).toBe(1);
  });
});
