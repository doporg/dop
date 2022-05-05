package cn.com.devopsplus.dop.server.defect;

import com.github.jeffreyning.mybatisplus.conf.EnableMPP;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.com.devopsplus.dop.server.defect.mapper")
@EnableMPP
public class DefectLocationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DefectLocationApplication.class, args);
	}
}
