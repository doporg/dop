package cn.com.devopsplus.dop.server.defect;

import com.github.jeffreyning.mybatisplus.conf.EnableMPP;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@MapperScan("cn.com.devopsplus.dop.server.defect.mapper")
@EnableMPP
@EnableEurekaClient
public class DefectLocationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DefectLocationApplication.class, args);
	}
}
