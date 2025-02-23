package nextstep.subway.unit;

import nextstep.line.application.LineService;
import nextstep.line.domain.Color;
import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.line.persistance.LineRepository;
import nextstep.line.presentation.LineResponse;
import nextstep.station.domain.Station;
import nextstep.station.persistance.StationRepository;
import nextstep.station.presentation.StationResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
@SpringBootTest
@Transactional
public class LineServiceTest {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @Test
    void addSection() {
        // given
        Station 건대입구역 = createStation(new Station("건대입구역"));
        Station 구의역 = createStation(new Station("건대입구역"));
        Station 강변역 = createStation(new Station("건대입구역"));
        Line line = createLine(건대입구역, 구의역);

        // when
        // lineService.addSection 호출
        lineService.addSection(line, new Section(구의역, 강변역, 4, line));

        // then
        // lineService.findLineById 메서드를 통해 검증
        LineResponse 이호선 = lineService.findLine(line.getId());
        assertThat(이호선.getStations().stream().map(StationResponse::getId).collect(Collectors.toList()))
                .containsExactly(1L, 2L, 3L);
    }

    private Line createLine(Station 건대입구역, Station 구의역) {
        return lineRepository.save(new Line("2호선", Color.GREEN, 건대입구역, 구의역, 6));
    }

    private Station createStation(Station 건대입구역) {
        return stationRepository.save(건대입구역);
    }
}
