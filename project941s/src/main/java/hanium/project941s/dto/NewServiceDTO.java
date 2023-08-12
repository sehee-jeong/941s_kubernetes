package hanium.project941s.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter @Setter
public class NewServiceDTO {
    private String githubUrl;
    private String serviceName;
    private int port;
    private HashMap<String, String> env = new HashMap<>();

    @Override
    public String toString() {
        return "NewServiceDTO{" +
                "githubUrl='" + githubUrl + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", port=" + port +
                ", env=" + env +
                '}';
    }
}
