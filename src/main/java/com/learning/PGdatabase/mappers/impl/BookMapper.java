package com.learning.PGdatabase.mappers.impl;

import com.learning.PGdatabase.domain.BookEntity;
import com.learning.PGdatabase.domain.DTO.BookDto;
import com.learning.PGdatabase.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapper implements Mapper<BookDto, BookEntity> {
    private final ModelMapper modelMapper;

    public BookMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BookEntity mapTo(BookDto bookDto) {
        return modelMapper.map(bookDto, BookEntity.class);
    }

    @Override
    public BookDto mapFrom(BookEntity bookEntity) {
        return modelMapper.map(bookEntity, BookDto.class);
    }
}
