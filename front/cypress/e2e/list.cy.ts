describe('List spec', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/session', { fixture: 'sessions.json' }).as(
      'sessions'
    );
  });
  it('Should display sessions list with length of 2', () => {
    cy.loginAdmin();
    cy.wait('@sessions');

    cy.get('mat-card')
      .first()
      .children('div')
      .children('mat-card')
      .should('have.length', 2);
  });
});
