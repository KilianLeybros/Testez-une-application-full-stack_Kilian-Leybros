describe('Register spec', () => {
  it('Register successfull', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {
      body: {
        email: 'yoga@studio.com',
        firstName: 'Yoga name',
        lastName: 'Yoga lastname',
        password: 'test!1234',
      },
    });

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=firstName]').type('yoga@studio.com');
    cy.get('input[formControlName=lastName]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}`);

    cy.url().should('include', '/login');
  });

  it('Cant register', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register');

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=firstName]').type('yoga@studio.com');
    cy.get('input[formControlName=lastName]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}`);

    cy.get('[class*="error"]').should('have.text', 'An error occurred');
    cy.url().should('include', '/register');
  });
});
