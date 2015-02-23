package inject.log4j.redis.rest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@JsonTypeName("api")
public class ApiDto {

    @JsonProperty("version")
    @Value("${restApp.version}")
    private String version;

    @JsonProperty("name")
    @Value("${restApp.name}")
    private String name;

    @JsonProperty("buildDate")
    @Value("${restApp.build.timestamp}")
    private String buildDate;

    @Override
    public String toString() {
        return name + " version '" + version + '\'' + " built on " + buildDate;
    }
}
