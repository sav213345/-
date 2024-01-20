package com.example.texlog;


import java.util.Arrays;
import java.util.List;
import main.Controller.ConferenceController;
import main.Models.Conference;
import main.Repository.ConferenceRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class TexlogApplicationTests {


//    @InjectMocks
//    private ConferenceController yourController;
//
//    @Mock
//    private ConferenceRepository conferenceRepo;
//
//    public TexlogApplicationTests() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void testSearchConferenceAllParamsEmpty() {
//        // Arrange
//        when(conferenceRepo.findAll()).thenReturn(Arrays.asList(new Conference(), new Conference()));
//
//        // Act
//        ResponseEntity responseEntity = yourController.searchConference("", "");
//
//        // Assert
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        List<Conference> resultConferences = (List<Conference>) responseEntity.getBody();
//        assertEquals(2, resultConferences.size());
//        // Add more assertions as needed based on your actual data and business logic
//    }




}
