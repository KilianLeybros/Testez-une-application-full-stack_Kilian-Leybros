describe('Form spec', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/teacher', { fixture: 'teachers.json' }).as(
      'teachers'
    );
    cy.intercept('GET', '/api/session', { fixture: 'sessions.json' }).as(
      'sessions'
    );
  });

  it('Should create session', () => {
    cy.loginAdmin();
    cy.wait('@sessions');

    cy.wait(1500);
    cy.get('span')
      .contains('Create')
      .then((elem) => {
        cy.wrap(elem).click();
      });
    cy.wait('@teachers');

    cy.url().should('include', '/sessions/create');

    cy.get('input[formControlName=name]').type('test');

    cy.get('input[formControlName=date]').type('2024-01-01');

    cy.get('mat-select[formControlName=teacher_id]')
      .click()
      .then(() =>
        cy
          .get('mat-option')
          .contains('TeacherFirstName TeacherLastName')
          .click()
      );

    cy.get('textarea[formControlName=description]').type('description');

    cy.intercept('POST', '/api/session', {
      body: {
        id: 1,
        name: 'test',
        description: 'description',
        date: '2024-01-01',
        teacher_id: 1,
        users: [],
        createdAt: '2024-01-01',
        updatedAt: '2024-01-01',
      },
    });

    cy.get('button[type=submit]')
      .click()
      .then(() => {
        cy.url().should('include', '/sessions');
        cy.get('span').contains('Session created !').should('have.length', 1);
      });
  });

  it('Should update session', () => {
    cy.loginAdmin();
    cy.wait('@sessions');

    cy.intercept('GET', '/api/session/1', { fixture: 'session.json' });

    cy.wait(1500);
    cy.get('span')
      .contains('Edit')
      .first()
      .then((elem) => {
        cy.wrap(elem).click();
      });
    cy.wait('@teachers');

    cy.url().should('include', '/sessions/update');

    cy.get('input[formControlName=name]').type('test update');

    cy.intercept('PUT', '/api/session/1', {
      body: {
        id: 1,
        name: 'test update',
        description: 'description',
        date: '2024-01-01',
        teacher_id: 1,
        users: [],
        createdAt: '2024-01-01',
        updatedAt: '2024-01-01',
      },
    });

    cy.get('button[type=submit]')
      .click()
      .then(() => {
        cy.url().should('include', '/sessions');
        cy.get('span').contains('Session updated !').should('have.length', 1);
      });
  });
});
