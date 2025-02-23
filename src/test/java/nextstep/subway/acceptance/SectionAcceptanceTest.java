package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.line.domain.Line;
import nextstep.line.persistance.LineRepository;
import nextstep.line.presentation.LineResponse;
import nextstep.line.presentation.SectionRequest;
import nextstep.station.presentation.StationResponse;
import nextstep.utils.AcceptanceTest;
import nextstep.subway.fixture.LineSteps;
import nextstep.subway.fixture.SectionSteps;
import nextstep.subway.fixture.StationSteps;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
@Transactional
public class SectionAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private LineRepository lineRepository;
    private StationResponse 구의역;
    private StationResponse 건대입구역;
    private StationResponse 강변역;
    private LineResponse 이호선;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        건대입구역 = StationSteps.createStation("건대입구역");
        구의역 = StationSteps.createStation("구의역");
        강변역 = StationSteps.createStation("강변역");
    }

    @Test
    @DisplayName("노선에 역 추가시 노선 가운데 추가 할 수 있다")
    public void addSectionInMiddle() {

        이호선 = LineSteps.이호선_생성(건대입구역.getId(), 강변역.getId());

        // when
        SectionSteps.라인에_구간을_추가한다(이호선.getId(), new SectionRequest(건대입구역.getId(), 구의역.getId(), 7));

        // then
        LineResponse lineResponse = LineSteps.노선을_조회한다(이호선.getId());
        노선이_가진_역을_검증한다(lineResponse.getStations(), List.of(건대입구역, 구의역, 강변역));
    }


    @Test
    @DisplayName("노선에 역 추가시 새로운 길이를 뺀 나머지를 새롭게 추가된 역과의 길이다")
    public void addSectionDistanceTest() {

        int distance = 10;
        LineResponse 이호선 = LineSteps.이호선_생성(건대입구역.getId(), 강변역.getId(), distance);

        // when
        SectionSteps.라인에_구간을_추가한다(이호선.getId(), new SectionRequest(건대입구역.getId(), 구의역.getId(), 7));

        // then
        Line line = lineRepository.findById(이호선.getId()).get();
        assertThat(line.getDistance()).isEqualTo(distance);

    }


    @Test
    @DisplayName("노선에 역 추가시 기존 구간과 신규 구간의 모두 같을 수 없다")
    public void addSectionInMiddleFail() {
        // when
        Assertions.assertThatThrownBy(
            () -> SectionSteps.라인에_구간을_추가한다(이호선.getId(), new SectionRequest(건대입구역.getId(), 강변역.getId(), 20))
        );
    }


    @Test
    @DisplayName("노선에 등록된 역 제거 시 해당 역이 노선 가운데 있어도 제거할 수 있다")
    public void shouldDeleteMidSection() {

        이호선 = LineSteps.이호선_생성(건대입구역.getId(), 구의역.getId());
        SectionSteps.라인에_구간을_추가한다(이호선.getId(), new SectionRequest(구의역.getId(), 강변역.getId(), 7));

        // when
        SectionSteps.라인의_구간을_삭제한다(이호선.getId(), 구의역.getId());

        // then
        LineResponse lineResponse = LineSteps.노선을_조회한다(이호선.getId());
        노선이_가진_역을_검증한다(lineResponse.getStations(), List.of(건대입구역, 강변역));
    }

    @Test
    @DisplayName("노선에 등록된 역 제거 시 해당 역이 상행 종점역이어도 제거할 수 있다.")
    public void shouldDeleteFirstSection() {

        이호선 = LineSteps.이호선_생성(건대입구역.getId(), 구의역.getId());
        SectionSteps.라인에_구간을_추가한다(이호선.getId(), new SectionRequest(구의역.getId(), 강변역.getId(), 7));

        // when
        SectionSteps.라인의_구간을_삭제한다(이호선.getId(), 건대입구역.getId());

        // then
        LineResponse lineResponse = LineSteps.노선을_조회한다(이호선.getId());
        노선이_가진_역을_검증한다(lineResponse.getStations(), List.of(구의역, 강변역));
    }

    private void 노선이_가진_역을_검증한다(List<StationResponse> actual, List<StationResponse> expect) {
        assertThat(actual).isEqualTo(expect);
    }

}
