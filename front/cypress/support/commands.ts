/// <reference types="cypress" />
declare namespace Cypress {
  interface Chainable<Subject = any> {
    login(): Chainable<any>;
    loginAdmin(): Chainable<any>;
  }
}

Cypress.Commands.add('loginAdmin', () => {
  cy.intercept('POST', '/api/auth/login', { fixture: 'session-admin.json' });
  cy.visit('/login');
  cy.get('input[formControlName=email]').type('yoga@studio.com');
  cy.get('input[formControlName=password]').type(
    `${'test!1234'}{enter}{enter}`
  );
});

Cypress.Commands.add('login', () => {
  cy.intercept('POST', '/api/auth/login', { fixture: 'session-noadmin.json' });
  cy.visit('/login');
  cy.get('input[formControlName=email]').type('yoga@studio.com');
  cy.get('input[formControlName=password]').type(
    `${'test!1234'}{enter}{enter}`
  );
});
