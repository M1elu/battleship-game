package lab.cleancode.engine.board;

import lab.cleancode.engine.Ship;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardConfigurationUtilsTest {

    @Test
    void shouldBeCapableToAddShips() {
        //given
        var boardConstraints = new BoardConstraints(5, 5);
        var ships = List.of(new Ship("ship", 2));

        //when
        var result = BoardConfigurationUtils.isCapableToAddShips(ships, boardConstraints);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void shouldNotBeCapableToAddShips() {
        //given
        var boardConstraints = new BoardConstraints(5, 5);
        var ships = List.of(new Ship("ship", 6));

        //when
        var result = BoardConfigurationUtils.isCapableToAddShips(ships, boardConstraints);

        //then
        assertThat(result).isFalse();
    }
}