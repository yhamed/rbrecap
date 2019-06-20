import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserStory } from 'app/shared/model/user-story.model';
import { UserStoryService } from './user-story.service';

@Component({
  selector: 'jhi-user-story-delete-dialog',
  templateUrl: './user-story-delete-dialog.component.html'
})
export class UserStoryDeleteDialogComponent {
  userStory: IUserStory;

  constructor(protected userStoryService: UserStoryService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.userStoryService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'userStoryListModification',
        content: 'Deleted an userStory'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-user-story-delete-popup',
  template: ''
})
export class UserStoryDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ userStory }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(UserStoryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.userStory = userStory;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/user-story', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/user-story', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
