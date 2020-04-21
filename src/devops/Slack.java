package devops;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class Slack {
	public static void sendAlert(String text) throws Exception{
        HttpPost post = new HttpPost("https://hooks.slack.com/services/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");
        String JSON_STRING = String.format("{\"text\":\"%s\"}", text);
        StringEntity stringEntity = new StringEntity(
        	    JSON_STRING,
        	    ContentType.APPLICATION_JSON);
        post.setEntity(stringEntity);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post)) {
            System.out.println(EntityUtils.toString(response.getEntity()));
        }
	}
	
//    public static void main(String[] args) throws Exception {
//            System.out.println("Testing 1 - Send Http GET request");
//            Slack.sendAlert("hello from java");
//    }
	
}
