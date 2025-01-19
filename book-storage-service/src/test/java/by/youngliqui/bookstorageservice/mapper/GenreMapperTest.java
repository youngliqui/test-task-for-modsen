package by.youngliqui.bookstorageservice.mapper;

import by.youngliqui.bookstorageservice.dto.genre.CreateGenreDto;
import by.youngliqui.bookstorageservice.dto.genre.InfoGenreDto;
import by.youngliqui.bookstorageservice.entity.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenreMapperTest {

    private GenreMapper genreMapper;

    private Genre genre;

    @BeforeEach
    void setUp() {
        genreMapper = new GenreMapperImpl();
        genre = Genre.builder()
                .id(1L)
                .name("genre_name")
                .build();
    }

    @Test
    void testGenreToInfoGenreDto() {
        InfoGenreDto actualResult = genreMapper.genreToInfoGenreDto(genre);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getId()).isEqualTo(genre.getId());
        assertThat(actualResult.getName()).isEqualTo(genre.getName());
    }

    @Test
    void testCreateGenreDtoToGenre() {
        CreateGenreDto createGenreDto = CreateGenreDto.builder()
                .name("genre_new_name")
                .build();

        Genre actualResult = genreMapper.createGenreDtoToGenre(createGenreDto);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getName()).isEqualTo(createGenreDto.getName());
        assertThat(actualResult.getId()).isNull();
    }
}