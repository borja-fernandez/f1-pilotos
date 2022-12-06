package com.f1.pilots.query;

import com.f1.pilots.query.model.PilotQueryResponse;
import com.f1.pilots.query.repository.PilotQueryDocument;
import com.f1.pilots.query.repository.PilotQueryRepo;
import com.f1.pilots.query.service.PilotQueryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PilotCommandRequestQueryServiceTest {

    PilotQueryDocument pilotQueryDocument;

    @Mock
    PilotQueryRepo pilotQueryRepo;

    @InjectMocks
    private PilotQueryService pilotoService;

    @Before
    public void init() throws Exception{
        this.pilotQueryDocument = PilotQueryDocument.builder()
                        .code("BOR")
                        .name("BORJA FERNANDEZ")
                        .build();

    }

    @Test
    public void givenAnNonExistingPilot_WhenIsGetting_ThenThrowException(){
        String codigoPilotoABuscar = "KUBICA";
        when(pilotQueryRepo.findByCode(codigoPilotoABuscar)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pilotoService.recuperarPiloto(codigoPilotoABuscar);
        });

        String expectedMessage = "Pilot " + codigoPilotoABuscar + " not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenANExistingPilot_WhenIsGetting_ThenReturnPilotInfo(){
        String codigoPilotoABuscar = "CODIGO";
        when(pilotQueryRepo.findByCode(codigoPilotoABuscar)).thenReturn(Optional.of(this.pilotQueryDocument));

        PilotQueryResponse pilotQueryResponse = pilotoService.recuperarPiloto(codigoPilotoABuscar);

        assertAll(
                () -> pilotQueryResponse.code.equalsIgnoreCase(this.pilotQueryDocument.code),
                () -> pilotQueryResponse.name.equalsIgnoreCase(this.pilotQueryDocument.name)
        );
        assertTrue(pilotQueryResponse.code.equalsIgnoreCase(this.pilotQueryDocument.code));
        assertTrue(pilotQueryResponse.name.equalsIgnoreCase(this.pilotQueryDocument.name));
    }

}
