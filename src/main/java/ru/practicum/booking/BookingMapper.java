package ru.practicum.booking;

import org.springframework.stereotype.Component;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingShortDto;
import ru.practicum.item.Item;
import ru.practicum.user.User;

@Component
public class BookingMapper {
    public Booking toEntity(BookingDto dto) {
        if (dto == null) return null;
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setStatus(dto.getStatus());
        return booking;
    }

    public Booking toEntity(BookingDto dto, Item item, User booker) {
        Booking booking = toEntity(dto);
        booking.setItem(item);
        booking.setBooker(booker);
        return booking;
    }

    public BookingDto toDto(Booking booking) {
        if (booking == null) return null;
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setItemId(booking.getItem().getId());
        dto.setBookerId(booking.getBooker().getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());
        return dto;
    }
    public BookingShortDto toShortDto(Booking booking) {
        BookingShortDto dto = new BookingShortDto();
        dto.setId(booking.getId());
        dto.setBookerId(booking.getBooker().getId());
        // dto.setStart(booking.getStart());
        // dto.setEnd(booking.getEnd());
        return dto;
    }

}
