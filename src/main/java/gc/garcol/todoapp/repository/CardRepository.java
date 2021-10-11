package gc.garcol.todoapp.repository;

import gc.garcol.todoapp.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Card entity.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("select card from Card as card where card.task.id = :taskId")
    List<Card> findAllByTaskId(@Param("taskId") Long taskId);

}
