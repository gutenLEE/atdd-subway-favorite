package nextstep.line.presentation;


import nextstep.line.domain.Color;
import nextstep.station.presentation.StationResponse;

import java.util.List;
import java.util.Objects;

public class LineResponse {

    private Long id;
    private String name;
    private Color color;

    private List<StationResponse> stations;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, Color color, List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stationResponses;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineResponse that = (LineResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
