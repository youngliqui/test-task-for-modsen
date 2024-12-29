package by.youngliqui.bookstorageservice.mapper;

import by.youngliqui.bookstorageservice.dto.genre.CreateGenreDto;
import by.youngliqui.bookstorageservice.dto.genre.InfoGenreDto;
import by.youngliqui.bookstorageservice.entity.Genre;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    InfoGenreDto genreToInfoGenreDto(Genre genre);

    Genre createGenreDtoToGenre(CreateGenreDto createGenreDto);
}
