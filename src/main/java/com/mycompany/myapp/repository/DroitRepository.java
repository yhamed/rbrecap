package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Droit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Droit entity.
 */
@Repository
public interface DroitRepository extends JpaRepository<Droit, Long>, JpaSpecificationExecutor<Droit> {

    @Query(value = "select distinct droit from Droit droit left join fetch droit.userStories",
        countQuery = "select count(distinct droit) from Droit droit")
    Page<Droit> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct droit from Droit droit left join fetch droit.userStories")
    List<Droit> findAllWithEagerRelationships();

    @Query("select droit from Droit droit left join fetch droit.userStories where droit.id =:id")
    Optional<Droit> findOneWithEagerRelationships(@Param("id") Long id);

}
