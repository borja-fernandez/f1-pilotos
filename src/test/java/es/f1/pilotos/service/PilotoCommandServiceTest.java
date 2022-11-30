package es.f1.pilotos.service;

import es.f1.pilotos.command.model.Piloto;
import es.f1.pilotos.command.repository.PilotoCommandRepo;
import es.f1.pilotos.command.repository.TipoRegistro;
import es.f1.pilotos.command.service.PilotoCommandService;
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
public class PilotoCommandServiceTest {

    Piloto piloto;

    @Mock
    PilotoCommandRepo pilotoCommandRepo;

    @InjectMocks
    private PilotoCommandService pilotoService;

    @Before
    public void init() throws Exception{
        this.piloto = Piloto.builder()
                        .codigo("BOR")
                        .nombre("BORJA FERNANDEZ")
                        .build();

       // pilotoService = new es.f1.pilotos.command.service.Piloto();
    }

    @Test
    public void givenAnExistingPilot_WhenIsInserting_ThenThrowException(){
        when(pilotoCommandRepo.findFirstByCodigoOrderByFechaCreacionDesc(any())).thenReturn(getExistingPiloto());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pilotoService.insertarPiloto(this.piloto);
        });

        String expectedMessage = "Ya existe un piloto";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenANonExistingPilot_WhenIsInserting_ThenFinishSuccessfully(){
        when(pilotoCommandRepo.findFirstByCodigoOrderByFechaCreacionDesc(any())).thenReturn(Optional.empty());

        pilotoService.insertarPiloto(this.piloto);

        verify(pilotoCommandRepo,times(1)).save(any());
    }

    @Test
    public void givenAnExistingPilot_WhenIsUpdating_ThenFinishSuccessfully(){
        when(pilotoCommandRepo.findFirstByCodigoOrderByFechaCreacionDesc(any())).thenReturn(getExistingPiloto());

        pilotoService.modificarPiloto(this.piloto);

        verify(pilotoCommandRepo,times(1)).save(any());
    }

    @Test
    public void givenANonExistingPilot_WhenIsUpdating_ThenThrowException(){
        when(pilotoCommandRepo.findFirstByCodigoOrderByFechaCreacionDesc(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pilotoService.modificarPiloto(this.piloto);
        });

        String expectedMessage = "Piloto inexistente";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenAnExistingPilot_WhenIsUDeletin_ThenThrowException(){
        when(pilotoCommandRepo.findFirstByCodigoOrderByFechaCreacionDesc(any())).thenReturn(getExistingPiloto());

        pilotoService.eliminarPiloto("BOR");

        verify(pilotoCommandRepo,times(1)).save(any());
    }

    @Test
    public void givenANonExistingPilot_WhenIsDeleting_ThenThrowException(){
        when(pilotoCommandRepo.findFirstByCodigoOrderByFechaCreacionDesc(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pilotoService.eliminarPiloto("BOR");
        });

        String expectedMessage = "Piloto inexistente";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private Optional<es.f1.pilotos.command.repository.Piloto> getExistingPiloto(){
        return Optional.of(es.f1.pilotos.command.repository.Piloto.builder()
                .id(1)
                .tipoRegistro(TipoRegistro.tipoRegistro.ALTA)
                .fechaCreacion(new Timestamp(System.currentTimeMillis()))
                .nombre("BORJA FERNANDEZ")
                .codigo("BOR")
                .build());
    }
}
