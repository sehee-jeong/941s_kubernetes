package hanium.project941s.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.List;

@Getter @Setter
public class NewServiceDTO {
    private String githubUrl;
    private String serviceName;
    private int innerPort;
    private int outterPort;
    private HashMap<String, List<String>> env = new HashMap<>();

    @Override
    public String toString() {
        return "NewServiceDTO{" +
                "githubUrl='" + githubUrl + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", port=" + innerPort +
                ", env=" + env +
                '}';
    }
}
