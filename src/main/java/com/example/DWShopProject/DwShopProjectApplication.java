package com.example.DWShopProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.hibernate.cfg.JdbcSettings.URL;

@SpringBootApplication
public class DwShopProjectApplication {

	public static void main(String[] args) {

		SpringApplication.run(DwShopProjectApplication.class, args);


	}

}
