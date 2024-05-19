package ru.practicum.ewm.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class Pagination {

    public PageRequest pagination(Integer from, Integer size, Sort sort) {
        return PageRequest.of(from / size, size, sort);
    }

    public PageRequest pagination(Integer from, Integer size) {
        return PageRequest.of(from / size, size);
    }

}
