import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class UserStoryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-user-story div table .btn-danger'));
  title = element.all(by.css('jhi-user-story div h2#page-heading span')).first();

  async clickOnCreateButton(timeout?: number) {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(timeout?: number) {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class UserStoryUpdatePage {
  pageTitle = element(by.id('jhi-user-story-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  nameInput = element(by.id('field_name'));
  descriptionInput = element(by.id('field_description'));
  chiffrageInput = element(by.id('field_chiffrage'));
  sprintSelect = element(by.id('field_sprint'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return await this.nameInput.getAttribute('value');
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return await this.descriptionInput.getAttribute('value');
  }

  async setChiffrageInput(chiffrage) {
    await this.chiffrageInput.sendKeys(chiffrage);
  }

  async getChiffrageInput() {
    return await this.chiffrageInput.getAttribute('value');
  }

  async sprintSelectLastOption(timeout?: number) {
    await this.sprintSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async sprintSelectOption(option) {
    await this.sprintSelect.sendKeys(option);
  }

  getSprintSelect(): ElementFinder {
    return this.sprintSelect;
  }

  async getSprintSelectedOption() {
    return await this.sprintSelect.element(by.css('option:checked')).getText();
  }

  async save(timeout?: number) {
    await this.saveButton.click();
  }

  async cancel(timeout?: number) {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class UserStoryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-userStory-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-userStory'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
