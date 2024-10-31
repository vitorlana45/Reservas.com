package services;

import com.lanaVitor.Reservas.com.dtos.HotelDTO;
import com.lanaVitor.Reservas.com.dtos.HotelInfoDTO;
import com.lanaVitor.Reservas.com.dtos.ReservationHotelDTO;
import com.lanaVitor.Reservas.com.dtos.ReserveRoomsRequestDTO;
import com.lanaVitor.Reservas.com.entities.Hotel;
import com.lanaVitor.Reservas.com.entities.Rooms;
import com.lanaVitor.Reservas.com.entities.User;
import com.lanaVitor.Reservas.com.repositories.HotelRepository;
import com.lanaVitor.Reservas.com.repositories.RoomsRepository;
import com.lanaVitor.Reservas.com.repositories.UserRepository;
import com.lanaVitor.Reservas.com.services.EmailService;
import com.lanaVitor.Reservas.com.services.HotelService;
import com.lanaVitor.Reservas.com.services.exception.InvalidReservationDateException;
import com.lanaVitor.Reservas.com.services.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tests.Factory;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static tests.Factory.reservationWithOutUserDTO;


@ExtendWith(SpringExtension.class)
public class HotelServiceTests {

    @InjectMocks
    private HotelService service;

    @Mock
    private HotelRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomsRepository roomsRepository;

    @Mock
    private EmailService emailService;

    private Hotel hotel;

    private long existingId;
    private long nonExistingId;

    private String emailNotFoundOnDataBase;

    private ReserveRoomsRequestDTO nonExistsUser;


    private long hotelEntityOccupied;

    private Rooms emptyRoom;

    private long existingRoom;
    private List<Hotel> listHotels;

    private Hotel hotelEmptyRooms;
    private HotelDTO hotelDTO;

    private Rooms rooms;

    private List<Rooms> listRooms;

    private ReserveRoomsRequestDTO requestDTO;

    private String existUser;

    private User createUser;


    @BeforeEach
    void setUp() {

        existingId = 1L;
        existingRoom = 2L;
        hotelEntityOccupied = 5L;
        nonExistingId = 100L;
        emailNotFoundOnDataBase = Factory.emailNotFoundReservationDTO().getUser().getEmail();
        nonExistsUser = Factory.emailNotFoundReservationDTO();
        hotelEmptyRooms = Factory.createRoomsOccupied();
        rooms = Factory.createRoom();
        hotelDTO = Factory.createHotelDTO();
        hotel = Factory.createHotel();
        hotel.addRooms(Factory.createRoom());
        listHotels = Factory.createHotelList();
        requestDTO = Factory.reservationDTO();
        existUser = "email@email.com";
        createUser = Factory.createUser();

        when(repository.findById(existingId)).thenReturn(Optional.of(hotel));
        when(repository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        when(repository.findAll()).thenReturn(List.of(hotel));
        when(repository.save(any(Hotel.class))).thenReturn(hotel);

        when(userRepository.findByEmail(existUser)).thenReturn(createUser);
        when(roomsRepository.save(any(Rooms.class))).thenReturn(rooms);
        when(roomsRepository.findById(existingRoom)).thenReturn(Optional.of(rooms));
        when(userRepository.findUserByEmail(existUser)).thenReturn(createUser);
    }

    @Test
    @DisplayName("reserve rooms should return EntityNotFoundException when user non exists")
    public void reserveRoomShouldReturnEntityNotFoundExceptionWhenUserNonExists() {


        ReserveRoomsRequestDTO reservationDTONonExistUser = reservationWithOutUserDTO();

        Mockito.doThrow(EntityNotFoundException.class).when(userRepository).findUserByEmail(reservationDTONonExistUser.getUser().getEmail());

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.reserveRoom(requestDTO, hotelDTO.getId(), reservationDTONonExistUser));
    }

    @Test
    @DisplayName("Reserve room should book the room when it becomes available")
    public void reserveRoomShouldBookTheRoomWhenItBecomesAvailable() {

        var userDTO = requestDTO.getUser().getEmail();
        Mockito.when(userRepository.findUserByEmail(userDTO)).thenReturn(createUser);


        service.reserveRoom(requestDTO, existingId, requestDTO);
        userRepository.findByEmail(existUser);
        repository.findById(existingId);
    }

    @Test
    @DisplayName("reserve room should return ResourceNotFoundException when rooms list is empty")
    public void reserveRoomShouldChangeStatusWhenRoomsListIsEmpty() {

        when(roomsRepository.save(emptyRoom)).thenThrow(ResourceNotFoundException.class);
        when(repository.findById(hotelEntityOccupied)).thenReturn(Optional.of(hotelEmptyRooms));


        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.reserveRoom(requestDTO, nonExistingId, requestDTO));
    }

    @Test
    @DisplayName("getInfoResort should return information of resort when Id exists")
    public void getInfoResortShouldReturnInformationOfResortWhenIdResortExists() {

        HotelInfoDTO expectedHotel = service.getInfoResort(existingId);

        Assertions.assertNotNull(expectedHotel);
        Assertions.assertEquals(hotel.getId(), expectedHotel.getId());
        Assertions.assertEquals(hotel.getName(), expectedHotel.getName());

    }

    @Test
    @DisplayName("getInfoResort should ResourceNotFoundException when Id non exists")
    public void getInfoResortShouldResourceNotFoundExceptionWhenIdNonExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.getInfoResort(nonExistingId));

    }

    @Test
    @DisplayName("available Rooms should return room list when resort id exists ")
    public void availableRoomsShouldReturnRoomListWhenResortIdExists() {
        // Arrange

        PageRequest pageRequest = PageRequest.of(0, 10);
        Optional<Hotel> hotelEntity = repository.findById(existingId);
        when(repository.findById(existingId)).thenReturn(Optional.of(hotel));

        // Act
        Page<HotelDTO> result = service.searchAllRooms(existingId, pageRequest);

        // Assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertTrue(hotelEntity.isPresent());

    }

    @Test
    @DisplayName("searchAvailableRooms should return ResourceNotFoundException.class when id non exists")
    public void searchAvailableRoomsShouldReturnResourceNotFoundExceptionWhenIdNonExists() {

        PageRequest pageRequest = PageRequest.of(0, 10);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.searchAllRooms(nonExistingId, pageRequest));
    }

    @Test
    @DisplayName("getHotelDto should return list hotels")
    public void getHotelDtoShouldReturnListOfHotels() {

        var result = service.getHotelDTOS();
        List<Hotel> list = repository.findAll();

        Assertions.assertNotNull(list);
        Assertions.assertFalse(list.isEmpty());
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());

    }

    @Test
    @DisplayName("getHotelDto should return ResourceNOtFoundException when hotel entity is Empty ")
    public void getHotelDtoShouldReturnResourceNotFoundExceptionWhenHotelEntityIsEmpty() {

        when(repository.findAll()).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> repository.findAll());


    }


}
