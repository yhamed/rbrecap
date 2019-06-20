import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISprint } from 'app/shared/model/sprint.model';
import { SprintService } from './sprint.service';

@Component({
  selector: 'jhi-sprint-delete-dialog',
  templateUrl: './sprint-delete-dialog.component.html'
})
export class SprintDeleteDialogComponent {
  sprint: ISprint;

  constructor(protected sprintService: SprintService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.sprintService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'sprintListModification',
        content: 'Deleted an sprint'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-sprint-delete-popup',
  template: ''
})
export class SprintDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ sprint }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SprintDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.sprint = sprint;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/sprint', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/sprint', { outlets: { popup: null } }]);
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
