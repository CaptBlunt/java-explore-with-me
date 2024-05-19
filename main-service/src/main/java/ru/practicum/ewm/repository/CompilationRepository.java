package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.entity.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    @Query(value = "select * from compilations " +
            "where pinned = ?1", nativeQuery = true)
    Page<Compilation> findByPinned(Boolean pinned, PageRequest pageRequest);
}
