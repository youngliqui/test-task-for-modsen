package by.youngliqui.bookstorageservice.mapper;

import by.youngliqui.bookstorageservice.dto.book.CreateBookDto;
import by.youngliqui.bookstorageservice.dto.book.InfoBookDto;
import by.youngliqui.bookstorageservice.dto.book.PatchBookDto;
import by.youngliqui.bookstorageservice.dto.book.UpdateBookDto;
import by.youngliqui.bookstorageservice.entity.Book;
import by.youngliqui.bookstorageservice.entity.Genre;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "genre.name", target = "genreName")
    InfoBookDto bookToInfoBookDto(Book book);

    @Mapping(source = "genre", target = "genre")
    @Mapping(target = "id", ignore = true)
    Book createBookDtoToBook(CreateBookDto createBookDto, Genre genre);

    @Mapping(source = "genre", target = "genre")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateBookFromDto(@MappingTarget Book book, UpdateBookDto updateBookDto, Genre genre);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    @Mapping(source = "genre", target = "genre")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void patchBookFromDto(@MappingTarget Book book, PatchBookDto patchBookDto, Genre genre);
}
