package lab.cleancode.engine;

import lab.cleancode.engine.board.PlayerBoard;
import lab.cleancode.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class EngineTest {

    private Engine objectUnderTest;
    private GameView gameViewMock;

    @BeforeEach
    void setUp() {
        gameViewMock = mock(GameView.class);
        objectUnderTest = new Engine(gameViewMock);
    }

    @Test
    void shouldReadBoardFromPlayerOnStart() {
        //given
        given(gameViewMock.readBoardFromPlayer()).willReturn(mock(PlayerBoard.class));

        //when
        objectUnderTest.start();

        //then
        then(gameViewMock).should(times(1)).readBoardFromPlayer();
    }

    @Test
    void shouldDisplayBoardOnStart() {
        //given
        given(gameViewMock.readBoardFromPlayer()).willReturn(mock(PlayerBoard.class));

        //when
        objectUnderTest.start();

        //then
        then(gameViewMock).should(times(1)).displayBoard(any(), any());
    }

    @Test
    void shouldDisplayEnd() {
        //given
        given(gameViewMock.readBoardFromPlayer()).willReturn(mock(PlayerBoard.class));

        //when
        objectUnderTest.start();

        //then
        then(gameViewMock).should(times(1)).displayEnd(any(), any());
    }

    @Test
    void shouldRenderDataUntilShipsSunk() {
        //given
        final AtomicInteger counter = new AtomicInteger(3);
        var boardMock = mock(PlayerBoard.class);
        given(boardMock.getNumberOfBattleshipsLeft()).willAnswer($ -> counter.decrementAndGet());
        given(boardMock.isCoordinateAlreadyHit(any())).willReturn(false);
        given(boardMock.isCoordinateWithinBoard(any())).willReturn(true);
        given(gameViewMock.readBoardFromPlayer()).willReturn(boardMock);

        //when
        objectUnderTest.start();

        //then
        then(gameViewMock).should(times(3)).displayBoard(any(), any());
        then(gameViewMock).should(times(2)).displayShotResult(eq(true), eq(false));
    }
}