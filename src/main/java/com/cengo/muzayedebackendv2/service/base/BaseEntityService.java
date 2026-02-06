package com.cengo.muzayedebackendv2.service.base;

import com.cengo.muzayedebackendv2.domain.entity.base.BaseEntity;
import com.cengo.muzayedebackendv2.exception.ItemNotFoundException;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter(AccessLevel.PROTECTED)
public abstract class BaseEntityService<E extends BaseEntity, R extends JpaRepository<E, UUID>> {
    private final R repository;

    protected BaseEntityService(R repository) {
        this.repository = repository;
    }

    protected E save(E entity) {
        LocalDateTime now = LocalDateTime.now();

        if (entity.getId() == null) {
            entity.setCreateDate(now);
        }

        entity.setUpdateDate(now);

        entity = repository.save(entity);
        return entity;
    }

    protected List<E> saveAll(List<E> entities) {
        return entities.stream().map(this::save).toList();
    }

    protected E getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException(ErrorMessage.ITEM_NOT_FOUND));
    }

    protected Page<E> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    protected void delete(E entity) {
        repository.delete(entity);
    }

    protected void deleteAll(List<E> entities) {
        entities.forEach(this::delete);
    }

}
