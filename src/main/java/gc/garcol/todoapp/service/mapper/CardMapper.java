package gc.garcol.todoapp.service.mapper;

import gc.garcol.todoapp.domain.*;
import gc.garcol.todoapp.service.dto.CardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Card} and its DTO {@link CardDTO}.
 */
@Mapper(componentModel = "spring", uses = { TaskMapper.class })
public interface CardMapper extends EntityMapper<CardDTO, Card> {
    @Mapping(target = "taskId", source = "task.id")
    CardDTO toDto(Card s);
}
