package gc.garcol.todoapp.service.impl;

import gc.garcol.todoapp.domain.Task;
import gc.garcol.todoapp.repository.TaskRepository;
import gc.garcol.todoapp.service.TaskService;
import gc.garcol.todoapp.service.dto.TaskDTO;
import gc.garcol.todoapp.service.mapper.TaskMapper;

import java.util.List;
import java.util.Optional;

import gc.garcol.todoapp.utils.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Task}.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired private TaskRepository taskRepository;

    @Autowired private TaskMapper taskMapper;

    @Autowired private JacksonUtil jacksonUtil;

    @Override
    public TaskDTO save(TaskDTO taskDTO) {
        log.debug("Request to save Task : {}", taskDTO);
        Task task = taskMapper.toEntity(taskDTO);
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public Optional<TaskDTO> partialUpdate(TaskDTO taskDTO) {
        log.debug("Request to partially update Task : {}", taskDTO);

        return taskRepository
            .findById(taskDTO.getId())
            .map(existingTask -> {
                taskMapper.partialUpdate(existingTask, taskDTO);

                return existingTask;
            })
            .map(taskRepository::save)
            .map(taskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tasks");
        return taskRepository.findAll(pageable).map(taskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskDTO> findOne(Long id) {
        log.debug("Request to get Task : {}", id);
        return taskRepository.findById(id).map(taskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Task : {}", id);
        taskRepository.deleteById(id);
    }

    @Override
    public void moveCard(Long taskId, Integer oldPosition, Integer newPosition) {
        Task task = taskRepository.getById(taskId);
        List<Long> orders = jacksonUtil.fromString(task.getCardOrder(), Long.class);
        Long removedItem = orders.get(oldPosition);
        orders.remove(oldPosition);
        orders.add(newPosition, removedItem);
        task.setCardOrder(jacksonUtil.toString(orders));
        taskRepository.save(task);
    }
}
