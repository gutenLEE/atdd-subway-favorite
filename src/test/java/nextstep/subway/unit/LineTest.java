package nextstep.subway.unit;

import nextstep.line.domain.Color;
import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    Station 건대입구역 = new Station("건대입구역");
    Station 구의역 = new Station("구의역");
    Station 강변역 = new Station("강변역");
    Line line = new Line("2호선", Color.GREEN, 건대입구역, 구의역, 6);


    @Test
    void addSection() {
        line.addSection(new Section(구의역, 강변역, 4, line));

        assertThat(line.getStations()).containsExactly(건대입구역, 구의역, 강변역);
    }

    @Test
    void getStations() {

        Section section2 = new Section(구의역, 강변역, 4, line);
        line.addSection(section2);

        assertThat(line.getStations()).containsExactly(건대입구역, 구의역, 강변역);
    }


    @Test
    void removeMidStation() {

        // given
        Section section2 = new Section(구의역, 강변역, 4, line);
        line.addSection(section2);

        // when
        line.removeStation(구의역);

        // then
        assertThat(line.getStations()).containsExactly(건대입구역, 강변역);
    }

    @Test
    void removeFirstStation() {
        // given
        Section section2 = new Section(구의역, 강변역, 4, line);
        line.addSection(section2);

        // when
        line.removeStation(건대입구역);

        // then
        assertThat(line.getStations()).containsExactly(구의역, 강변역);
    }

    @Test
    void removeLastStation() {
        // given
        Section section2 = new Section(구의역, 강변역, 4, line);
        line.addSection(section2);

        // when
        line.removeStation(강변역);

        // then
        assertThat(line.getStations()).containsExactly(건대입구역, 구의역);
    }



}
