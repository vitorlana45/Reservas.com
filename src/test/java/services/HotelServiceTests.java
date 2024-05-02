package services;

import com.lanaVitor.Reservas.com.dtos.HotelDTO;
import com.lanaVitor.Reservas.com.dtos.HotelInfoDTO;
import com.lanaVitor.Reservas.com.dtos.ReserveRoomsRequestDTO;
import com.lanaVitor.Reservas.com.entities.Hotel;
import com.lanaVitor.Reservas.com.entities.Rooms;
import com.lanaVitor.Reservas.com.entities.User;
import com.lanaVitor.Reservas.com.repositories.HotelRepository;
import com.lanaVitor.Reservas.com.repositories.RoomsRepository;
import com.lanaVitor.Reservas.com.repositories.UserRepository;
import com.lanaVitor.Reservas.com.services.EmailService;
import com.lanaVitor.Reservas.com.services.HotelService;
import com.lanaVitor.Reservas.com.services.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tests.Factory;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


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

    private ReserveRoomsRequestDTO notFoundReservationDTO;


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
        notFoundReservationDTO = Factory.emailNotFoundReservationDTO();
        hotelEmptyRooms = Factory.createRoomsOccupied();
        rooms = Factory.createRoom();
        hotelDTO = Factory.createHotelDTO();
        hotel = Factory.createHotel();
        hotel.addRooms(Factory.createRoom());
        listHotels = Factory.createHotelList();
        requestDTO = Factory.reservationDTO();
        existUser = "email@email.com";
        createUser = Factory.createUser();

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(hotel));
        Mockito.when(repository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(repository.findAll()).thenReturn(List.of(hotel));
        Mockito.when(repository.save(any(Hotel.class))).thenReturn(hotel);

        Mockito.when(userRepository.findByEmail(existUser)).thenReturn(createUser);
        Mockito.when(roomsRepository.save(any(Rooms.class))).thenReturn(rooms);
        Mockito.when(roomsRepository.findById(existingRoom)).thenReturn(Optional.of(rooms));
        Mockito.when(userRepository.findUserByEmail(existUser)).thenReturn(createUser);
    }

    @Test
    @DisplayName("reserve rooms should return EntityNotFoundException when user non exists")
    public void reserveRoomShouldReturnEntityNotFoundExceptionWhenUserNonExists() {


        Mockito.doThrow(EntityNotFoundException.class).when(userRepository).findUserByEmail(createUser.getEmail());

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.reserveRoom(notFoundReservationDTO, existingId, requestDTO));
    }

    @Test
    @DisplayName("Reserve room should book the room when it becomes available")
    public void reserveRoomShouldBookTheRoomWhenItBecomesAvailable() {

        var entity = service.reserveRoom(requestDTO, existingId, requestDTO);
        var user = userRepository.findByEmail(existUser);
        var hotel = repository.findById(existingId);

        var roomList = roomsRepository.save(entity.getRooms());

        Assertions.assertNotNull(entity);
        Assertions.assertNotNull(roomList);
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(hotel);
    }

    @Test
    @DisplayName("reserve room should return ResourceNotFoundException when rooms list is empty")
    public void reserveRoomShouldChangeStatusWhenRoomsListIsEmpty() {

        Mockito.when(roomsRepository.save(emptyRoom)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(repository.findById(hotelEntityOccupied)).thenReturn(Optional.of(hotelEmptyRooms));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.reserveRoom(requestDTO, hotelEntityOccupied, requestDTO));
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

        Optional<Hotel> hotelEntity = repository.findById(existingId);

        // Act
        HotelDTO actualRooms = service.searchAllRooms(existingId);

        // Assert
        Assertions.assertNotNull(actualRooms);
        Assertions.assertTrue(hotelEntity.isPresent());

    }

    @Test
    @DisplayName("searchAvailableRooms should return ResourceNotFoundException.class when id non exists")
    public void searchAvailableRoomsShouldReturnResourceNotFoundExceptionWhenIdNonExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.searchAllRooms(nonExistingId));
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

        Mockito.when(repository.findAll()).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> repository.findAll());


    }


}
