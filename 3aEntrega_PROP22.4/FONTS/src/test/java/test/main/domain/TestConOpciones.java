package test.main.domain;

import main.domain.ConOpciones;
import main.domain.Opcion;
import main.domain.exceptions.TDatosIncorrecto;
import main.domain.types.TDatos;
import main.domain.types.TDatosOpciones;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(value= MockitoJUnitRunner.class)
public class TestConOpciones {
    @Mock
    private Opcion opcion1;
    @Mock
    private Opcion opcion2;
    @Mock
    private Opcion opcion3;
    @Mock
    private TDatosOpciones datosOpciones;
    @Mock
    private TDatos datos;

    @Test
    public void testCreadora() {
        ConOpciones conOpciones1;
        ConOpciones conOpciones2;
        try {
            conOpciones1 = new ConOpciones(1);
            assertEquals(1, conOpciones1.getMaxSelect());
            assertTrue(conOpciones1.getOpciones().isEmpty());
            assertEquals(1, conOpciones1.getIdNextOpcion());
        } catch (Exception e) {
            fail("No deberia establecer la excepcion");
        }
        try {
            conOpciones2 = new ConOpciones(0);
            fail("Deberia de saltar la excepcion");
        } catch (Exception e) {
            if (!(e instanceof IllegalArgumentException)) {
                fail("Ha saltado una excepcion diferente");
            }
        }
    }

    @Test
    public void testSetNumRespuestas() {
        try {
            ConOpciones conOpciones1 = new ConOpciones(1);
            conOpciones1.setMaxSelect(2);
            assertEquals(2, conOpciones1.getMaxSelect());
        } catch (Exception e) {
            fail("No deberia establecer la excepcion");
        }

        try {
            ConOpciones conOpciones1 = new ConOpciones(1);
            conOpciones1.setMaxSelect(0);
            fail("No ha saltado la excepcion");
        } catch (Exception e) {
            if (!(e instanceof IllegalArgumentException)) {
                fail("Ha saltado una excepcion diferente");
            }
        }
    }

    @Test
    public void testAddOpcion() {
        when(opcion1.getId()).thenReturn(1);
        when(opcion2.getId()).thenReturn(2);
        when(opcion3.getId()).thenReturn(3);
        try {
            ConOpciones conOpciones1 = new ConOpciones(1);
            assertFalse(conOpciones1.addOpcion(opcion3));
            assertTrue(conOpciones1.addOpcion(opcion1));
            assertEquals(2, conOpciones1.getIdNextOpcion());
            assertTrue(conOpciones1.addOpcion(opcion2));
            assertEquals(3, conOpciones1.getIdNextOpcion());

            assertTrue(conOpciones1.getOpciones().contains(opcion1));
            assertTrue(conOpciones1.getOpciones().contains(opcion2));
        } catch (Exception e) {
            fail("No deberia establecer la excepcion");
        }
    }

    @Test
    public void testRemoveOpcion() {
        when(opcion1.getId()).thenReturn(1);
        when(opcion2.getId()).thenReturn(2);
        when(opcion3.getId()).thenReturn(3);
        try {
            ConOpciones conOpciones1 = new ConOpciones(1);
            conOpciones1.addOpcion(opcion1);
            conOpciones1.addOpcion(opcion2);
            conOpciones1.addOpcion(opcion3);
            conOpciones1.removeOpcion(opcion2);
            assertFalse(conOpciones1.getOpciones().contains(opcion2));
            conOpciones1.removeOpcion(opcion3);
            assertFalse(conOpciones1.getOpciones().contains(opcion3));


        } catch (Exception e) {
            fail("No deberia establecer la excepcion");
        }
    }

    @Test
    public void testIsValid() {
        when(opcion1.getId()).thenReturn(1);
        when(opcion2.getId()).thenReturn(2);

        try {
            // Caso correcto datosOpciones vacio
            ConOpciones conOpciones1 = new ConOpciones(1);
            conOpciones1.addOpcion(opcion1);
            when(datosOpciones.getNumOpciones()).thenReturn(1);
            when(datosOpciones.getIdOpciones()).thenReturn(new ArrayList<>());
            assertTrue(conOpciones1.isValid(datosOpciones, false));

            // numOpciones != numRespuestas
            when(datosOpciones.getNumOpciones()).thenReturn(2);
            assertFalse(conOpciones1.isValid(datosOpciones, false));

            when(datosOpciones.getNumOpciones()).thenReturn(1);

            // Caso diferente tipo TDatos
            assertFalse(conOpciones1.isValid(datos, false));

            // Caso idx correcto
            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(1);

            when(datosOpciones.getIdOpciones()).thenReturn(ids);

            conOpciones1.addOpcion(opcion1);
            assertTrue(conOpciones1.isValid(datosOpciones, false));

            // Caso con mas de 1 opcion
            when(datosOpciones.getNumOpciones()).thenReturn(2);
            ids.add(2);
            conOpciones1.addOpcion(opcion2);
            conOpciones1.setMaxSelect(2);
            assertTrue(conOpciones1.isValid(datosOpciones, false));

            // Caso incorrecto mas de 1 opcion
            ids.removeLast();
            ids.add(3);
            assertFalse(conOpciones1.isValid(datosOpciones, false));

            // Caso obligatorio = true incorrecto
            ids.clear();
            assertFalse(conOpciones1.isValid(datosOpciones, true));
        } catch (Exception e) {
            fail("No deberia establecer la excepcion");
        }
    }

    @Test
    public void testValidar() {
        when(opcion1.getId()).thenReturn(1);
        when(opcion2.getId()).thenReturn(2);

        // Casos incorrectos
        try {
            ConOpciones conOpciones1 = new ConOpciones(1);
            conOpciones1.addOpcion(opcion1);
            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(10);
            when(datosOpciones.getIdOpciones()).thenReturn(ids);
            conOpciones1.validar(datosOpciones, false);
            fail("Deberia saltar la excepcion TDatosOpcionesIncorrecto");
        } catch (Exception e) {
            if (!(e instanceof TDatosIncorrecto)) {
                fail("Deberia saltar la excepcion TDatosOpcionesIncorrecto");
            }
        }
         // Caso obligatorio vacio
        try {
            ConOpciones conOpciones1 = new ConOpciones(1);
            conOpciones1.addOpcion(opcion1);
            conOpciones1.addOpcion(opcion2);
            ArrayList<Integer> ids = new ArrayList<>();
            when(datosOpciones.getIdOpciones()).thenReturn(ids);
            conOpciones1.validar(datosOpciones, true);
            fail("Deberia saltar la excepcion TDatosOpcionesIncorrecto");
        } catch (Exception e) {
            if (!(e instanceof TDatosIncorrecto)) {
                fail("Deberia saltar la excepcion TDatosOpcionesIncorrecto");
            }
        }

        // Casos correctos
        try {
            ConOpciones conOpciones1 = new ConOpciones(1);
            conOpciones1.addOpcion(opcion1);

            ArrayList<Integer> ids = new ArrayList<>();
            when(datosOpciones.getIdOpciones()).thenReturn(ids);

            // Caso ids vacio
            conOpciones1.validar(datosOpciones, false);

            // Caso con ids numRespuestas = 1
            ids.add(1);
            conOpciones1.validar(datosOpciones, false);

            // Caso con numRespuestas > 1
            conOpciones1.setMaxSelect(2);
            conOpciones1.addOpcion(opcion2);

            conOpciones1.validar(datosOpciones, false);

            // Caso obligatorio true
            conOpciones1.validar(datosOpciones, true);
        } catch (Exception e) {
            fail("No deberia establecer la excepcion");
        }
    }
}
