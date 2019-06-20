import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class SprintComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-sprint div table .btn-danger'));
  title = element.all(by.css('jhi-sprint div h2#page-heading span')).first();

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

export class SprintUpdatePage {
  pageTitle = element(by.id('jhi-sprint-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  sprintNumberInput = element(by.id('field_sprintNumber'));
  activeInput = element(by.id('field_active'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setSprintNumberInput(sprintNumber) {
    await this.sprintNumberInput.sendKeys(sprintNumber);
  }

  async getSprintNumberInput() {
    return await this.sprintNumberInput.getAttribute('value');
  }

  getActiveInput(timeout?: number) {
    return this.activeInput;
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

export class SprintDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-sprint-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-sprint'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
