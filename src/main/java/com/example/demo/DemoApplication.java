package com.example.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Student API", version = "1.0", description = "Information"))
@SecurityScheme(name = "api", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)//custom lai de test token tren swagger
public class DemoApplication {
	//RESTFUL : TIÊU CHUẨN THIẾT KẾ API
	//POST   : create
	//PUT 	 : update
	//GET    : get
	//DELETE : remove
	//DELETE : remove
	//Ex : /api/<tên đối tượng>
	//     /api/student
	//đặt tên sai : /api/create-new-student
	//vì khi nhìn vào method POST ngta đã biết là create new student rồi

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
