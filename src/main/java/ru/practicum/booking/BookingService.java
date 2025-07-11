package ru.practicum.booking;

import ru.practicum.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
        BookingDto create(Long userId, BookingDto bookingDto);

        BookingDto approve(Long ownerId, Long bookingId, boolean approved);

        BookingDto getById(Long userId, Long bookingId);

        List<BookingDto> getAllByBooker(Long userId, String state);

        List<BookingDto> getAllByOwner(Long ownerId, String state);
}




