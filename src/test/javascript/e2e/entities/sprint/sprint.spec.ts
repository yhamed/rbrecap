/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { SprintComponentsPage, SprintDeleteDialog, SprintUpdatePage } from './sprint.page-object';

const expect = chai.expect;

describe('Sprint e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sprintUpdatePage: SprintUpdatePage;
  let sprintComponentsPage: SprintComponentsPage;
  let sprintDeleteDialog: SprintDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Sprints', async () => {
    await navBarPage.goToEntity('sprint');
    sprintComponentsPage = new SprintComponentsPage();
    await browser.wait(ec.visibilityOf(sprintComponentsPage.title), 5000);
    expect(await sprintComponentsPage.getTitle()).to.eq('rbRecapApp.sprint.home.title');
  });

  it('should load create Sprint page', async () => {
    await sprintComponentsPage.clickOnCreateButton();
    sprintUpdatePage = new SprintUpdatePage();
    expect(await sprintUpdatePage.getPageTitle()).to.eq('rbRecapApp.sprint.home.createOrEditLabel');
    await sprintUpdatePage.cancel();
  });

  it('should create and save Sprints', async () => {
    const nbButtonsBeforeCreate = await sprintComponentsPage.countDeleteButtons();

    await sprintComponentsPage.clickOnCreateButton();
    await promise.all([sprintUpdatePage.setSprintNumberInput('5')]);
    expect(await sprintUpdatePage.getSprintNumberInput()).to.eq('5', 'Expected sprintNumber value to be equals to 5');
    const selectedActive = sprintUpdatePage.getActiveInput();
    if (await selectedActive.isSelected()) {
      await sprintUpdatePage.getActiveInput().click();
      expect(await sprintUpdatePage.getActiveInput().isSelected(), 'Expected active not to be selected').to.be.false;
    } else {
      await sprintUpdatePage.getActiveInput().click();
      expect(await sprintUpdatePage.getActiveInput().isSelected(), 'Expected active to be selected').to.be.true;
    }
    await sprintUpdatePage.save();
    expect(await sprintUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sprintComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Sprint', async () => {
    const nbButtonsBeforeDelete = await sprintComponentsPage.countDeleteButtons();
    await sprintComponentsPage.clickOnLastDeleteButton();

    sprintDeleteDialog = new SprintDeleteDialog();
    expect(await sprintDeleteDialog.getDialogTitle()).to.eq('rbRecapApp.sprint.delete.question');
    await sprintDeleteDialog.clickOnConfirmButton();

    expect(await sprintComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
