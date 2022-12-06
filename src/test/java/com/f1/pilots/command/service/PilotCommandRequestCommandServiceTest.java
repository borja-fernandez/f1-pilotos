package com.f1.pilots.command.service;

import com.f1.pilots.command.model.PilotCommandRequest;
import com.f1.pilots.command.repository.Movement;
import com.f1.pilots.command.repository.PilotCommandEntity;
import com.f1.pilots.command.repository.PilotCommandRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PilotCommandRequestCommandServiceTest {

    PilotCommandRequest pilotCommandRequest;

    @Mock
    PilotCommandRepo pilotCommandRepo;

    @InjectMocks
    private PilotCommandService pilotoService;

    @Before
    public void init() throws Exception{
        this.pilotCommandRequest = PilotCommandRequest.builder()
                        .codeA3("BOR")
                        .name("BORJA FERNANDEZ")
                        .build();

       // pilotoService = new es.f1.pilotos.command.service.Piloto();
    }

    @Test
    public void givenAnExistingPilot_WhenIsInserting_ThenThrowException(){
        when(pilotCommandRepo.findFirstByCodeA3OrderByCreationDateDesc(this.pilotCommandRequest.getCodeA3())).thenReturn(getExistingPiloto());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pilotoService.insertarPiloto(this.pilotCommandRequest);
        });

        String expectedMessage = "Pilot " + this.pilotCommandRequest.getCodeA3() + " already exists";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenANonExistingPilot_WhenIsInserting_ThenFinishSuccessfully(){
        when(pilotCommandRepo.findFirstByCodeA3OrderByCreationDateDesc(any())).thenReturn(Optional.empty());

        pilotoService.insertarPiloto(this.pilotCommandRequest);

        verify(pilotCommandRepo,times(1)).save(any());
    }

    @Test
    public void givenAnExistingPilot_WhenIsUpdating_ThenFinishSuccessfully(){
        when(pilotCommandRepo.findFirstByCodeA3OrderByCreationDateDesc(any())).thenReturn(getExistingPiloto());

        pilotoService.modificarPiloto(this.pilotCommandRequest);

        verify(pilotCommandRepo,times(1)).save(any());
    }

    @Test
    public void givenANonExistingPilot_WhenIsUpdating_ThenThrowException(){
        when(pilotCommandRepo.findFirstByCodeA3OrderByCreationDateDesc(this.pilotCommandRequest.getCodeA3())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pilotoService.modificarPiloto(this.pilotCommandRequest);
        });

        String expectedMessage = "Pilot " + this.pilotCommandRequest.getCodeA3() + " doesn't exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenAnExistingPilot_WhenIsUDeletin_ThenThrowException(){
        when(pilotCommandRepo.findFirstByCodeA3OrderByCreationDateDesc(any())).thenReturn(getExistingPiloto());

        pilotoService.eliminarPiloto("BOR");

        verify(pilotCommandRepo,times(1)).save(any());
    }

    @Test
    public void givenANonExistingPilot_WhenIsDeleting_ThenThrowException(){
        when(pilotCommandRepo.findFirstByCodeA3OrderByCreationDateDesc(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pilotoService.eliminarPiloto("BOR");
        });

        String expectedMessage = "Pilot BOR doesn't exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private Optional<PilotCommandEntity> getExistingPiloto(){
        return Optional.of(PilotCommandEntity.builder()
                .id(1)
                .type(Movement.type.ALTA_PILOTO)
                .creationDate(new Timestamp(System.currentTimeMillis()))
                .name("BORJA FERNANDEZ")
                .codeA3("BOR")
                .build());
    }
}
