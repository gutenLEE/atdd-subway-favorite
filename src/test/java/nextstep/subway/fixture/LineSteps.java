package nextstep.subway.fixture;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import nextstep.line.domain.Color;
import nextstep.line.presentation.LineRequest;
import nextstep.line.presentation.LineResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineSteps {
    public static LineResponse createLine(Map<String, Object> param) {
        return RestAssured.given().log().all()
                .when()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/lines")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(LineResponse.class);
    }

    public static List<LineResponse> getLines() {
        return RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<List<LineResponse>>() {});
    }


    public static LineResponse 노선을_조회한다(long id) {
        return RestAssured.given().log().all()
                .pathParam("id", id)
                .when()
                .get("/lines/{id}")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(LineResponse.class);
    }

    public static LineResponse 이호선_생성(long upStationId, long downStationId) {
        return LineSteps.이호선_생성(upStationId, downStationId, 10);
    }

    public static LineResponse 이호선_생성(long upStationId, long downStationId, int distance) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", "2호선");
        param.put("color", Color.GREEN.name());
        param.put("upStationId", upStationId);
        param.put("downStationId", downStationId);
        param.put("distance", distance);
        return LineSteps.createLine(param);
    }

    public static LineResponse 노선_생성(LineRequest lineRequest) {
        return LineSteps.createLine(lineRequest);
    }

    public static LineResponse createLine(LineRequest lineRequest) {
        return RestAssured.given()
                .log().all()
                .when()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/lines")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(LineResponse.class);
    }
}
