package aware.domain;

public final class CameraBuilder {
    private String name;
    private Float latitude;
    private Float longitude;

    private CameraBuilder() {}

    public static CameraBuilder aCamera() {
        return new CameraBuilder();
    }

    public CameraBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CameraBuilder setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public CameraBuilder setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public Camera build() {
        return new Camera(name, latitude, longitude);
    }
}
