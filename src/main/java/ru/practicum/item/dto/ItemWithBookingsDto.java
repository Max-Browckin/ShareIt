package ru.practicum.item.dto;

import lombok.Data;
import ru.practicum.booking.dto.BookingShortDto;
import ru.practicum.comments.dto.CommentDto;

import java.util.List;


@Data
public class ItemWithBookingsDto extends ItemDto {
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private List<CommentDto> comments;
}
