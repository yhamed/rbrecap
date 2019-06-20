import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDroit } from 'app/shared/model/droit.model';
import { DroitService } from './droit.service';

@Component({
  selector: 'jhi-droit-delete-dialog',
  templateUrl: './droit-delete-dialog.component.html'
})
export class DroitDeleteDialogComponent {
  droit: IDroit;

  constructor(protected droitService: DroitService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.droitService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'droitListModification',
        content: 'Deleted an droit'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-droit-delete-popup',
  template: ''
})
export class DroitDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ droit }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DroitDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.droit = droit;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/droit', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/droit', { outlets: { popup: null } }]);
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
