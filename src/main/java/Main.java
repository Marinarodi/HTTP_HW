import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;





    public class Main {
        public  record Post (String id, String text, String type, String user, Integer upvotes){

            public Integer getUpvotes() {
                return upvotes;
            }
        }


        public static final String REMOTE_SERVICE_URI = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

        public static void main(String[] args) throws IOException {
            //создание клиента для работы по http
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setUserAgent("My Test Service")
                    .setDefaultRequestConfig(
                            RequestConfig.custom()
                                    .setConnectTimeout(5000) // максимальное время ожидание подключения к серверу
                                    .setSocketTimeout(30000) // максимальное время ожидания получения данных
                                    .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                                    .build()
                    )
                    .build();

            //создание объекта запроса с произвольными заголовками
            HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

            // отправка запроса
            CloseableHttpResponse response = httpClient.execute(request);

            // вывод полученных заголовков
            System.out.println("===ЗАГОЛОВКИ===");
            Arrays.stream(response.getAllHeaders()).forEach(System.out::println);

            // чтение тела ответа
            String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("===ТЕЛО===");
            System.out.println(body);

            //преобразование в объект
            response = httpClient.execute(request);

            ObjectMapper objectMapper = new ObjectMapper();
            List<Post> posts = objectMapper.readValue(
                    response.getEntity().getContent(),
                    new TypeReference<List<Post>>() {}
            );

           posts.stream().filter(value -> value.getUpvotes() != null && value.getUpvotes() > 0).forEach(System.out::println);



        }








    }



