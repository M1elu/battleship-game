package lab.cleancode.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShipTest {

    private Ship objectUnderTest;

    @BeforeEach
    void setUp() {
        this.objectUnderTest = new Ship("test", 2);
    }

    @Test
    void shouldNotSunkShipWithNotEnoughHitsTest() {
        //given
        assertThat(objectUnderTest.isSunk()).isFalse();

        //when
        objectUnderTest.hit();

        //then
        assertThat(objectUnderTest.isSunk()).isFalse();
    }


    @Test
    void shouldSunkShipTest() {
        //given
        assertThat(objectUnderTest.isSunk()).isFalse();

        //when
        objectUnderTest.hit();
        objectUnderTest.hit();

        //then
        assertThat(objectUnderTest.isSunk()).isTrue();
    }

    @Test
    void shouldCloneShipTest() {
        //when
        var shipClone = objectUnderTest.clone();

        //then
        assertThat(shipClone).usingRecursiveComparison().isEqualTo(objectUnderTest);
        assertThat(shipClone).isNotSameAs(objectUnderTest);
    }
}