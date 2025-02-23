package nextstep.subway.unit;

import nextstep.line.domain.Color;
import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.line.presentation.LineRequest;
import nextstep.path.domain.PathFinder;
import nextstep.path.domain.dto.PathsDto;
import nextstep.station.domain.Station;
import nextstep.subway.fixture.LineSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 교대역    --- *2호선* ---   강남역
 * |                        |
 * *3호선*                   *신분당선*
 * |                        |
 * 남부터미널역  --- *3호선* ---   양재
 */

public class PathFinderTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");

        이호선 = new Line("2호선", Color.ORANGE, 교대역, 강남역, 1000000);
        신분당선 = new Line("신분당선", Color.ORANGE, 강남역, 양재역, 19999);
        삼호선 = new Line("3호선", Color.ORANGE, 교대역, 남부터미널역, 1);
        삼호선.addSection(new Section(남부터미널역, 양재역, 1, 삼호선));
    }


    @Test
    @DisplayName("최단 경로를 반환한다")
    public void shouldFindShortestPath() {

        PathFinder pathFinder = new PathFinder(List.of(이호선, 신분당선, 삼호선));

        PathsDto pathsDto = pathFinder.findPath(교대역, 양재역);
        assertThat(pathsDto.getPaths()).containsExactly(교대역, 남부터미널역, 양재역);
    }

    @Test
    @DisplayName("최단 경로의 거리를 반환한다")
    public void shouldFindShortestDistance() {

        PathFinder pathFinder = new PathFinder(List.of(이호선, 신분당선, 삼호선));

        PathsDto pathsDto = pathFinder.findPath(교대역, 양재역);
        assertThat(pathsDto.getDistance()).isEqualTo(2);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우는 최단 경로를 탐색할 수 없다")
    public void shouldFailIfStationsAreEquals() {

        PathFinder pathFinder = new PathFinder(List.of(이호선, 신분당선, 삼호선));

        assertThrows(
                IllegalStateException.class,
                () -> pathFinder.findPath(교대역, 교대역)
        );
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 최단 경로를 탐색할 수 없다")
    @Test
    public void shouldFailIfStationsAreNotNeighbors() {

        PathFinder pathFinder = new PathFinder(List.of(이호선, 신분당선, 삼호선));

        assertThrows(
                IllegalArgumentException.class,
                () -> pathFinder.findPath(교대역, new Station("신논현역"))
        );
    }
    
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 최단 경로를 탐색할 수 없다")
    @Test
    public void shouldFailIfStationsAreNotExist() {
        PathFinder pathFinder = new PathFinder(List.of(이호선, 신분당선, 삼호선));

        assertThrows(
                IllegalArgumentException.class,
                () -> pathFinder.findPath(new Station("미등록역"), new Station("미등록역2"))
        );
    }

    @Test
    @DisplayName("두 역이 연결되어 있으면 TRUE를 반환한다")
    public void isConnectedShouldReturnTrueIfStationsAreConnected() {

        PathFinder pathFinder = new PathFinder(List.of(이호선, 신분당선, 삼호선));

        assertThat(pathFinder.isConnected(강남역, 남부터미널역)).isTrue();
    }


    @Test
    @DisplayName("두 역이 연결되어있지 않으면 FALSE를 반환한다")
    public void isConnectedShouldReturnFalse_IfStationsAreConnected() {

        신분당선 = new Line("신분당선", Color.ORANGE, 강남역, 양재역, 19999);
        삼호선 = new Line("3호선", Color.ORANGE, 교대역, 남부터미널역, 1);

        PathFinder pathFinder = new PathFinder(List.of(신분당선, 삼호선));

        assertThat(pathFinder.isConnected(강남역, 남부터미널역)).isFalse();
    }

}
