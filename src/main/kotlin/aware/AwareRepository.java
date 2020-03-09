package aware;

import aware.domain.BuilderBeanPropertyRowMapper;
import aware.domain.Camera;
import aware.domain.CameraBuilder;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AwareRepository {
    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    public AwareRepository(NamedParameterJdbcTemplate parameterJdbcTemplate) {
        this.parameterJdbcTemplate = parameterJdbcTemplate;
    }

    public void insertCamera(Camera camera) {
        parameterJdbcTemplate.update(
                SQL_INSERT_CAMERA,
                new MapSqlParameterSource("name", camera.getName())
                        .addValue("latitude", camera.getLatitude())
                        .addValue("longitude", camera.getLongitude())
        );
    }

    public List<Camera> getNearbyCameras(Float latitude, Float longitude) {
        return parameterJdbcTemplate.query(
                SQL_GET_NEARBY_CAMERAS,
                new MapSqlParameterSource("minLat", latitude - 0.000005)
                        .addValue("maxLat", latitude + 0.000005)
                        .addValue("minLong", longitude - 0.000005)
                        .addValue("maxLong", longitude + 0.000005),
                new BuilderBeanPropertyRowMapper<>(Camera.class, CameraBuilder.class)
        );
    }

    private static final String SQL_INSERT_CAMERA = "INSERT INTO aware.cameras (name, latitude, longitude) " +
            "VALUES (:name, :latitude, :longitude)";

    private static final String SQL_GET_NEARBY_CAMERAS = "SELECT name, latitude, longitude " +
            "FROM aware.cameras " +
            "WHERE (latitude between :minLat AND :maxLat) AND " +
            "(longitude between :minLong AND :maxLong)";
}
