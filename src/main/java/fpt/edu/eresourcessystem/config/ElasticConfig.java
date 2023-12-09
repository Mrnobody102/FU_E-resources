//package fpt.edu.eresourcessystem.config;
//
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
////import org.springframework.data.elasticsearch.client.erhlc.AbstractElasticsearchConfiguration;
////import org.springframework.data.elasticsearch.client.erhlc.RestClients;
//import org.springframework.data.elasticsearch.client.RestClients;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "fpt.edu.eresourcessystem.repository.elasticsearch")
//@ComponentScan(basePackages = { "fpt.edu.eresourcessystem.service.elasticsearch" })
//public class ElasticConfig extends AbstractElasticsearchConfiguration {
//
//    @Bean
//    @Override
//    public RestHighLevelClient elasticsearchClient() {
//        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo("https://b7e5ab.es.us-east-1.aws.found.io")
//                .build();
//
//        return RestClients.create(clientConfiguration)
//                .rest();
//    }
//
//}
//
