describe('Detail spec', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/session', { fixture: 'sessions.json' }).as(
      'sessions'
    );

    cy.intercept('GET', '/api/teacher/1', { fixture: 'teacher.json' }).as(
      'teacherDetail'
    );
  });

  it('Delete button declared and Delete successfull', () => {
    cy.loginAdmin();
    cy.wait('@sessions');

    cy.intercept('GET', '/api/session/1', { fixture: 'session.json' }).as(
      'sessionDetail'
    );

    cy.get('span')
      .contains('Detail')
      .first()
      .then((elem) => {
        cy.wrap(elem).click();
      });

    cy.intercept('DELETE', '/api/session/1', {
      body: {},
    });

    const button = cy.get('button[color=warn]');

    button.should('have.length', 1);

    button.click();

    cy.url().should('include', '/sessions');
  });

  it('Participate button declared and Participate successfull', () => {
    cy.login();
    cy.wait('@sessions');

    cy.intercept('GET', '/api/session/1', { fixture: 'session.json' }).as(
      'sessionDetail'
    );

    cy.get('span')
      .contains('Detail')
      .first()
      .then((elem) => {
        cy.wrap(elem).click();
      });
    cy.intercept('POST', '/api/session/1/participate/1', {
      body: {},
    });

    const button = cy.get('span').contains('Participate');

    button.should('have.length', 1);

    button.then((elem) => {
      cy.wrap(elem).click();
    });

    cy.get('@sessionDetail.all').should('have.length', 2);
  });

  it('Unparticipate button declared and Unparticipate successfull', () => {
    cy.login();
    cy.wait('@sessions');

    cy.intercept('GET', '/api/session/1', {
      fixture: 'session-alreadyparticipate.json',
    }).as('sessionDetail');

    cy.get('span')
      .contains('Detail')
      .first()
      .then((elem) => {
        cy.wrap(elem).click();
      });
    cy.intercept('DELETE', '/api/session/1/participate/1', {
      body: {},
    });

    const button = cy.get('span').contains('Do not participate');

    button.should('have.length', 1);

    button.then((elem) => {
      cy.wrap(elem).click();
    });

    cy.get('@sessionDetail.all').should('have.length', 2);
  });
});
