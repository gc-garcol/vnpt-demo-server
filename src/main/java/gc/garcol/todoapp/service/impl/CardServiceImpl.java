package gc.garcol.todoapp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import gc.garcol.todoapp.domain.Card;
import gc.garcol.todoapp.domain.Task;
import gc.garcol.todoapp.repository.CardRepository;
import gc.garcol.todoapp.repository.TaskRepository;
import gc.garcol.todoapp.service.CardService;
import gc.garcol.todoapp.service.dto.CardDTO;
import gc.garcol.todoapp.service.mapper.CardMapper;
import gc.garcol.todoapp.utils.JacksonUtil;
import org.hibernate.type.IntegerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Card}.
 */
@Service
@Transactional
public class CardServiceImpl implements CardService {

    private static final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private JacksonUtil jacksonUtil;

    @Override
    public CardDTO save(CardDTO cardDTO) {
        log.debug("Request to save Card : {}", cardDTO);
        Card card = cardMapper.toEntity(cardDTO);
        Card storedCard = cardRepository.getById(cardDTO.getId());
        card.setTask(storedCard.getTask());
        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }

    @Override
    public CardDTO create(CardDTO cardDTO) {
        log.debug("Request to create Card : {}", cardDTO);
        Card card = cardMapper.toEntity(cardDTO);
//        Task task = new Task();
//        task.setId(cardDTO.getTaskId());
        Task task = taskRepository.getById(cardDTO.getTaskId());
        List<Long> orders = jacksonUtil.fromString(task.getCardOrder(), Long.class);
        card.setTask(task);
        card = cardRepository.save(card);
        orders.add(card.getId());
        String newOrders = jacksonUtil.toString(orders);
        task.setCardOrder(newOrders);
        taskRepository.save(task);
        return cardMapper.toDto(card);
    }

    @Override
    public Optional<CardDTO> partialUpdate(CardDTO cardDTO) {
        log.debug("Request to partially update Card : {}", cardDTO);

        return cardRepository
            .findById(cardDTO.getId())
            .map(existingCard -> {
                cardMapper.partialUpdate(existingCard, cardDTO);

                return existingCard;
            })
            .map(cardRepository::save)
            .map(cardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> findAll() {
        log.debug("Request to get all Cards");
        return cardRepository.findAll().stream().map(cardMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> findAll(Long taskId) {
        log.debug("Request to get all Cards");
        return cardRepository.findAllByTaskId(taskId).stream().map(cardMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardDTO> findOne(Long id) {
        log.debug("Request to get Card : {}", id);
        return cardRepository.findById(id).map(cardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Card : {}", id);
        Card card = cardRepository.getById(id);
        Task task = card.getTask();
        List<Long> orders = jacksonUtil.fromString(task.getCardOrder(), Long.class);
        orders.remove(id);
        String newOrders = jacksonUtil.toString(orders);
        task.setCardOrder(newOrders);
        taskRepository.save(task);

        cardRepository.deleteById(id);
    }
}
