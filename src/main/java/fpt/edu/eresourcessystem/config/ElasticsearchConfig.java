//package fpt.edu.eresourcessystem.config;
////
////import org.elasticsearch.client.RestHighLevelClient;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.ComponentScan;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.data.elasticsearch.client.ClientConfiguration;
//////import org.springframework.data.elasticsearch.client.erhlc.AbstractElasticsearchConfiguration;
//////import org.springframework.data.elasticsearch.client.erhlc.RestClients;
////import org.springframework.data.elasticsearch.client.erhlc.AbstractElasticsearchConfiguration;
////import org.springframework.data.elasticsearch.client.erhlc.RestClients;
////import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
////
////@Configuration
////@EnableElasticsearchRepositories(basePackages = "fpt.edu.eresourcessystem.repository.elasticsearch")
////@ComponentScan(basePackages = { "fpt.edu.eresourcessystem.service.elasticsearch" })
////public class ElasticConfig extends AbstractElasticsearchConfiguration {
////
////    @Bean
////    @Override
////    public RestHighLevelClient elasticsearchClient() {
////        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
////                .connectedTo("https://b7e5ab.es.us-east-1.aws.found.io")
////                .build();
////
////        return RestClients.create(clientConfiguration)
////                .rest();
////    }
////
////}
////
//import org. apache.http.conn. ssl. TrustAllStrategy;
//import org. apache. http.ssl.SSLContextBuilder;
//import org. springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data. elasticsearch.client.elc.ElasticsearchConfiguration;
//import javax.net.ssl.SSLContext;
//@Configuration
//public class ElasticApplicationConfiguration extends ElasticsearchConfiguration {
//    @Override
//    public ClientConfiguration clientConfiguration() {
//        return ClientConfiguration.builder()
//                .connectedToLocalhost()
//                .usingSsl(buildSSLContext())
//// Execute elasticsearch-8.11.0\bin›elasticsearch-reset-password -u elastic to reset the password
//                .withBasicAuth( "elastic","").build();
//    }
//
//    private static SSLContext buildSSLContext(){
//        try{
//            return new SSLContextBuilder().loadKeyMaterial(null, TrustAllStrategy.INSTANCE).build();
//        }
//        catch (Exception e){
//            throw new RuntimeException(e);
//        }
//    }
//}