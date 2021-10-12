package gc.garcol.todoapp.service;

import gc.garcol.todoapp.service.dto.CardDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link gc.garcol.todoapp.domain.Card}.
 */
public interface CardService {
    /**
     * Save a card.
     *
     * @param cardDTO the entity to save.
     * @return the persisted entity.
     */
    CardDTO save(CardDTO cardDTO);

    CardDTO create(CardDTO cardDTO);

    /**
     * Partially updates a card.
     *
     * @param cardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CardDTO> partialUpdate(CardDTO cardDTO);

    /**
     * Get all the cards.
     *
     * @return the list of entities.
     */
    List<CardDTO> findAll();

    List<CardDTO> findAll(Long taskId);

    /**
     * Get the "id" card.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CardDTO> findOne(Long id);

    /**
     * Delete the "id" card.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
