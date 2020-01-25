package lab.cleancode.engine.board;

import lab.cleancode.engine.Coordinate;
import lab.cleancode.engine.FieldState;
import lab.cleancode.engine.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static lab.cleancode.engine.FieldState.Hit;
import static lab.cleancode.engine.FieldState.Idle;
import static lab.cleancode.engine.FieldState.Miss;
import static lab.cleancode.engine.FieldState.Sunk;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class PlayerBoardTest {

    private BoardConfiguration boardConfigurationMock;
    private BoardConstraints boardConstraintsMock;

    @BeforeEach
    void setUp() {
        this.boardConfigurationMock = mock(BoardConfiguration.class);
        this.boardConstraintsMock = mock(BoardConstraints.class);
        given(boardConstraintsMock.getSizeY()).willReturn(2);
        given(boardConstraintsMock.getSizeX()).willReturn(2);
        given(boardConfigurationMock.getShips()).willReturn(List.of());
        given(boardConfigurationMock.getBoardConstraints()).willReturn(boardConstraintsMock);
    }

    @Test
    void shouldGetDefaultStateBoardTest() {
        //given
        var objectUnderTest = new PlayerBoard(boardConfigurationMock);

        //when
        var result = objectUnderTest.getStateBoard();

        //then
        assertThat(result).isEqualTo(new FieldState[][]{{Idle, Idle}, {Idle, Idle}});
    }

    @Test
    void shouldGetDefaultShotBoardTest() {
        //given
        var objectUnderTest = new PlayerBoard(boardConfigurationMock);

        //when
        var result = objectUnderTest.getShotBoard();

        //then
        assertThat(result).isEqualTo(new FieldState[][]{{Idle, Idle}, {Idle, Idle}});
    }

    @Test
    void shouldShootMissTest() {
        //given
        var ship = new Ship("test", 2);
        ship.setCoordinates(List.of(new Coordinate(0, 0), new Coordinate(1, 0)));
        given(boardConfigurationMock.getShips()).willReturn(List.of(ship));
        var objectUnderTest = new PlayerBoard(boardConfigurationMock);

        //when
        objectUnderTest.shoot(new Coordinate(1, 1));
        var result = objectUnderTest.getShotBoard();

        //then
        assertThat(result).isEqualTo(new FieldState[][]{{Idle, Idle}, {Idle, Miss}});
    }

    @Test
    void shouldShootHitTest() {
        //given
        var ship = new Ship("test", 2);
        ship.setCoordinates(List.of(new Coordinate(0, 0), new Coordinate(1, 0)));
        given(boardConfigurationMock.getShips()).willReturn(List.of(ship));
        var objectUnderTest = new PlayerBoard(boardConfigurationMock);

        //when
        objectUnderTest.shoot(new Coordinate(0, 0));
        var result = objectUnderTest.getShotBoard();

        //then
        assertThat(result).isEqualTo(new FieldState[][]{{Hit, Idle}, {Idle, Idle}});
    }

    @Test
    void shouldShootSunkTest() {
        //given
        var ship = new Ship("test", 2);
        ship.setCoordinates(List.of(new Coordinate(0, 0), new Coordinate(1, 0)));
        given(boardConfigurationMock.getShips()).willReturn(List.of(ship));
        var objectUnderTest = new PlayerBoard(boardConfigurationMock);

        //when
        objectUnderTest.shoot(new Coordinate(0, 0));
        objectUnderTest.shoot(new Coordinate(1, 0));
        var result = objectUnderTest.getShotBoard();

        //then
        assertThat(result).isEqualTo(new FieldState[][]{{Sunk, Idle}, {Sunk, Idle}});
    }

    @Test
    void shouldGetNumberOfBattleshipsLeftAsZeroTest() {
        //given
        var objectUnderTest = new PlayerBoard(boardConfigurationMock);

        //when
        var result = objectUnderTest.getNumberOfBattleshipsLeft();

        //then
        assertThat(result).isZero();
    }

    @Test
    void shouldGetNumberOfBattleshipsLeftAsOneTest() {
        //given
        var ship = new Ship("test", 2);
        ship.setCoordinates(List.of(new Coordinate(0, 0), new Coordinate(1, 0)));
        given(boardConfigurationMock.getShips()).willReturn(List.of(ship));
        var objectUnderTest = new PlayerBoard(boardConfigurationMock);

        //when
        var result = objectUnderTest.getNumberOfBattleshipsLeft();

        //then
        assertThat(result).isEqualTo(1);
    }

    @Test
    void shouldBeCoordinateWithinBoardTest() {
        //given
        var coordinate = new Coordinate(0, 0);
        var objectUnderTest = new PlayerBoard(boardConfigurationMock);

        //when
        var result = objectUnderTest.isCoordinateWithinBoard(coordinate);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void shouldBeCoordinateOutOfBoardTest() {
        //given
        var objectUnderTest = new PlayerBoard(boardConfigurationMock);
        var coordinate = new Coordinate(6, 0);

        //when
        var result = objectUnderTest.isCoordinateWithinBoard(coordinate);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void shouldMarkCoordinateAsAlreadyHitTest() {
        //given
        var ship = new Ship("test", 2);
        ship.setCoordinates(List.of(new Coordinate(0, 0), new Coordinate(1, 0)));
        given(boardConfigurationMock.getShips()).willReturn(List.of(ship));
        var objectUnderTest = new PlayerBoard(boardConfigurationMock);

        //when
        objectUnderTest.shoot(new Coordinate(0, 0));
        var result = objectUnderTest.isCoordinateAlreadyHit(new Coordinate(0 ,0));

        //then
        assertThat(result).isTrue();
    }

    @Test
    void shouldNotMarkCoordinateAsAlreadyHitTest() {
        //given
        var objectUnderTest = new PlayerBoard(boardConfigurationMock);

        //when
        var result = objectUnderTest.isCoordinateAlreadyHit(new Coordinate(0 ,0));

        //then
        assertThat(result).isFalse();
    }
}