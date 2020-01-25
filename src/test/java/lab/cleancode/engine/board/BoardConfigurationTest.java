package lab.cleancode.engine.board;

import lab.cleancode.engine.Coordinate;
import lab.cleancode.engine.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class BoardConfigurationTest {

    private BoardConfiguration objectUnderTest;
    private BoardConstraints boardConstraintsMock;

    @BeforeEach
    void setUp() {
        this.boardConstraintsMock = mock(BoardConstraints.class);
        this.objectUnderTest = new BoardConfiguration(boardConstraintsMock);
    }

    @Test
    void shouldSetShipTest() {
        //given
        var shipMock = mock(Ship.class);

        //when
        objectUnderTest.setShips(List.of(shipMock));

        //then
        assertThat(objectUnderTest.getShips().size()).isEqualTo(1);
        assertThat(objectUnderTest.getShips().get(0)).isEqualTo(shipMock);
    }

    @Test
    void shouldSetShipAndRemoveOldShipsTest() {
        //given
        var shipMock = mock(Ship.class);
        objectUnderTest.setShips(List.of(mock(Ship.class), mock(Ship.class)));
        assertThat(objectUnderTest.getShips().size()).isEqualTo(2);

        //when
        objectUnderTest.setShips(List.of(shipMock));

        //then
        assertThat(objectUnderTest.getShips().size()).isEqualTo(1);
        assertThat(objectUnderTest.getShips().get(0)).isEqualTo(shipMock);
    }

    @Test
    void shouldSetCoordinatesTest() {
        //given
        given(boardConstraintsMock.getSizeY()).willReturn(5);
        given(boardConstraintsMock.getSizeX()).willReturn(5);
        var coords = List.of(new Coordinate(0, 1));

        //when
        var result = objectUnderTest.canSetCoordinates(coords);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void shouldNotSetCoordinatesOutOfBoardTest() {
        //given
        given(boardConstraintsMock.getSizeY()).willReturn(5);
        given(boardConstraintsMock.getSizeX()).willReturn(5);
        var coords = List.of(new Coordinate(0, 7));

        //when
        var result = objectUnderTest.canSetCoordinates(coords);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void shouldNotSetCoordinatesUsedByShipTest() {
        //given
        var coords = List.of(new Coordinate(0, 0), new Coordinate(0, 1));
        var ship = new Ship("test", 1);
        ship.setCoordinates(coords);
        objectUnderTest.setShips(List.of(ship));
        given(boardConstraintsMock.getSizeY()).willReturn(5);
        given(boardConstraintsMock.getSizeX()).willReturn(5);

        //when
        var result = objectUnderTest.canSetCoordinates(List.of(new Coordinate(0, 1)));

        //then
        assertThat(result).isFalse();
    }
}