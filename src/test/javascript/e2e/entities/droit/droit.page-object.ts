import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class DroitComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-droit div table .btn-danger'));
  title = element.all(by.css('jhi-droit div h2#page-heading span')).first();

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

export class DroitUpdatePage {
  pageTitle = element(by.id('jhi-droit-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  nameInput = element(by.id('field_name'));
  isSwitchInput = element(by.id('field_isSwitch'));
  userStoriesSelect = element(by.id('field_userStories'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return await this.nameInput.getAttribute('value');
  }

  getIsSwitchInput(timeout?: number) {
    return this.isSwitchInput;
  }

  async userStoriesSelectLastOption(timeout?: number) {
    await this.userStoriesSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async userStoriesSelectOption(option) {
    await this.userStoriesSelect.sendKeys(option);
  }

  getUserStoriesSelect(): ElementFinder {
    return this.userStoriesSelect;
  }

  async getUserStoriesSelectedOption() {
    return await this.userStoriesSelect.element(by.css('option:checked')).getText();
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

export class DroitDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-droit-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-droit'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
