/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { UserStoryComponentsPage, UserStoryDeleteDialog, UserStoryUpdatePage } from './user-story.page-object';

const expect = chai.expect;

describe('UserStory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let userStoryUpdatePage: UserStoryUpdatePage;
  let userStoryComponentsPage: UserStoryComponentsPage;
  let userStoryDeleteDialog: UserStoryDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load UserStories', async () => {
    await navBarPage.goToEntity('user-story');
    userStoryComponentsPage = new UserStoryComponentsPage();
    await browser.wait(ec.visibilityOf(userStoryComponentsPage.title), 5000);
    expect(await userStoryComponentsPage.getTitle()).to.eq('rbRecapApp.userStory.home.title');
  });

  it('should load create UserStory page', async () => {
    await userStoryComponentsPage.clickOnCreateButton();
    userStoryUpdatePage = new UserStoryUpdatePage();
    expect(await userStoryUpdatePage.getPageTitle()).to.eq('rbRecapApp.userStory.home.createOrEditLabel');
    await userStoryUpdatePage.cancel();
  });

  it('should create and save UserStories', async () => {
    const nbButtonsBeforeCreate = await userStoryComponentsPage.countDeleteButtons();

    await userStoryComponentsPage.clickOnCreateButton();
    await promise.all([
      userStoryUpdatePage.setNameInput('name'),
      userStoryUpdatePage.setDescriptionInput('description'),
      userStoryUpdatePage.setChiffrageInput('5'),
      userStoryUpdatePage.sprintSelectLastOption()
    ]);
    expect(await userStoryUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await userStoryUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');
    expect(await userStoryUpdatePage.getChiffrageInput()).to.eq('5', 'Expected chiffrage value to be equals to 5');
    await userStoryUpdatePage.save();
    expect(await userStoryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await userStoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last UserStory', async () => {
    const nbButtonsBeforeDelete = await userStoryComponentsPage.countDeleteButtons();
    await userStoryComponentsPage.clickOnLastDeleteButton();

    userStoryDeleteDialog = new UserStoryDeleteDialog();
    expect(await userStoryDeleteDialog.getDialogTitle()).to.eq('rbRecapApp.userStory.delete.question');
    await userStoryDeleteDialog.clickOnConfirmButton();

    expect(await userStoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
