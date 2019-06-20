/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DroitComponentsPage, DroitDeleteDialog, DroitUpdatePage } from './droit.page-object';

const expect = chai.expect;

describe('Droit e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let droitUpdatePage: DroitUpdatePage;
  let droitComponentsPage: DroitComponentsPage;
  let droitDeleteDialog: DroitDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Droits', async () => {
    await navBarPage.goToEntity('droit');
    droitComponentsPage = new DroitComponentsPage();
    await browser.wait(ec.visibilityOf(droitComponentsPage.title), 5000);
    expect(await droitComponentsPage.getTitle()).to.eq('rbRecapApp.droit.home.title');
  });

  it('should load create Droit page', async () => {
    await droitComponentsPage.clickOnCreateButton();
    droitUpdatePage = new DroitUpdatePage();
    expect(await droitUpdatePage.getPageTitle()).to.eq('rbRecapApp.droit.home.createOrEditLabel');
    await droitUpdatePage.cancel();
  });

  it('should create and save Droits', async () => {
    const nbButtonsBeforeCreate = await droitComponentsPage.countDeleteButtons();

    await droitComponentsPage.clickOnCreateButton();
    await promise.all([
      droitUpdatePage.setNameInput('name')
      // droitUpdatePage.userStoriesSelectLastOption(),
    ]);
    expect(await droitUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    const selectedIsSwitch = droitUpdatePage.getIsSwitchInput();
    if (await selectedIsSwitch.isSelected()) {
      await droitUpdatePage.getIsSwitchInput().click();
      expect(await droitUpdatePage.getIsSwitchInput().isSelected(), 'Expected isSwitch not to be selected').to.be.false;
    } else {
      await droitUpdatePage.getIsSwitchInput().click();
      expect(await droitUpdatePage.getIsSwitchInput().isSelected(), 'Expected isSwitch to be selected').to.be.true;
    }
    await droitUpdatePage.save();
    expect(await droitUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await droitComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Droit', async () => {
    const nbButtonsBeforeDelete = await droitComponentsPage.countDeleteButtons();
    await droitComponentsPage.clickOnLastDeleteButton();

    droitDeleteDialog = new DroitDeleteDialog();
    expect(await droitDeleteDialog.getDialogTitle()).to.eq('rbRecapApp.droit.delete.question');
    await droitDeleteDialog.clickOnConfirmButton();

    expect(await droitComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
