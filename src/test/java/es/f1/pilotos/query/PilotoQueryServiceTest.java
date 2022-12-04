package es.f1.pilotos.query;

import es.f1.pilotos.query.model.PilotoResponse;
import es.f1.pilotos.query.repository.Piloto;
import es.f1.pilotos.query.repository.PilotoQueryRepo;
import es.f1.pilotos.query.service.PilotoQueryService;
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
public class PilotoQueryServiceTest {

    Piloto piloto;

    @Mock
    PilotoQueryRepo pilotoQueryRepo;

    @InjectMocks
    private PilotoQueryService pilotoService;

    @Before
    public void init() throws Exception{
        this.piloto = Piloto.builder()
                        .codigo("BOR")
                        .nombre("BORJA FERNANDEZ")
                        .build();

    }

    @Test
    public void givenAnNonExistingPilot_WhenIsGetting_ThenThrowException(){
        String codigoPilotoABuscar = "CODIGO";
        when(pilotoQueryRepo.findByCodigo(codigoPilotoABuscar)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pilotoService.recuperarPiloto(codigoPilotoABuscar);
        });

        String expectedMessage = "Piloto " + codigoPilotoABuscar + " no encontrado";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenANExistingPilot_WhenIsGetting_ThenReturnPilotInfo(){
        String codigoPilotoABuscar = "CODIGO";
        when(pilotoQueryRepo.findByCodigo(codigoPilotoABuscar)).thenReturn(Optional.of(this.piloto));

        PilotoResponse pilotoResponse = pilotoService.recuperarPiloto(codigoPilotoABuscar);

        assertAll(
                () -> pilotoResponse.codigo.equalsIgnoreCase(this.piloto.codigo),
                () -> pilotoResponse.nombre.equalsIgnoreCase(this.piloto.nombre)
        );
        assertTrue(pilotoResponse.codigo.equalsIgnoreCase(this.piloto.codigo));
        assertTrue(pilotoResponse.nombre.equalsIgnoreCase(this.piloto.nombre));
    }

}
