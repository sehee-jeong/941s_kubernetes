package hanium.project941s.service;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.domain.common.IntegerResponse;
import com.cdancy.jenkins.rest.domain.common.RequestStatus;
import com.cdancy.jenkins.rest.domain.job.BuildInfo;
import com.cdancy.jenkins.rest.domain.job.JobInfo;
import hanium.project941s.domain.MemberService;
import hanium.project941s.dto.NewServiceDTO;
import hanium.project941s.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JenkinsService {
    private String conAddr = "http://117.16.17.165:30000/job/HYLEE/";
    private String conNameKey = "k8s_jenkins165:111771fcf88cc2f8afaa25ff24b51a313c";

    public boolean createJobToJenkins(NewServiceDTO newServiceDTO, String memberProviderId){
        memberProviderId = memberProviderId.replaceAll("_", "-");
        String serviceName = memberProviderId + "-" + newServiceDTO.getServiceName();

        JenkinsClient client = JenkinsClient.builder()
                .endPoint(conAddr) // Optional.
                .credentials(conNameKey) // Optional.
                .build();

        String configPath = "src/main/resources/static/config.xml";
        try {
            // config.xml 파일 수정 후 String 으로 반환
            String configXml = this.updateConfigXmlFile(configPath, serviceName, newServiceDTO.getOutterPort(), newServiceDTO.getInnerPort(), newServiceDTO.getGithubUrl(), newServiceDTO.getEnv());

            // rest API 로 jenkins job 생성
            RequestStatus result = client.api().jobsApi().create(null, serviceName, configXml);
            if (result.value() == false){
                return false;
            }
            client.api().jobsApi().buildWithParameters(null, serviceName, newServiceDTO.getEnv());
            client.close();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }

        return true;
    }

    private String updateConfigXmlFile(String filePath, String serviceName, int outterPort, int innerPort, String githubUrl, HashMap<String, List<String>> Env) throws Exception{
        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringElementContentWhitespace(true);// 공백 무시
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();

            // Github URL 수정
            Element elementToModify = (Element)document.getElementsByTagName("url").item(0);
            elementToModify.setTextContent(githubUrl); // 요소의 내용을 수정

            // Env 파라미터 추가
            elementToModify = (Element)document.getElementsByTagName("parameterDefinitions").item(0);
            for (Map.Entry<String, List<String>> entry : Env.entrySet()){
                Element top = document.createElement("hudson.model.StringParameterDefinition");

                // Key 할당
                Element content = document.createElement("name");
                content.setTextContent(entry.getKey());
                top.appendChild(content);

                // Value 할당
                content = document.createElement("defaultValue");
                content.setTextContent(entry.getValue().get(0));
                top.appendChild(content);

                // trim
                content = document.createElement("trim");
                content.setTextContent("false");
                top.appendChild(content);


                // 최종 할당
                elementToModify.appendChild(top);
            }

            // shell script에 ServiceName 추가하기
            elementToModify = (Element)document.getElementsByTagName("command").item(1);
            String text = "aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 600359243171.dkr.ecr.ap-northeast-2.amazonaws.com \n" +
                    "sh /941/push-repo.sh " + serviceName + " \n" +
                    "docker build -t " + serviceName + " . \n" +
                    "docker tag " + serviceName + ":latest 600359243171.dkr.ecr.ap-northeast-2.amazonaws.com/" + serviceName + ":latest \n" +
                    "docker push 600359243171.dkr.ecr.ap-northeast-2.amazonaws.com/" + serviceName + ":latest";
            elementToModify.setTextContent(text);

            elementToModify = (Element)document.getElementsByTagName("command").item(2);;
            text = "sh /941/secret.sh " + serviceName + "\n" +
                    "/941/yaml_generator.sh " + serviceName + " " + serviceName + " /941/" + serviceName + ".yaml 600359243171.dkr.ecr.ap-northeast-2.amazonaws.com/" + serviceName + ":latest " + outterPort + " " + innerPort + " VAR1=value1 VAR2=value2 \n" +
                    "kubectl apply -f /941/" + serviceName + ".yaml";
            elementToModify.setTextContent(text);


            // Document를 문자열로 변환
            StringWriter writer = new StringWriter();
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document), new StreamResult(writer));
            String updatedXmlString = writer.toString();

            updatedXmlString = URLEncoder.encode(updatedXmlString, "UTF-8");
            return updatedXmlString;
        }
        catch(Exception ex) {
            throw ex;
        }
    }

    public String checkServiceBuild(String jobName){
        String result = "";

        try{
            JenkinsClient client = JenkinsClient.builder()
                    .endPoint(conAddr) // Optional.
                    .credentials(conNameKey) // Optional.
                    .build();

            // jenkins jobInfo
            JobInfo jobInfo = client.api().jobsApi().jobInfo(null, jobName);

            if (jobInfo.lastBuild() == null){
                return "빌드 준비 중";
            }

            // jenkins BuildInfo
            BuildInfo lastBuild = jobInfo.lastBuild(); // 가장 최근 빌드 가져오기

            // Receive JSON
            String jsonData = this.requestUrl(lastBuild.url() + "api/json");
            JSONObject objData = (JSONObject)new JSONParser().parse(jsonData);

            result = (String) objData.get("result");

            if (result == null){
                result = "빌드 중";
            }
            client.close();
        }
        catch (Exception ex){
            System.out.println(ex);
        }

        return result;
    }

    public String receiveServiceBuildLog(String jobName){
        String result = "";

        try{
            JenkinsClient client = JenkinsClient.builder()
                    .endPoint(conAddr) // Optional.
                    .credentials(conNameKey) // Optional.
                    .build();

            // jenkins jobInfo
            JobInfo jobInfo = client.api().jobsApi().jobInfo(null, jobName);

            // jenkins BuildInfo
            BuildInfo lastBuild = jobInfo.lastBuild(); // 가장 최근 빌드 가져오기

            // Receive Build Log
            String buildLog = this.requestUrl(lastBuild.url() + "logText/progressiveText?start=0");

            result = buildLog;
            client.close();
        }
        catch (Exception ex){
            System.out.println(ex);
        }

        return result;
    }

    private String requestUrl(String strUrl){
        String result = "";
        try {
            URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Authorization 설정
            String encodedValue = Base64.encodeBase64String(conNameKey.getBytes());
            String authorization = "Basic " + new String(encodedValue);
            con.setRequestProperty("Authorization", authorization);

            // JSON으로 받기
            con.setRequestProperty("Content-type", "application/json");
            con.setRequestMethod("GET");

            // 받아온 Json 데이터를 출력 가능한 상태(True)로 변경해줘야 한다. 기본값은 false이다.
            con.setDoOutput(true);

            // 입력 스트림으로 데이터 읽기
            try{
                StringBuffer sb = new StringBuffer();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while(br.ready()) {
                    sb.append(br.readLine());
                    sb.append("\n");
                }
                br.close();
                result = sb.toString();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception ex){
            System.out.println(ex);
        }

        return result;
    }

    public boolean deleteToJenkins(String serviceName){
        JenkinsClient client = JenkinsClient.builder()
                .endPoint(conAddr) // Optional.
                .credentials(conNameKey) // Optional.
                .build();

        HashMap<String, List<String>> params = new HashMap<String, List<String>>();
        params.put("nameSpace", new ArrayList<String>(Arrays.asList(serviceName)));

        try {
            IntegerResponse result = client.api().jobsApi().buildWithParameters(null, "deleteJob", params);
            client.api().jobsApi().delete(null, serviceName);
            client.close();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }

        return true;
    }
}
