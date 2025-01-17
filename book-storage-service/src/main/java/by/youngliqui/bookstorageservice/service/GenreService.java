package by.youngliqui.bookstorageservice.service;

import by.youngliqui.bookstorageservice.dto.genre.CreateGenreDto;
import by.youngliqui.bookstorageservice.dto.genre.InfoGenreDto;

import java.util.List;

public interface GenreService {

    List<InfoGenreDto> findAllGenres();

    InfoGenreDto createGenre(CreateGenreDto createGenreDto);

    void deleteGenreById(Long genreId);

}
