package services;

import com.lanaVitor.Reservas.com.dtos.HotelDTO;
import com.lanaVitor.Reservas.com.dtos.HotelInfoDTO;
import com.lanaVitor.Reservas.com.entities.Hotel;
import com.lanaVitor.Reservas.com.repositories.HotelRepository;
import com.lanaVitor.Reservas.com.services.HotelService;
import com.lanaVitor.Reservas.com.services.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tests.Factory;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(SpringExtension.class)
public class HotelServiceTests {

    @InjectMocks
    private HotelService service;

    @Mock
    private HotelRepository repository;

    private Hotel hotel;

    private long existingId;
    private long nonExistingId;

    private HotelDTO hotelDTO;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 100L;

        hotelDTO = Factory.createHotelDTO();
        hotel = Factory.createHotel();
        hotel.addRooms(Factory.ListRooms());


        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(hotel));
        Mockito.when(repository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(repository.save(any(Hotel.class))).thenReturn(new Hotel());
    }

    @Test
    public void getInfoResortShouldReturnInformationOfResortWhenIdResortExists() {

        HotelInfoDTO expectedHotel = service.getInfoResort(existingId);

        Assertions.assertNotNull(expectedHotel);
        Assertions.assertEquals(hotel.getId(), expectedHotel.getId());
        Assertions.assertEquals(hotel.getName(), expectedHotel.getName());

    }

    @Test
    public void getInfoResortShouldResourceNotFoundExceptionWhenIdNonExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.getInfoResort(nonExistingId));

    }

    @Test
    public void InsertShouldSaveOnHotelRepository() {

        Hotel entity = repository.save(hotel);
        Assertions.assertNotNull(entity);
    }
}