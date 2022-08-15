package com.seven.RailroadApp.services;

import com.seven.RailroadApp.models.entities.Booking;
import com.seven.RailroadApp.models.entities.User;
import com.seven.RailroadApp.models.enums.BookingStatus;
import com.seven.RailroadApp.models.records.BookingRecord;
import com.seven.RailroadApp.models.records.TicketRecord;
import com.seven.RailroadApp.repositories.BookingRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class BookingService implements com.seven.RailroadApp.services.Service {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TicketService ticketService;

    @Override
    public Set<BookingRecord> getAll() {
        Set<BookingRecord> bookingRecords = new HashSet<>(0);
        List<Booking> bookingList = bookingRepository.findAll();
        for (Booking booking : bookingList) {
            BookingRecord bookingRecord = BookingRecord.copy(booking);
            bookingRecords.add(bookingRecord);
        }
        return bookingRecords;
    }

    public Set<BookingRecord> getAllByPassenger(User passenger){
        Set<BookingRecord> bookingRecords = new HashSet<>(0);
        Iterable<Booking> bookingList = bookingRepository.findAllByPassenger(passenger.getId());
        for (Booking booking : bookingList) {
            BookingRecord bookingRecord = BookingRecord.copy(booking);
            bookingRecords.add(bookingRecord);
        }
        return bookingRecords;
    }

    @Override
    public Record get(Object id) {
        try {
            Optional<Booking> bookingReturned = bookingRepository.findById((UUID) id);
            /*If a value is present, map returns an Optional describing the result of applying
             the given mapping function to the value, otherwise returns an empty Optional.
            If the mapping function returns a null result then this method returns an empty Optional.
             */
            return bookingReturned.map(BookingRecord::copy).orElse(null);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Record create(Record recordObject) {
        try {
            BookingRecord bookingRecord = (BookingRecord) recordObject;
            Booking booking = new Booking();
            BeanUtils.copyProperties(bookingRecord, booking);

            //Set booking date
            booking.setBookingDate(LocalDateTime.now());
            //Set booking status
            booking.setStatus(BookingStatus.VALID);
            //Set passenger
            booking.setPassenger(new User());

            //Save
            bookingRepository.save(booking);

            return BookingRecord.copy(booking);
        }catch (Exception ex){return null;}
    }

    @Override
    public Boolean delete(Object id) {
        try {
            Optional<Booking> bookingReturned = bookingRepository.findById((UUID) id);
            if (bookingReturned.isPresent()) {
                Boolean deletedTicket = ticketService.deleteByBookingNo(bookingReturned.get().getBookingNo());

                if (!deletedTicket) return false;

                bookingRepository.delete(bookingReturned.get());
                return true;
            }
        } catch (Exception ex) {return false;}
        return false;
    }

    @Override
    public Record update(Record recordObject) {
        Boolean modified = false;
        try {//Retrieve indicated Booking Object from the Database
            BookingRecord propertiesToUpdate = (BookingRecord) recordObject;
            Optional<Booking> bookingReturned = bookingRepository.findByBookingNo(propertiesToUpdate.bookingNo());

            BookingRecord propertiesToReturn = null;

            if (bookingReturned.isPresent()) {
                Booking booking = bookingReturned.get();
                String status = booking.getStatus().name();

                if(status.equals("CANCELLED") && !propertiesToUpdate.status().name().equals(status)) {
                    propertiesToReturn = (BookingRecord) handleCancelled(booking);
                    modified =(propertiesToReturn != null);
                }
                else if(status.equals("USED") && !propertiesToUpdate.status().name().equals(status)) {
                    propertiesToReturn = (BookingRecord) handleUsed(booking);
                    modified =(propertiesToReturn != null);
                }
                if(modified){
                    bookingRepository.save(booking);
                    return propertiesToReturn;
                }
            }



        } catch (Exception ex) {return null;}
        return null;
    }

    private Record handleCancelled(Booking booking){
        TicketRecord tr = new TicketRecord(booking.getBookingNo(),null, BookingStatus.CANCELLED);//Cancel/delete ticket
        Record r = ticketService.update(tr);
        if(r!=null) {
            booking.setStatus(BookingStatus.CANCELLED);
            return BookingRecord.copy(booking);
        }
        else return null;
    }
    private Record handleUsed(Booking booking){
        TicketRecord tr = new TicketRecord(booking.getBookingNo(),null, BookingStatus.USED);//Cancel/delete ticket
        Record r = ticketService.update(tr);
        if(r!=null) {
            booking.setStatus(BookingStatus.USED);
            return BookingRecord.copy(booking);
        }
        else return null;
    }
}