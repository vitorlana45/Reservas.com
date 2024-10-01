package com.lanaVitor.Reservas.com.services;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanaVitor.Reservas.com.entities.Hotel;
import com.lanaVitor.Reservas.com.entities.Rooms;
import com.lanaVitor.Reservas.com.entities.User;
import com.lanaVitor.Reservas.com.services.util.DateFormatter;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


@Service
public class WebhookService {

    @Value("${endpoint-stripe}")
    private String endpointSecret;


    private static final Logger logger = (Logger) LoggerFactory.getLogger(WebhookService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserService userService;
    private final HotelService hotelService;

    @Autowired
    public WebhookService(UserService userService, HotelService hotelService) {
        this.userService = userService;
        this.hotelService = hotelService;
    }

    @Transactional
    public void creatingEventAndCheckCase(String payload, String sigHeader) {
        Event event = new Event();
        try {
            event = Webhook.constructEvent(payload, sigHeader, this.endpointSecret);

            JsonNode jsonNode = objectMapper.readTree(payload);

            String email = jsonNode.path("data").path("object").path("customer_details").path("email").asText();
            String checkIn = jsonNode.path("data").path("object").path("metadata").path("check_in").asText();
            String checkOut = jsonNode.path("data").path("object").path("metadata").path("check_out").asText();

            LocalDateTime checkinDateTime = DateFormatter.formatDateString(checkIn);
            LocalDateTime checkOutDateTime = DateFormatter.formatDateString(checkOut);

            switch (event.getType()) {
                case "checkout.session.completed":
                    saveUserInRoom(email, checkinDateTime, checkOutDateTime);
                    return;
                case "checkout.session.async_payment_succeeded":
                    logger.info("Async payment succeeded.");
                    break;
                case "checkout.session.async_payment_failed":
                    logger.info("Async payment failed.");
                    break;
                case "checkout.session.expired":
                    logger.info("Checkout session expired.");
                    break;
                case "payment_intent.succeeded":
                    logger.info("Payment intent succeeded.");
                    break;
                default:
                    logger.warn("Unrecognized event type: {}", event.getType());
            }

        } catch (SignatureVerificationException e) {
            logger.error("Signature verification failed", e);
            throw new RuntimeException("Invalid signature");
        } catch (Exception e) {
            logger.error("Error processing webhook", e);
            throw new RuntimeException("Invalid payload");
        }
    }

    public void saveUserInRoom(String userEmail, LocalDateTime checkIn, LocalDateTime checkOut) {
        User user = userService.findUserByEmail(userEmail);
        Hotel hotel = hotelService.searchHotelById(1L);

        for (Rooms room : hotel.getListRooms()) {
            if (!room.isRented()) {
                room.setCheckIn(checkIn);
                room.setCheckOut(checkOut);
                room.setRented(true);
                room.setUser(user);
                user.getRooms().add(room);
                break;
            }
        }

        if (!hotel.getUserList().contains(user)) {
            hotel.getUserList().add(user);
        }

        hotelService.saveHotel(hotel);
    }
}
