describe('Me spec', () => {
  it('Should display user info', () => {
    cy.loginAdmin();

    const mockUser = {
      id: 1,
      email: 'test@test.com',
      lastName: 'TestLastName',
      firstName: 'TestFirstName',
      admin: true,
      password: 'test',
      createdAt: '2024-01-01',
      updatedAt: '2024-01-01',
    };

    cy.intercept('GET', '/api/user/1', { body: mockUser });

    cy.get('span')
      .contains('Account')
      .first()
      .then((elem) => {
        cy.wrap(elem).click();
      });

    cy.get('p')
      .contains(
        `Name: ${mockUser.firstName} ${mockUser.lastName.toUpperCase()}`
      )
      .should('have.length', 1);

    cy.get('p').contains(`Email: ${mockUser.email}`).should('have.length', 1);

    cy.get('p').contains('You are admin').should('have.length', 1);

    cy.url().should('include', '/me');
  });

  it('Should delete account', () => {
    cy.intercept('DELETE', '/api/user/1', {
      body: {},
    });

    cy.login();

    const mockUser = {
      id: 1,
      email: 'test@test.com',
      lastName: 'TestLastName',
      firstName: 'TestFirstName',
      admin: false,
      password: 'test',
      createdAt: '2024-01-01',
      updatedAt: '2024-01-01',
    };

    cy.intercept('GET', '/api/user/1', { body: mockUser });

    cy.get('span')
      .contains('Account')
      .first()
      .then((elem) => {
        cy.wrap(elem).click();
      });

    cy.url().should('include', '/me');

    const button = cy.get('span').contains('Delete');

    button.click().then(() => {
      cy.url().should('include', '/');
      cy.get('span')
        .contains('Your account has been deleted !')
        .should('have.length', 1);
    });
  });
});
