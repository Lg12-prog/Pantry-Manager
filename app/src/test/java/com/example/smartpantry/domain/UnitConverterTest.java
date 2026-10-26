package com.example.smartpantry.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UnitConverterTest {

    private static final double DELTA = 1e-9;

    @Test
    public void everyUnitInTheDropdownIsKnown() {
        assertEquals(UnitConverter.MASS, UnitConverter.dimensionOf("g"));
        assertEquals(UnitConverter.MASS, UnitConverter.dimensionOf("kg"));
        assertEquals(UnitConverter.VOLUME, UnitConverter.dimensionOf("ml"));
        assertEquals(UnitConverter.VOLUME, UnitConverter.dimensionOf("l"));
        assertEquals(UnitConverter.VOLUME, UnitConverter.dimensionOf("tsp"));
        assertEquals(UnitConverter.VOLUME, UnitConverter.dimensionOf("tbsp"));
        assertEquals(UnitConverter.VOLUME, UnitConverter.dimensionOf("cup"));
        assertEquals(UnitConverter.COUNT, UnitConverter.dimensionOf("pcs"));
    }

    @Test
    public void conversionsToBase() {
        assertEquals(1000.0, UnitConverter.toBase(1, "kg"), DELTA);
        assertEquals(250.0, UnitConverter.toBase(0.25, "kg"), DELTA);
        assertEquals(1500.0, UnitConverter.toBase(1.5, "l"), DELTA);
        assertEquals(5.0, UnitConverter.toBase(1, "tsp"), DELTA);
        assertEquals(30.0, UnitConverter.toBase(2, "tbsp"), DELTA);
        assertEquals(120.0, UnitConverter.toBase(0.5, "cup"), DELTA);
        assertEquals(3.0, UnitConverter.toBase(3, "pcs"), DELTA);
    }

    @Test
    public void unitsAreCaseAndSpaceInsensitive() {
        assertEquals(UnitConverter.MASS, UnitConverter.dimensionOf(" KG "));
        assertEquals(UnitConverter.VOLUME, UnitConverter.dimensionOf("Tbsp."));
    }

    @Test
    public void massVolumeAndCountAreDifferentDimensions() {
        assertFalse(UnitConverter.dimensionOf("g").equals(UnitConverter.dimensionOf("ml")));
        assertFalse(UnitConverter.dimensionOf("g").equals(UnitConverter.dimensionOf("pcs")));
        assertFalse(UnitConverter.dimensionOf("cup").equals(UnitConverter.dimensionOf("pcs")));
    }

    @Test
    public void unknownUnitsOnlyMatchThemselves() {
        assertTrue(UnitConverter.dimensionOf("bunch").equals(UnitConverter.dimensionOf("Bunch")));
        assertFalse(UnitConverter.dimensionOf("bunch").equals(UnitConverter.dimensionOf("pcs")));
        assertEquals(2.0, UnitConverter.toBase(2, "bunch"), DELTA);
    }
}
