package com.sprint.part2.sb1hrbankteam03.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("HR-Bank API 문서")
            .description("Discodeit 프로젝트의 Swagger API 문서입니다.")
        )
        .servers(List.of(
            new Server().url("http://localhost:8080").description("로컬 서버")
        ));
  }
}