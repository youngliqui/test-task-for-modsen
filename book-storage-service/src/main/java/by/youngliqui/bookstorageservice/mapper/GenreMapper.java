package by.youngliqui.bookstorageservice.mapper;

import by.youngliqui.bookstorageservice.dto.genre.CreateGenreDto;
import by.youngliqui.bookstorageservice.dto.genre.InfoGenreDto;
import by.youngliqui.bookstorageservice.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    InfoGenreDto genreToInfoGenreDto(Genre genre);

    @Mapping(target = "id", ignore = true)
    Genre createGenreDtoToGenre(CreateGenreDto createGenreDto);
}
