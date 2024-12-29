package by.youngliqui.bookstorageservice.controller;

import by.youngliqui.bookstorageservice.controller.doc.GenreControllerDoc;
import by.youngliqui.bookstorageservice.dto.genre.CreateGenreDto;
import by.youngliqui.bookstorageservice.dto.genre.InfoGenreDto;
import by.youngliqui.bookstorageservice.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books/genres")
@RequiredArgsConstructor
public class GenreController implements GenreControllerDoc {

    private final GenreService genreService;

    @Override
    public List<InfoGenreDto> getAllGenres() {
        return genreService.findAllGenres();
    }

    @Override
    public InfoGenreDto createGenre(CreateGenreDto createGenreDto) {
        return genreService.createGenre(createGenreDto);
    }

    @Override
    public void deleteGenreById(Long genreId) {
        genreService.deleteGenreById(genreId);
    }
}
