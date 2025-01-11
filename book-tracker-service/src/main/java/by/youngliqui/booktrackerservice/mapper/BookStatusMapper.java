package by.youngliqui.booktrackerservice.mapper;

import by.youngliqui.booktrackerservice.dto.AvailableBookStatusDto;
import by.youngliqui.booktrackerservice.dto.BorrowedBookStatusDto;
import by.youngliqui.booktrackerservice.dto.CreateBookStatusDto;
import by.youngliqui.booktrackerservice.dto.InfoBookStatusDto;
import by.youngliqui.booktrackerservice.entity.BookStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookStatusMapper {

    InfoBookStatusDto bookStatusToInfoBookStatusDto(BookStatus bookStatus);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "borrowedAt", ignore = true)
    @Mapping(target = "returnBy", ignore = true)
    @Mapping(target = "status", constant = "AVAILABLE")
    BookStatus createBookStatusDtoToBookStatus(CreateBookStatusDto createBookStatusDto);

    AvailableBookStatusDto bookStatusToAvailableBookStatusDto(BookStatus bookStatus);

    BorrowedBookStatusDto bookStatusToBorrowedBookStatusDto(BookStatus bookStatus);

}
