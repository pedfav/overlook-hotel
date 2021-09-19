package com.pedfav.overlookhotel.usecases;

import com.pedfav.overlookhotel.config.Properties;
import com.pedfav.overlookhotel.entities.Availability;
import com.pedfav.overlookhotel.entities.RoomAvailability;
import com.pedfav.overlookhotel.exceptions.PlaceReservationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@AllArgsConstructor
public class AvailabilityUseCase {

    private final ReservationUseCase reservationUseCase;
    private final Properties properties;

    public Availability getRoomAvailabilityByPeriod(LocalDate startDate, LocalDate endDate) {

        LocalDateTime startDateWithTime = startDate.atStartOfDay();
        LocalDateTime endDateWithTime = endDate.atTime(23, 59, 59);

        checkDates(startDateWithTime, endDateWithTime);
        boolean isAvailable = reservationUseCase.findAvailability(startDateWithTime, endDateWithTime).isEmpty();

        return Availability.builder()
                .startDate(startDateWithTime.toLocalDate())
                .endDate(endDateWithTime.toLocalDate())
                .available(isAvailable)
                .build();
    }

    public List<RoomAvailability> getRoomAvailabilityByMonth(Integer month, Integer year) {
        checkMonthAndYear(month, year);

        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, month, Month.of(month).maxLength(), 23, 59, 59);

        List<LocalDate> daysReserved = reservationUseCase.findAvailability(startDate, endDate).stream()
                .map(reservation -> reservation.getStartDate().toLocalDate()
                        .datesUntil(reservation.getEndDate().toLocalDate().plusDays(1))
                        .collect(Collectors.toList())
                ).flatMap(List::stream)
                .filter(date -> date.getMonth().getValue() == month)
                .collect(Collectors.toList());

        List<LocalDate> allDaysOfMonth = getAllDaysOfMonth(month, year);

        return Stream.concat(daysReserved.stream(), allDaysOfMonth.stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .map(date -> {
                    if (date.getValue() > 1) {
                        return RoomAvailability.builder()
                                .day(date.getKey())
                                .available(false)
                                .build();
                    } else {
                        return RoomAvailability.builder()
                                .day(date.getKey())
                                .available(true)
                                .build();
                    }
                }).sorted(Comparator.comparing(RoomAvailability::getDay))
                .collect(Collectors.toList());
    }

    private List<LocalDate> getAllDaysOfMonth(Integer month, Integer year) {
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, month, Month.of(month).maxLength(), 23, 59, 59).plusDays(1);

        return startDate.toLocalDate()
                .datesUntil(endDate.toLocalDate())
                .collect(Collectors.toList());
    }

    private void checkDates(LocalDateTime startDate, LocalDateTime endDate) {
        Duration lengthOfStay = Duration.between(startDate, endDate);

        if (startDate.isBefore(LocalDateTime.now()) && endDate.isBefore(LocalDateTime.now())) {
            throw new PlaceReservationException("Dates are in the past!");
        }

        if ((lengthOfStay.toDays() + 1) > properties.getMaxStayInDays()) {
            throw new PlaceReservationException("The period is greater than allowed!");
        }
    }

    private void checkMonthAndYear(Integer month, Integer year) {
        LocalDate now = LocalDate.now();

        if ((now.getMonth().getValue() > month) || (now.getYear() > year)) {
            throw new PlaceReservationException("Year and month must not be in the past!");
        }
    }
}
