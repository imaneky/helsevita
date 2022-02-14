package com.urjc.es.helsevita.Test.java;

import org.apache.http.impl.client.BasicCookieStore;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.io.IOException;


public class Test_Selenium_API {

    public static void main(String[] args) throws IOException {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\imane\\OneDrive - Universidad Rey Juan Carlos\\Hospitalito\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://127.0.0.1:8000/");

        //First, we have to login as administrator so we can check the API

        driver.findElement(By.xpath("/html/body/div/div[2]/button[3]")).click();
        driver.findElement(By.xpath("/html/body/div/div[3]/p[2]/a")).click();
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/button")).click();
        driver.findElement(By.xpath("/html/body/main/section/form/center/button[3]")).click();
        driver.findElement(By.xpath("/html/body/main/section/div/form/div[1]/input")).sendKeys("Nico");
        driver.findElement(By.xpath("/html/body/main/section/div/form/div[2]/input[1]")).sendKeys("ponnosun10");
        driver.findElement(By.xpath("/html/body/main/section/div/form/button")).click();

        driver.get("https://127.0.0.1:8000/api/patients");

        String json = driver.findElement(By.tagName("body")).getText();
        Assert.assertTrue(json.contains("Imane"));




    }
}
