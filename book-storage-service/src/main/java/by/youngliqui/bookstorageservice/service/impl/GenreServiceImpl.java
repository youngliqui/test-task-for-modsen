package by.youngliqui.bookstorageservice.service.impl;

import by.youngliqui.bookstorageservice.dto.genre.CreateGenreDto;
import by.youngliqui.bookstorageservice.dto.genre.InfoGenreDto;
import by.youngliqui.bookstorageservice.entity.Genre;
import by.youngliqui.bookstorageservice.exception.GenreAlreadyExistsException;
import by.youngliqui.bookstorageservice.exception.GenreNotFoundException;
import by.youngliqui.bookstorageservice.mapper.GenreMapper;
import by.youngliqui.bookstorageservice.repository.GenreRepository;
import by.youngliqui.bookstorageservice.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;


    @Override
    public List<InfoGenreDto> findAllGenres() {
        return genreRepository.findAll().stream()
                .map(genreMapper::genreToInfoGenreDto)
                .toList();
    }

    @Override
    public InfoGenreDto createGenre(CreateGenreDto createGenreDto) {
        if (genreRepository.existsByName(createGenreDto.getName())) {
            throw new GenreAlreadyExistsException("Genre already exists");
        }

        Genre genre = genreMapper.createGenreDtoToGenre(createGenreDto);
        genreRepository.save(genre);

        return genreMapper.genreToInfoGenreDto(genre);
    }

    @Override
    public void deleteGenreById(Long genreId) {
        Genre deletedGenre = findGenreById(genreId);
        genreRepository.delete(deletedGenre);
    }

    private Genre findGenreById(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() ->
                        new GenreNotFoundException("Genre with id = " + genreId + " was not found"));
    }
}
