package lab.cleancode.engine;

import lab.cleancode.engine.board.BoardConstraints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ShotStatisticsTest {

    private BoardConstraints boardConstraintsMock;

    @BeforeEach
    void setUp() {
        boardConstraintsMock = mock(BoardConstraints.class);
        given(boardConstraintsMock.getSizeY()).willReturn(5);
        given(boardConstraintsMock.getSizeX()).willReturn(5);
    }

    @ParameterizedTest
    @CsvSource({
            "Idle, Idle, Idle, Idle, 0",
            "Idle, Idle, Idle, Hit, 1",
            "Idle, Idle, Hit, Hit, 2",
            "Miss, Idle, Sunk, Hit, 3",
    })
    void getAllShotsCount(FieldState a, FieldState b, FieldState c, FieldState d, int expectedResult) {
        //given
        ShotStatistics shotStatistics = new ShotStatistics(new FieldState[][]{{a, b, c, d}}, boardConstraintsMock);

        //when
        var result = shotStatistics.getAllShotsCount();

        //then
        assertThat(result).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({
            "Idle, Idle, Idle, Idle, 0",
            "Idle, Idle, Idle, Hit, 0",
            "Idle, Idle, Miss, Miss, 2",
            "Miss, Idle, Sunk, Hit, 1",
    })
    void getMissedShotsCount(FieldState a, FieldState b, FieldState c, FieldState d, int expectedResult) {
        //given
        ShotStatistics shotStatistics = new ShotStatistics(new FieldState[][]{{a, b, c, d}}, boardConstraintsMock);

        //when
        var result = shotStatistics.getMissedShotsCount();

        //then
        assertThat(result).isEqualTo(expectedResult);
    }
}