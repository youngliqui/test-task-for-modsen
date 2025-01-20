package by.youngliqui.bookstorageservice.service.impl;

import by.youngliqui.bookstorageservice.dto.genre.CreateGenreDto;
import by.youngliqui.bookstorageservice.dto.genre.InfoGenreDto;
import by.youngliqui.bookstorageservice.entity.Genre;
import by.youngliqui.bookstorageservice.exception.GenreAlreadyExistsException;
import by.youngliqui.bookstorageservice.exception.GenreNotFoundException;
import by.youngliqui.bookstorageservice.mapper.GenreMapper;
import by.youngliqui.bookstorageservice.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {

    @InjectMocks
    private GenreServiceImpl genreService;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreMapper genreMapper;

    private Genre genre;

    @BeforeEach
    void setUp() {
        genre = Genre.builder()
                .id(1L)
                .name("genre_name")
                .build();
    }

    @Test
    void testFindAllGenres() {
        // Given
        InfoGenreDto infoGenreDto = InfoGenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();

        List<Genre> expectedGenreList = Collections.singletonList(genre);
        List<InfoGenreDto> expectedInfoGenreDtoList = Collections.singletonList(infoGenreDto);

        // When
        when(genreRepository.findAll()).thenReturn(expectedGenreList);
        when(genreMapper.genreToInfoGenreDto(genre)).thenReturn(infoGenreDto);

        List<InfoGenreDto> actualResult = genreService.findAllGenres();

        // Then
        assertThat(actualResult)
                .isNotNull()
                .isEqualTo(expectedInfoGenreDtoList);

        verify(genreRepository).findAll();
    }

    @Test
    void testCreateGenre_Success() {
        // Given
        CreateGenreDto createGenreDto = CreateGenreDto.builder()
                .name(genre.getName())
                .build();

        // When
        when(genreRepository.existsByName(createGenreDto.getName())).thenReturn(false);
        when(genreMapper.createGenreDtoToGenre(createGenreDto)).thenReturn(genre);
        when(genreMapper.genreToInfoGenreDto(genre)).thenReturn(new InfoGenreDto());

        InfoGenreDto actualResult = genreService.createGenre(createGenreDto);

        // Then
        assertThat(actualResult).isNotNull();

        verify(genreRepository).existsByName(createGenreDto.getName());
        verify(genreRepository).save(genre);
    }

    @Test
    void testCreateGenre_AlreadyExists() {
        // Given
        CreateGenreDto createGenreDto = CreateGenreDto.builder()
                .name("already_exists_name")
                .build();

        // When
        when(genreRepository.existsByName(createGenreDto.getName())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> genreService.createGenre(createGenreDto))
                .isInstanceOf(GenreAlreadyExistsException.class)
                .hasMessageContaining("Genre already exists");

        verify(genreRepository).existsByName(createGenreDto.getName());
        verify(genreRepository, times(0)).save(any(Genre.class));
    }

    @Test
    void testDeleteGenreById_Success() {
        // Given
        Long deletedId = genre.getId();

        // When
        when(genreRepository.findById(deletedId)).thenReturn(Optional.of(genre));

        genreService.deleteGenreById(deletedId);

        // Then
        verify(genreRepository).findById(deletedId);
        verify(genreRepository).delete(any(Genre.class));
    }

    @Test
    void testDeleteGenreById_NotFound() {
        // Given
        Long notFoundId = 100L;

        // When
        when(genreRepository.findById(notFoundId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> genreService.deleteGenreById(notFoundId))
                .isInstanceOf(GenreNotFoundException.class)
                .hasMessageContaining("was not found");

        verify(genreRepository).findById(notFoundId);
        verify(genreRepository, times(0)).delete(any(Genre.class));
    }
}