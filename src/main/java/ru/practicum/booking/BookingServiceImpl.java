package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingDto create(Long userId, BookingDto bookingDto) {
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found: " + bookingDto.getItemId()));

        if (!item.getAvailable()) {
            throw new AccessDeniedException("Item is not available for booking");
        }

        if (item.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Owner cannot book their own item");
        }

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        Booking booking = bookingMapper.toEntity(bookingDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto approve(Long ownerId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found: " + bookingId));

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("Only the owner can approve the booking");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new AccessDeniedException("Booking already processed");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found: " + bookingId));

        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("User has no access to this booking");
        }

        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getAllByBooker(Long userId, String state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        List<Booking> bookings = bookingRepository.findByBookerId(userId);
        return filterAndMap(bookings, state);
    }

    @Override
    public List<BookingDto> getAllByOwner(Long userId, String state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        List<Booking> bookings = bookingRepository.findByItemOwnerId(userId);
        return filterAndMap(bookings, state);
    }

    private List<BookingDto> filterAndMap(List<Booking> bookings, String state) {
        LocalDateTime now = LocalDateTime.now();
        return bookings.stream()
                .filter(booking -> {
                    return switch (state.toUpperCase()) {
                        case "ALL" -> true;
                        case "CURRENT" -> !booking.getStart().isAfter(now) && !booking.getEnd().isBefore(now);
                        case "PAST" -> booking.getEnd().isBefore(now);
                        case "FUTURE" -> booking.getStart().isAfter(now);
                        case "WAITING" -> booking.getStatus() == BookingStatus.WAITING;
                        case "REJECTED" -> booking.getStatus() == BookingStatus.REJECTED;
                        default -> false;
                    };
                })
                .sorted((a, b) -> b.getStart().compareTo(a.getStart()))
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }
}