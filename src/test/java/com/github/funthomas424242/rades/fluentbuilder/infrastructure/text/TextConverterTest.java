package com.github.funthomas424242.rades.fluentbuilder.infrastructure.text;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextConverterTest {

    @Test
    @DisplayName("Konstruktion gültiger Instanzen möglich")
    public void canCreateValidObjectInstanz() {
        final TextConverter converter = new TextConverter(" ");
        assertNotNull(converter);
    }

    @Test
    @DisplayName("Keine Konstruktion ungültiger Instanzen möglich")
    public void cantCreateInvalidObjectInstanz() {
        assertThrows(IllegalArgumentException.class, () -> new TextConverter(null));
    }

}
